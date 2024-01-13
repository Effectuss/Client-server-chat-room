package edu.effectuss.controllers;

import edu.effectuss.model.client.MessageSender;
import edu.effectuss.view.listeners.MessageEventListener;

public class MessageEventController implements MessageEventListener {
    private final MessageSender messageSender;

    public MessageEventController(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public void onMessageSendClick(String message) {
        messageSender.sendMessage(message);
    }
}
