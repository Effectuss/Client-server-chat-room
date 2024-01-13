package edu.effectuss.chatserver;

import lombok.extern.log4j.Log4j2;
import edu.effectuss.config.ServerConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Log4j2
public class Server {
    private static final String ERROR_MSG_START_SERVER = "Can't start server!";
    private static final List<ClientHandler> connections = new CopyOnWriteArrayList<>();
    private ServerSocket serverSocket;

    public Server(ServerConfig serverConfig) {
        try {
            this.serverSocket = new ServerSocket(serverConfig.getPort());
        } catch (IOException e) {
            log.error(ERROR_MSG_START_SERVER, e);
            shutdown();
        }
    }

    public void startServer() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                connections.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (IOException e) {
            log.error(e);
        } finally {
            shutdown();
        }
    }

    public void shutdown() {
        try {
            for (ClientHandler clientHandler : connections) {
                clientHandler.shutdown();
            }
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            log.error(e);
        }
    }
}
