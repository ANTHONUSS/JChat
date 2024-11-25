package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Server {
    private final String name;
    private final int port;
    private final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

    public Server(String name, int port) {
        this.name = name;
        this.port = port;
    }


    public void serverMain() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started!");
            System.out.println("Server waiting for connexions...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client " + clientSocket.getInetAddress() + " connected!");

                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void broadcastMessage(String message, ClientHandler sender) {
        synchronized (clients) {
            for (ClientHandler client : clients) {
                if (client != sender) {
                    client.sendMessage(message);
                }
            }
        }
    }

    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }
}