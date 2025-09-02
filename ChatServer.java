import java.io.*;
import java.net.*;
import java.util.*;

class ClientHandler implements Runnable {
    private final Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private String username;
    private final List<ClientHandler> clients;
    private final PrintWriter logWriter;

    public ClientHandler(Socket socket, List<ClientHandler> clients, PrintWriter logWriter) throws IOException {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.clients = clients;
        this.logWriter = logWriter;
    }

    @Override
    public void run() {
        try {
            out.println("Enter your username:");
            this.username = in.readLine();

            synchronized (clients) {
                clients.add(this);
            }

            broadcast("🟢 " + username + " joined the chat.");

            String msg;
            while ((msg = in.readLine()) != null) {
                if (msg.equalsIgnoreCase("/exit")) {
                    broadcast("🔴 " + username + " left the chat.");
                    break;
                } else if (msg.startsWith("/msg ")) {
                    handlePrivateMessage(msg);
                } else {
                    broadcast(username + ": " + msg);
                }
            }
        } catch (IOException e) {
            System.err.println("❌ Error with client " + username + ": " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException ignored) {}
            synchronized (clients) {
                clients.remove(this);
            }
        }
    }

    private void handlePrivateMessage(String msg) {
        String[] parts = msg.split(" ", 3);
        if (parts.length < 3) {
            out.println("❌ Usage: /msg <username> <message>");
            return;
        }
        String targetUser = parts[1];
        String message = parts[2];

        synchronized (clients) {
            for (ClientHandler c : clients) {
                if (c.username.equals(targetUser)) {
                    c.out.println("📩 [Private from " + username + "]: " + message);
                    this.out.println("📤 [Private to " + targetUser + "]: " + message);
                    log(username + " -> " + targetUser + ": " + message);
                    return;
                }
            }
        }
        out.println("❌ User not found: " + targetUser);
    }

    private void broadcast(String msg) {
        log(msg);
        synchronized (clients) {
            for (ClientHandler c : clients) {
                c.out.println(msg);
            }
        }
    }

    private void log(String msg) {
        synchronized (logWriter) {
            logWriter.println(new Date() + " " + msg);
            logWriter.flush();
        }
    }
}

public class ChatServer {
    private static final int PORT = 1234;

    public static void main(String[] args) {
        final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());

        try (ServerSocket server = new ServerSocket(PORT);
             PrintWriter logWriter = new PrintWriter(new FileWriter("chatlog.txt", true))) {

            System.out.println("💬 Chat Server started on port " + PORT + "...");

            while (true) {
                Socket socket = server.accept();
                ClientHandler handler = new ClientHandler(socket, clients, logWriter);
                new Thread(handler).start();
            }
        } catch (IOException e) {
            System.err.println("❌ Server error: " + e.getMessage());
        }
    }
}
