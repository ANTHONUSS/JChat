package fr.anthonus.client;

import com.google.gson.Gson;
import fr.anthonus.Main;
import fr.anthonus.templates.ExitMessageJson;
import fr.anthonus.templates.UserMessageJson;
import jakarta.websocket.Session;
import org.jline.reader.LineReader;

import java.io.IOException;

public class JChatClient {
    private static final JChatClient INSTANCE = new JChatClient();
    private Session session;

    private String username;

    public JChatClient() {}

    public static JChatClient getInstance() {
        return INSTANCE;
    }

    public void setSession(Session session) {
        this.session = session;
    }
    public Session getSession() {
        return this.session;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getUsername() {
        return this.username;
    }

    public void run() {
        LineReader reader = Main.reader;

        String message = reader.readLine("> ");
        while (!message.equals("exit")) {
            try {

                Gson gson = new Gson();
                String userMessageJson = gson.toJson(new UserMessageJson(username, message));
                session.getBasicRemote().sendText(userMessageJson);

            } catch (IOException e) {
                System.err.println("Failed to send message: " + e.getMessage());
            }

            message = reader.readLine("> ");
        }
        reader.printAbove("Exiting...");

        try {
            Gson gson = new Gson();
            String userMessageJson = gson.toJson(new ExitMessageJson(username));
            session.getBasicRemote().sendText(userMessageJson);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            session.close();
            session = null;
            username = null;
        } catch (IOException e) {
            System.err.println("Failed to close session: " + e.getMessage());
        }

    }
}
