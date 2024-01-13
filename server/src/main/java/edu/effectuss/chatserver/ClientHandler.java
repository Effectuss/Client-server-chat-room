package edu.effectuss.chatserver;

import lombok.extern.log4j.Log4j2;
import edu.effectuss.serialization.exception.MessageSerializationException;
import edu.effectuss.serialization.MessageSerializationUtil;
import edu.effectuss.chatmsg.ClientStatus;
import edu.effectuss.chatmsg.requests.MessageRequest;
import edu.effectuss.chatmsg.requests.RegistrationRequest;
import edu.effectuss.chatmsg.responses.MessageResponse;
import edu.effectuss.chatmsg.responses.RegistrationResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
class ClientHandler implements Runnable {
    private static final String DATE_TIME_FORMAT = "dd MMMM yyyy HH:mm:ss";
    private static final List<ClientHandler> connections = new CopyOnWriteArrayList<>();
    private static final List<String> activeUsers = new CopyOnWriteArrayList<>();
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;
    private String clientNickname;

    public ClientHandler(Socket client) {
        try {
            this.client = client;
            this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
            this.output = new PrintWriter(client.getOutputStream(), true);
            connections.add(this);
        } catch (IOException e) {
            shutdown();
        }
    }

    @Override
    public void run() {
        try {
            handleRegistration();
            broadcastMessageForOtherUsers(MessageSerializationUtil.serializeMessage(
                    new MessageResponse(
                            clientNickname, clientNickname + " join the chat", ClientStatus.CONNECTED)
            ));
            handleMessage();
        } catch (IOException | MessageSerializationException e) {
            shutdown();
        }
    }

    private void handleRegistration() throws IOException, MessageSerializationException {
        while (!client.isClosed()) {

            String jsonRequest = input.readLine();


            RegistrationRequest registrationRequest = MessageSerializationUtil.deserializeMessage(
                    jsonRequest, RegistrationRequest.class
            );

            String requestNickname = registrationRequest.getNickname();
            if (isUniqueNickname(requestNickname)) {
                activeUsers.add(requestNickname);
                clientNickname = requestNickname;
                RegistrationResponse registrationResponse = new RegistrationResponse(
                        true, activeUsers, requestNickname
                );
                output.println(MessageSerializationUtil.serializeMessage(registrationResponse));
                break;
            } else {
                RegistrationResponse registrationResponse = new RegistrationResponse(
                        false, activeUsers, requestNickname
                );
                output.println(MessageSerializationUtil.serializeMessage(registrationResponse));
            }
        }
    }

    private void handleMessage() throws IOException, MessageSerializationException {
        while (!client.isClosed()) {
            String jsonRequest = input.readLine();

            MessageRequest messageRequest = MessageSerializationUtil.deserializeMessage(
                    jsonRequest, MessageRequest.class
            );

            if (messageRequest.getClientStatus().equals(ClientStatus.CONNECTED)) {
                String message = getCurrentDateTime()
                        .concat(" ")
                        .concat(messageRequest.getNickname())
                        .concat(": ")
                        .concat(messageRequest.getMessage());

                broadcastMessageForAllUsers(MessageSerializationUtil.serializeMessage(
                        new MessageResponse(clientNickname, message, ClientStatus.CONNECTED)));
            } else {
                broadcastMessageForAllUsers(
                        MessageSerializationUtil.serializeMessage(new MessageResponse(messageRequest.getNickname(),
                                messageRequest.getNickname().concat(" leave the chat"),
                                ClientStatus.DISCONNECTED))
                );
                shutdown();
            }
        }
    }

    private String getCurrentDateTime() {
        ZonedDateTime currentDateTime = ZonedDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);

        return currentDateTime.format(formatter);
    }

    private boolean isUniqueNickname(String nickname) {
        return activeUsers.stream().noneMatch(nickname::equals);
    }

    private void broadcastMessageForOtherUsers(String message) {
        for (ClientHandler connection : connections) {
            if (!connection.equals(this)) {
                connection.output.println(message);
            }
        }
    }

    private void broadcastMessageForAllUsers(String message) {
        for (ClientHandler connection : connections) {
            connection.output.println(message);
        }
    }

    private void removeConnection() {
        connections.remove(this);
        activeUsers.remove(clientNickname);
    }

    public void shutdown() {
        removeConnection();
        try {
            if (client != null) {
                client.close();
            }
            if (input != null) {
                input.close();
            }
            if (output != null) {
                output.close();
            }
        } catch (IOException e) {
            log.error(e);
        }
    }
}
