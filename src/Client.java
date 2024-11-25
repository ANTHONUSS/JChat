import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private String pseudo;
    private final String SERVER_IP;
    private final int SERVER_PORT;

    public Client(String pseudo, String IP, int PORT) {
        this.pseudo = pseudo;
        this.SERVER_IP = IP;
        this.SERVER_PORT = PORT;
    }

    public void clientMain() {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to server.");

            new Thread(() -> {
                try{
                    String serverMessage;
                    while ((serverMessage = in.readLine()) != null){
                        System.out.println("Server : "+serverMessage);
                    }
                } catch (IOException e){
                    System.out.println("Connection lost.");
                }finally {
                    try{
                        socket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();

            String userMessage;
            while ((userMessage = in.readLine()) != null){
                out.println(userMessage);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
