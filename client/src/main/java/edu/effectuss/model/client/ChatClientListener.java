package edu.effectuss.model.client;

import edu.effectuss.model.listeners.ActiveUserTrackerListener;
import edu.effectuss.model.listeners.ConnectedListener;
import edu.effectuss.model.listeners.MessageReceivedListener;
import edu.effectuss.model.listeners.RegisteredListener;

public interface ChatClientListener {
    void addActiveUserTrackerListener(ActiveUserTrackerListener listener);

    void addConnectedListener(ConnectedListener listener);

    void addMessageReceivedListener(MessageReceivedListener listener);

    void addRegisteredListener(RegisteredListener listener);
}
