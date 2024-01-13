package edu.effectuss.view;

import edu.effectuss.model.client.ChatClientListener;
import edu.effectuss.model.listeners.ActiveUserTrackerListener;
import edu.effectuss.model.listeners.ConnectedListener;
import edu.effectuss.model.listeners.MessageReceivedListener;
import edu.effectuss.model.listeners.RegisteredListener;
import edu.effectuss.view.forms.ChatWindow;
import edu.effectuss.view.forms.InformationWindow;
import edu.effectuss.view.forms.RegistrationWindow;
import edu.effectuss.view.forms.ServerConnectionWindow;
import edu.effectuss.view.listeners.MessageEventListener;
import edu.effectuss.view.listeners.RegistrationEventListeners;
import edu.effectuss.view.listeners.ServerConnectionEventListener;
import edu.effectuss.view.listeners.ShutdownEventListener;

import javax.swing.*;
import java.util.List;

public class ChatApplicationView extends JFrame
        implements ActiveUserTrackerListener, MessageReceivedListener,
        RegisteredListener, ConnectedListener {

    private final ChatWindow chatWindow;
    private final ServerConnectionWindow serverConnectionWindow;
    private final RegistrationWindow registrationWindow;
    private final InformationWindow informationWindow;


    public ChatApplicationView(ChatClientListener clientListener, MessageEventListener messageEventListener,
                               ServerConnectionEventListener serverConnectionEventListener,
                               RegistrationEventListeners registrationEventListeners,
                               ShutdownEventListener shutdownEventListener) {
        chatWindow = new ChatWindow(messageEventListener, shutdownEventListener);
        serverConnectionWindow = new ServerConnectionWindow(serverConnectionEventListener);
        registrationWindow = new RegistrationWindow(registrationEventListeners, shutdownEventListener);
        informationWindow = new InformationWindow(this);
        setupListeners(clientListener);
    }

    private void setupListeners(ChatClientListener clientListener) {
        clientListener.addActiveUserTrackerListener(this);
        clientListener.addConnectedListener(this);
        clientListener.addMessageReceivedListener(this);
        clientListener.addRegisteredListener(this);
    }

    public void showApplication() {
        serverConnectionWindow.setVisible(true);
    }

    @Override
    public void activeUsersInitialized(List<String> activeUsers) {
        chatWindow.activeUsersInitialized(activeUsers);
    }

    @Override
    public void activeUserDeleted(String activeUser) {
        chatWindow.activeUserDeleted(activeUser);
    }

    @Override
    public void activeUserAdded(String activeUser) {
        chatWindow.activeUserAdded(activeUser);
    }

    @Override
    public synchronized void successfullyConnected() {
        serverConnectionWindow.setVisible(false);
        serverConnectionWindow.dispose();
        registrationWindow.setVisible(true);
    }

    @Override
    public synchronized void failedToConnect(String reason) {
        informationWindow.showWindow(reason);
    }

    @Override
    public synchronized void messageReceived(String message) {
        chatWindow.addMessage(message);
    }

    @Override
    public void failedToReceiveMessage(String reason) {
        informationWindow.showWindow(reason);
    }

    @Override
    public synchronized void successfullyRegistered(String nickname) {
        registrationWindow.setVisible(false);
        registrationWindow.dispose();
        chatWindow.changeUserNickname(nickname);
        chatWindow.setVisible(true);
    }

    @Override
    public synchronized void failedToRegister(String reason) {
        informationWindow.showWindow(reason);
    }
}
