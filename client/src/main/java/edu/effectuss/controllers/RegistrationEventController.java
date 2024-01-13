package edu.effectuss.controllers;

import edu.effectuss.model.client.RegistrationWorker;
import edu.effectuss.view.listeners.RegistrationEventListeners;

public class RegistrationEventController implements RegistrationEventListeners {
    private final RegistrationWorker registrationWorker;

    public RegistrationEventController(RegistrationWorker registrationWorker) {
        this.registrationWorker = registrationWorker;
    }

    @Override
    public void onRegisterClick(String userNickname) {
        registrationWorker.register(userNickname);
    }
}
