package edu.effectuss.model.listeners;

public interface RegisteredListener {
    void successfullyRegistered(String nickname);
    void failedToRegister(String reason);
}
