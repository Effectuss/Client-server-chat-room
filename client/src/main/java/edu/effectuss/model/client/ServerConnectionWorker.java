package edu.effectuss.model.client;

import java.io.IOException;

public interface ServerConnectionWorker {
    void connectToServer(String serverHost, String serverPort);
}
