package edu.effectuss.controllers;

import edu.effectuss.model.client.ShutdownWorker;
import edu.effectuss.view.listeners.ShutdownEventListener;

public class ShutdownEventController implements ShutdownEventListener {
    private final ShutdownWorker shutdownWorker;

    public ShutdownEventController(ShutdownWorker shutdownWorker) {
        this.shutdownWorker = shutdownWorker;
    }

    @Override
    public void onShutdown() {
        shutdownWorker.shutdown();
    }
}
