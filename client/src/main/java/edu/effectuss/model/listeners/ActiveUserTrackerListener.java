package edu.effectuss.model.listeners;

import java.util.List;

public interface ActiveUserTrackerListener {
    void activeUsersInitialized(List<String> activeUsers);
    void activeUserDeleted(String activeUser);
    void activeUserAdded(String activeUser);
}
