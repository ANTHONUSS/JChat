package fr.anthonus.templates;

public class UserMessageJson {
    private final String type;
    private final String username;
    private final String message;

    public UserMessageJson(String username, String message) {
        this.type = "UserMessage";

        this.username = username;
        this.message = message;
    }
}
