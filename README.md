# Client-server-chat-room
This project is a client-server chat application with a GUI interface for the client. It allows users to connect to a server, send and receive messages, and see a list of participants in the chat.

## Features

### Client
- Establishes a connection to the server using the specified address.
- Prompts the user to enter a nickname before connecting to the server. The client connects with this nickname.
- Marks all messages sent by the client with the sending date.
- Provides a graphical user interface consisting of:
  - Message display panel
  - Message input panel
  - Participants list panel
  - Send button
- Allows the user to input the server address and nickname upon startup.
- Gracefully closes the connection with the server when the client window is closed.

### Server
- Listens for client connections on the specified port (configurable in a configuration file
  or set in launch parameters).
- Server have help option (-h,--help) `java -jar --help server.jar`.
- Upon client connection, sends a chat message notifying that the client <nickname> has joined the chat.
- Forwards messages received from one client to all other connected clients.
- Marks each message with the nickname of the client from which it was received.
- Prevents a client from connecting with a nickname that is already in use by another client in the chat.
- Sends a chat message notifying that the client <nickname> has disconnected when a client disconnects.

## Usage

1. Clone the repository.
2. Compile the Java files.
3. To start the server:
  - Run `java -jar server.jar` to start the server on the default port 6666.
  - Run `java -jar -db server.jar` to start the server on the default port 6666.
  - Run `java -jar server.jar -p <port>` to start the server on the specified port.
  - Run `java -jar server.jar -f <path_to_properties_file>` to start the server with configurations from a properties file.
4. Run the compiled client jar file.
5. Input the server address and nickname in the client GUI and click connect.
6. Start chatting!

## Demonstration
