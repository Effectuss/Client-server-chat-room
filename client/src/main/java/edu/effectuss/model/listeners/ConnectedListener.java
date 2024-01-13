package edu.effectuss.model.listeners;

public interface ConnectedListener {
    void successfullyConnected();
    void failedToConnect(String reason);
}
