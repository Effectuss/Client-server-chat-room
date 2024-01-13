package edu.effectuss.controllers;

import edu.effectuss.model.client.ServerConnectionWorker;
import edu.effectuss.view.listeners.ServerConnectionEventListener;

public class ServerConnectionEventController implements ServerConnectionEventListener {

    private final ServerConnectionWorker serverConnectionWorker;

    public ServerConnectionEventController(ServerConnectionWorker serverConnectionWorker) {
        this.serverConnectionWorker = serverConnectionWorker;
    }

    @Override
    public void onServerConnectClick(String serverHost, String serverPort) {
        serverConnectionWorker.connectToServer(serverHost, serverPort);
    }
}
