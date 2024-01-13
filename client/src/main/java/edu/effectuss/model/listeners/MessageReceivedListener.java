package edu.effectuss.model.listeners;

public interface MessageReceivedListener {
    void messageReceived(String message);
    void failedToReceiveMessage(String reason);
}
