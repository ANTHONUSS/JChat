package fr.anthonus;

import fr.anthonus.client.JChatClient;
import fr.anthonus.client.JChatClientEndpoint;
import fr.anthonus.server.JChatServer;
import fr.anthonus.server.JChatServerEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import org.glassfish.tyrus.server.Server;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.URI;

public class Main {
    public static Terminal terminal;
    public static LineReader reader;

    public static void main(String[] args) throws IOException {

        terminal = TerminalBuilder.builder()
                .system(true)
                .build();

        reader = LineReaderBuilder.builder()
                .terminal(terminal)
                .build();

        String command = reader.readLine("> ");
        while (!command.equals("exit")) {
            switch (command) {

                case "host" -> {
                    reader.printAbove("Enter server name : ");
                    String serverName = reader.readLine("> ");

                    int port = getAvailablePorts();
                    if (port == -1) {
                        System.err.println("No available ports found.");
                        break;
                    }

                    String ip = InetAddress.getLocalHost().getHostAddress();
                    reader.printAbove("Server IP : " + ip);
                    reader.printAbove("Server Port : " + port);

                    JChatServer jChatServer = JChatServer.getInstance();
                    jChatServer.setServerName(serverName);
                    jChatServer.setIp(ip);
                    jChatServer.setPort(port);

                    Server server = new Server(ip, port, "/", null, JChatServerEndpoint.class);
                    jChatServer.setServer(server);
                    try {
                        server.start();
                        reader.printAbove("WebSocket Server started at ws://" + ip + ":" + port + "/");
                        reader.printAbove("Type 'exit' to stop the server.");
                    } catch (DeploymentException e) {
                        System.err.println("Failed to start WebSocket Server: " + e.getMessage());
                    }

                    jChatServer.run();
                }

                case "join" -> {

                    if (JChatClient.getInstance().getUsername() == null) {
                        reader.printAbove("Enter username : ");
                        String username = reader.readLine("> ");
                        JChatClient.getInstance().setUsername(username);
                    }

                    reader.printAbove("Enter server IP : ");
                    String ip = reader.readLine("> ");
                    reader.printAbove("Enter server port : ");
                    int port = Integer.parseInt(reader.readLine("> "));

                    String uri = "ws://" + ip + ":" + port + "/";
                    reader.printAbove("Connecting to " + uri + "...");
                    try {
                        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
                        Session session = container.connectToServer(JChatClientEndpoint.class, URI.create(uri));
                        JChatClient.getInstance().setSession(session);

                    } catch (DeploymentException | IOException e) {
                        System.err.println("Failed to connect to Server " + e.getMessage());
                    }

                    JChatClient.getInstance().run();
                }

            }

            command = reader.readLine("> ");
        }
    }

    public static int getAvailablePorts() {
        int start = 3000;
        int end = 9000;
        for (int port = start; port <= end; port++) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                return port;
            } catch (Exception e) {
                // Port is in use
            }
        }
        return -1; // No available port found
    }

}