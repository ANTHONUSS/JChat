package fr.anthonus.server;

import fr.anthonus.Main;
import org.glassfish.tyrus.server.Server;
import org.jline.reader.LineReader;

public class JChatServer {
    private static final JChatServer INSTANCE = new JChatServer();

    private Server server;

    private String serverName;
    private String ip;
    private int port;

    public JChatServer() {}

    public static JChatServer getInstance() {
        return INSTANCE;
    }

    public Server getServer() {
        return server;
    }
    public void setServer(Server server) {
        this.server = server;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }
    public String getServerName() {
        return this.serverName;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getIp() {
        return this.ip;
    }
    public void setPort(int port) {
        this.port = port;
    }
    public int getPort() {
        return this.port;
    }

    public void run() {
        LineReader reader = Main.reader;

        String command = reader.readLine("> ");
        while (!command.equals("exit")) {
            switch (command) {

            }
            command = reader.readLine("> ");
        }
        reader.printAbove("Exiting...");
        server.stop();
        server = null;
        serverName = null;
        ip = null;
        port = 0;

    }

}
