package edu.effectuss.config.exception;

public class ServerConfigException extends RuntimeException {
    public ServerConfigException(String message) {
        super(message);
    }

    public ServerConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
