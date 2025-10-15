package fr.anthonus.templates;

public class ExitMessageJson {
    private final String type;
    private final String username;

    public ExitMessageJson(String username) {
        this.type = "ExitMessage";

        this.username = username;
    }
}
