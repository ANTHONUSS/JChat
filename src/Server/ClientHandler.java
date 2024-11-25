package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final Server server;
    private PrintWriter out;
    private String pseudo;

    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
    }

    @Override
    public void run() {
        boolean running = true;
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        ) {
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            pseudo = in.readLine();
            System.out.println(pseudo + " joined the chat");
            server.broadcastMessage(pseudo+" joined the chat.", this);

            String message;
            while (running && (message = in.readLine()) != null) {
                if (message.startsWith("/")) {
                    handleCommand(message);
                    if(message.equals("/exit")){
                        running = false;
                    }
                } else {
                    String formattedMessage = "["+pseudo+"] > " + message;
                    System.out.println(formattedMessage);
                    server.broadcastMessage(formattedMessage, this);
                }
            }

        } catch (IOException e) {
            System.out.println("Error with client: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                server.removeClient(this);
                System.out.println(pseudo+" disconnected.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        if(out != null) {
            out.println(message);
        }
    }

    private void handleCommand(String command) {
        switch (command) {
            case "/exit":
                System.out.println(pseudo+" is exiting...");
                server.broadcastMessage(pseudo+" left the chat", this);
                try{
                    clientSocket.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
                break;
            default:
                out.println("Unknown command.");
                break;
        }
    }
}
