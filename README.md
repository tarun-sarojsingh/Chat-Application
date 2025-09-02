# Chat-Application
"I built a chat application using Java Sockets. It follows a client–server architecture where multiple clients can connect to a central server. The server manages client connections, broadcasts messages, and also supports private messaging. Clients can join, send messages, and exit gracefully."


🗨️ Java Chat Application
A multi-client chat application built in Java using sockets and multithreading.
This application demonstrates client–server architecture where multiple clients can chat with each other in real time.

🚀 Features
✅ Multi-client support using threads
✅ Real-time broadcast messaging
✅ Private messaging using /msg <username> <message>
✅ Join/Leave notifications
✅ Graceful exit with exit command
✅ Chat logs stored in chatlog.txt

🛠️ Tech Stack
Language: Java
Concepts: Socket Programming, Multithreading, OOPs
Persistence: Chat logs stored in text file

📂 Project Structure
├── ChatServer.java      # Main server class (handles connections & broadcasts)
├── ChatClient.java      # Client class (connects to server & sends messages)
├── ClientHandler.java   # Handles each client in a separate thread
├── chatlog.txt          # Chat history log

⚙️ How to Run
1. Compile the Java files
javac ChatServer.java ChatClient.java ClientHandler.java
2. Start the Server
java ChatServer

By default, it listens on port 1234 (you can change it inside ChatServer.java).
4. Start Clients
Open multiple terminals and run:
java ChatClient
🖥️ Sample Commands
Send a public message:
hello everyone!
Send a private message:
/msg <username> <message>

Exit the chat:
exit

📝 Sample Chat Log
Thu Aug 21 19:09:55 IST 2025 🟢 Tarun Singh joined the chat 👋
Thu Aug 21 19:11:08 IST 2025 🟢 Tushar joined the chat 👋
[Tarun Singh]: hey how are you?
[Tushar]: good, you?

🔮 Future Improvements

Add user authentication
Support for group chats / rooms
GUI-based client using JavaFX or Swing
Store logs in a database instead of text file

📖 Low-Level Design (LLD)

ChatServer → accepts connections, maintains list of clients, broadcasts messages
ClientHandler → runs in its own thread, listens & forwards messages

ChatClient → connects to server, reads user input, and displays incoming messages
