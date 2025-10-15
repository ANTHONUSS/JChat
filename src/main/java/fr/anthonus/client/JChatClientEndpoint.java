package fr.anthonus.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.anthonus.Main;
import fr.anthonus.templates.JoinMessageJson;
import jakarta.websocket.*;
import org.jline.reader.LineReader;

@ClientEndpoint
public class JChatClientEndpoint {
    LineReader reader = Main.reader;

    @OnOpen
    public void onOpen(Session session) {
        Gson gson = new Gson();
        String joinMessageJson = gson.toJson(new JoinMessageJson(JChatClient.getInstance().getUsername()));
        try {
            session.getBasicRemote().sendText(joinMessageJson);
        } catch (Exception e) {
            System.err.println("Failed to send join message: " + e.getMessage());
        }

        reader.printAbove("You joined the chat.");
    }

    @OnMessage
    public void onMessage(String message) {

        JsonObject jsonObject = new JsonParser().parse(message).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        switch (type) {
            case "UserMessage" -> handleUserMessage(jsonObject);
            case "JoinMessage" -> handleJoinMessage(jsonObject);
            case "ExitMessage" -> handleExitMessage(jsonObject);
        }
    }


    @OnClose
    public void onClose(Session session) {
        reader.printAbove("You left the chat.");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error on session " + session.getId() + ": " + throwable.getMessage());
    }


    private void handleUserMessage(JsonObject jsonObject) {
        String username = jsonObject.get("username").getAsString();
        String userMessage = jsonObject.get("message").getAsString();

        reader.printAbove("[" + username + "] => " + userMessage);
    }

    private void handleJoinMessage(JsonObject jsonObject) {
        String username = jsonObject.get("username").getAsString();

        reader.printAbove(username + " has joined the chat.");
    }

    private void handleExitMessage(JsonObject jsonObject) {
        String username = jsonObject.get("username").getAsString();

        reader.printAbove(username + " has left the chat.");
    }
}
