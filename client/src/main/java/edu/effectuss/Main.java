package edu.effectuss;

import edu.effectuss.controllers.MessageEventController;
import edu.effectuss.controllers.RegistrationEventController;
import edu.effectuss.controllers.ServerConnectionEventController;
import edu.effectuss.controllers.ShutdownEventController;
import edu.effectuss.model.client.impl.ChatClient;
import edu.effectuss.view.ChatApplicationView;

public class Main {
    public static void main(String[] args) {
        ChatApplicationView chatApplicationView = getChatApplicationView();
        chatApplicationView.showApplication();
    }

    private static ChatApplicationView getChatApplicationView() {
        ChatClient client = new ChatClient();

        MessageEventController messageEventController = new MessageEventController(client);
        RegistrationEventController registrationEventController = new RegistrationEventController(client);
        ServerConnectionEventController serverConnectionEventController = new ServerConnectionEventController(client);
        ShutdownEventController shutdownEventController = new ShutdownEventController(client);

        return new ChatApplicationView(client,
                messageEventController, serverConnectionEventController,
                registrationEventController, shutdownEventController
        );
    }
}