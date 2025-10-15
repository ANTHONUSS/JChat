package fr.anthonus.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.anthonus.Main;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.jline.reader.LineReader;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/")
public class JChatServerEndpoint {
    private static final Set<Session> sessions = new CopyOnWriteArraySet<>();
    LineReader reader = Main.reader;

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        reader.printAbove("Client connected: " + session.getId());

    }

    @OnMessage
    public void onMessage(String message, Session session) {
        reader.printAbove("message received from " + session.getId() + ": " + message);

        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        switch (type) {
            case "UserMessage", "JoinMessage", "ExitMessage" -> broadcast(message, session);
        }

    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
        reader.printAbove("Client disconnected: " + session.getId());

    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error on session " + session.getId() + ": " + throwable.getMessage());

    }



    private void broadcast(String message) {
        for (Session s : sessions) {
            if (s.isOpen()) {
                try {
                    s.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private void broadcast(String message, Session actuelSession) {
        for (Session s : sessions) {
            if (s.isOpen() && !s.getId().equals(actuelSession.getId())) {
                try {
                    s.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
