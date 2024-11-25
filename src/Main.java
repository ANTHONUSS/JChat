import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean exit = false;
        int port;
        Scanner scanner = new Scanner(System.in);
        while (!exit) {
            String command = scanner.nextLine();

            switch (command) {
                case "/exit":
                    System.out.println("Closing program...");
                    exit = true;
                    break;
                case "/help":
                    Commands.help();
                    break;
                case "/host":
                    System.out.print("Server Name : ");
                    String name = scanner.nextLine();

                    System.out.print("Server port : ");
                    port = scanner.nextInt();
                    scanner.nextLine();

                    Server server = new Server(name, port);
                    server.serverMain();

                    break;
                case "/join":
                    System.out.print("Server IP : ");
                    String ip = scanner.nextLine();

                    System.out.print("Server port : ");
                    port = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter your pseudo : ");
                    String pseudo = scanner.nextLine();

                    Client client = new Client(pseudo, ip, port);
                    client.clientMain();

                    break;
                default:
                    System.out.println("This is not a valid command");
                    break;
            }
        }
    }
}