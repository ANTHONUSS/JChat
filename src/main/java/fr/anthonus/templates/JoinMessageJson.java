package fr.anthonus.templates;

public class JoinMessageJson {
    private final String type;
    private final String username;

    public JoinMessageJson(String username) {
        this.type = "JoinMessage";

        this.username = username;
    }
}
