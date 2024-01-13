package edu.effectuss.model.client.impl;

import edu.effectuss.model.client.*;
import lombok.extern.log4j.Log4j2;
import edu.effectuss.serialization.exception.MessageSerializationException;
import edu.effectuss.serialization.MessageSerializationUtil;
import edu.effectuss.chatmsg.ClientStatus;
import edu.effectuss.chatmsg.requests.MessageRequest;
import edu.effectuss.chatmsg.requests.RegistrationRequest;
import edu.effectuss.chatmsg.responses.MessageResponse;
import edu.effectuss.chatmsg.responses.RegistrationResponse;
import edu.effectuss.model.listeners.ActiveUserTrackerListener;
import edu.effectuss.model.listeners.ConnectedListener;
import edu.effectuss.model.listeners.MessageReceivedListener;
import edu.effectuss.model.listeners.RegisteredListener;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
public class ChatClient implements
        ChatClientListener, ServerConnectionWorker, MessageSender,
        RegistrationWorker, ShutdownWorker {
    private static final int DEFAULT_PORT = 6666;
    private static final String DEFAULT_HOST = "localhost";
    private static final int MAX_PORT_VALUE = 65535;
    private static final int MIN_PORT_VALUE = 1;
    private static final int MAX_MESSAGE_LEN = 1024;
    private static final String ERROR_SERVER_CONNECT =
            "\nPlease try connected to\nhost: " + DEFAULT_HOST + "\nport: " + DEFAULT_PORT;
    private static final String ERROR_REGISTRATION = "Nickname already in use\nPlease try another nickname";
    private final List<ActiveUserTrackerListener> activeUserTrackerListeners = new CopyOnWriteArrayList<>();
    private final List<ConnectedListener> connectedListeners = new CopyOnWriteArrayList<>();
    private final List<MessageReceivedListener> messageReceivedListeners = new CopyOnWriteArrayList<>();
    private final List<RegisteredListener> registeredListeners = new CopyOnWriteArrayList<>();
    private String clientNickname;
    private Socket socket;
    private BufferedReader inputStream;
    private PrintWriter outputStream;

    @Override
    public void connectToServer(String serverHost, String serverPort) {
        if (!isValidPort(serverPort)) {
            notifyFailedToConnect(ERROR_SERVER_CONNECT);
        } else {
            try {
                tryConnectToServer(serverHost, serverPort);
                notifySuccessfullyConnected();
                startListeningResponseFromServer();
            } catch (IOException e) {
                notifyFailedToConnect(e.getMessage() + ERROR_SERVER_CONNECT);
                closeResources();
            }
        }
    }

    private void tryConnectToServer(String serverHost, String serverPort)
            throws IOException {
        socket = new Socket(serverHost, Integer.parseInt(serverPort));
        inputStream = new BufferedReader(
                new InputStreamReader(socket.getInputStream())
        );
        outputStream = new PrintWriter(socket.getOutputStream(), true);
    }

    private boolean isValidPort(String port) {
        try {
            int portNumber = Integer.parseInt(port);
            return portNumber >= MIN_PORT_VALUE && portNumber <= MAX_PORT_VALUE;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void sendMessage(String message) {
        try {
            if (!isValidLengthMessage(message)) {
                notifyFailedToReceiveMessage("Maximum length of a single message: " + MAX_MESSAGE_LEN);
                return;
            }
            MessageRequest messageRequest = new MessageRequest(
                    message, clientNickname, ClientStatus.CONNECTED
            );
            outputStream.println(MessageSerializationUtil.serializeMessage(messageRequest));
        } catch (MessageSerializationException e) {
            log.error(e.getMessage());
            closeResources();
        }
    }

    private boolean isValidLengthMessage(String message) {
        return message != null && message.length() < MAX_MESSAGE_LEN;
    }

    @Override
    public void register(String userNickname) {
        try {
            RegistrationRequest registrationRequest = new RegistrationRequest(userNickname);
            outputStream.println(MessageSerializationUtil.serializeMessage(registrationRequest));
        } catch (MessageSerializationException e) {
            closeResources();
        }
    }

    private void startListeningResponseFromServer() {
        new Thread(() -> {
            listeningRegistrationResponse();
            listeningMessageResponse();
        }).start();
    }

    private void listeningRegistrationResponse() {
        while (!socket.isClosed()) {
            try {
                RegistrationResponse response = MessageSerializationUtil.deserializeMessage(
                        inputStream.readLine(),
                        RegistrationResponse.class);
                if (response.isSuccess()) {
                    clientNickname = response.getNickname();
                    notifySuccessfullyRegistered();
                    notifyActiveUsersInitialized(response.getActiveUsers());
                    break;
                } else {
                    notifyFailedToRegister(ERROR_REGISTRATION);
                }
            } catch (IOException | MessageSerializationException e) {
                notifyFailedToRegister(e.getMessage());
                closeResources();
            }
        }
    }

    private void listeningMessageResponse() {
        while (!socket.isClosed()) {
            try {
                MessageResponse response = MessageSerializationUtil.deserializeMessage(
                        inputStream.readLine(), MessageResponse.class);
                if (response.getClientStatus().equals(ClientStatus.DISCONNECTED) &&
                        response.getNickname().equals(clientNickname)) {
                    closeResources();
                } else if (response.getClientStatus().equals(ClientStatus.CONNECTED)) {
                    notifyMessageReceived(response.getMessage());
                    notifyActiveUserAdded(response.getNickname());
                } else {
                    notifyMessageReceived(response.getMessage());
                    notifyActiveUserDeleted(response.getNickname());
                }
            } catch (IOException | MessageSerializationException e) {
                closeResources();
                log.error(e.getMessage());
            }
        }
    }

    @Override
    public void shutdown() {
        try {
            MessageRequest disconnectionRequest = new MessageRequest("",
                    clientNickname,
                    ClientStatus.DISCONNECTED
            );
            outputStream.println(MessageSerializationUtil.serializeMessage(disconnectionRequest));
        } catch (MessageSerializationException e) {
            log.error(e);
        } finally {
            closeResources();
        }
    }

    private void closeResources() {
        try {
            if (socket != null) {
                socket.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            log.error(e);
        }
    }

    @Override
    public void addActiveUserTrackerListener(ActiveUserTrackerListener listener) {
        activeUserTrackerListeners.add(listener);
    }

    @Override
    public void addConnectedListener(ConnectedListener listener) {
        connectedListeners.add(listener);
    }

    @Override
    public void addMessageReceivedListener(MessageReceivedListener listener) {
        messageReceivedListeners.add(listener);
    }

    @Override
    public void addRegisteredListener(RegisteredListener listener) {
        registeredListeners.add(listener);
    }

    private void notifyFailedToReceiveMessage(String reason) {
        for (MessageReceivedListener listener : messageReceivedListeners) {
            listener.failedToReceiveMessage(reason);
        }
    }

    private void notifyActiveUserAdded(String userNickname) {
        for (ActiveUserTrackerListener listener : activeUserTrackerListeners) {
            listener.activeUserAdded(userNickname);
        }
    }

    private void notifyActiveUsersInitialized(List<String> activeUsers) {
        for (ActiveUserTrackerListener listener : activeUserTrackerListeners) {
            listener.activeUsersInitialized(activeUsers);
        }
    }

    private void notifyActiveUserDeleted(String userNickname) {
        for (ActiveUserTrackerListener listener : activeUserTrackerListeners) {
            listener.activeUserDeleted(userNickname);
        }
    }

    private void notifySuccessfullyConnected() {
        for (ConnectedListener listener : connectedListeners) {
            listener.successfullyConnected();
        }
    }

    private void notifyFailedToConnect(String reason) {
        for (ConnectedListener listener : connectedListeners) {
            listener.failedToConnect(reason);
        }
    }

    private void notifySuccessfullyRegistered() {
        for (RegisteredListener listener : registeredListeners) {
            listener.successfullyRegistered(clientNickname);
        }
    }

    private void notifyFailedToRegister(String reason) {
        for (RegisteredListener listener : registeredListeners) {
            listener.failedToRegister(reason);
        }
    }

    private void notifyMessageReceived(String message) {
        for (MessageReceivedListener listener : messageReceivedListeners) {
            listener.messageReceived(message);
        }
    }
}
