import java.io.*;
import java.net.*;
import java.util.*;

public class ChatClient {
    public static void main(String[] args) {
        // Use try-with-resources to avoid leaks
        try (Socket socket = new Socket("localhost", 1234);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner sc = new Scanner(System.in)) {

            // Ask for username
            System.out.print("Enter your username: ");
            String username = sc.nextLine();
            out.println(username + " joined the chat 👋");

            // Thread to read incoming messages
            new Thread(() -> {
                try {
                    String msg;
                    while ((msg = in.readLine()) != null) {
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    System.out.println("❌ Disconnected from server.");
                }
            }).start();

            // Sending messages
            while (true) {
                String msg = sc.nextLine();
                if (msg.equalsIgnoreCase("/exit")) {
                    out.println(username + " left the chat 👋");
                    break; // socket will auto-close because of try-with-resources
                }
                out.println("[" + username + "]: " + msg);
            }

        } catch (UnknownHostException e) {
            System.out.println("❌ Server not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("❌ I/O error: " + e.getMessage());
        }
    }
}
