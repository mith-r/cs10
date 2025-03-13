import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatServerCommunicator extends Thread {
    private Socket sock; // each instance is in a different thread and has its own socket
    private ChatServer server; // the main server instance
    private String name; // client's name (first interaction with server)
    private BufferedReader in; // from client
    private PrintWriter out; // to client
    //TODO: your code here
    boolean knowsPasscode;

    public ChatServerCommunicator(Socket sock, ChatServer server) {
        this.sock = sock;
        this.server = server;
        knowsPasscode = false;
    }

    public void kick() throws IOException {
        sock.close();
    }

    public void run() {
        try {
            System.out.println("someone connected");
            // Communication channel
            in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            out = new PrintWriter(sock.getOutputStream(), true);

            // Identify -- first message is the name
            name = in.readLine();
            System.out.println("it's " + name);
            out.println("welcome " + name);
            server.broadcast(this, name + " entered the room");
            server.clients.put(name, this);
            //TODO: your code here
            // Chat away
            String line;
            while ((line = in.readLine()) != null) {
                String[] splitLine = line.split(" ", 4);
                if (splitLine[0].equals("KICK")) {
                    if (knowsPasscode) {
                        server.removeCommunicator(server.clients.get(splitLine[1]));
                        server.broadcast(this, "KICKED " + splitLine[1]);
                        send("SUCCESSFULLY KICKED " + splitLine[1]);
                    } else {
                        send("NOT AUTHORIZED");
                    }
                } else if (splitLine[0].equals("RENAME")) {
                    server.clients.remove(name);
                    server.clients.put(splitLine[1], this);
                    name = splitLine[1];
                    send("RENAMED TO " + name);
                } else if (splitLine[0].equals("PASSWORD")) {
                    if (server.checkPasscode(Integer.parseInt(splitLine[1]))) {
                        knowsPasscode = true;
                        send("PASSCODE SUCCESS");
                    } else {
                        send("PASSCODE INCORRECT");
                    }
                } else if (splitLine[0].equals("PRIVATE") && splitLine[1].equals("MESSAGE")) {
                    server.clients.get(splitLine[2]).send(name + ": " + splitLine[3]);
                } else if (splitLine[0].equals("SET") && splitLine[1].equals("EMAIL")) {
                    if (splitLine[2].matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$”")) {
                        server.emails.put(name, splitLine[2]);
                        send("EMAIL SUCCESSFULLY SET");
                    } else {
                        send("CHECK EMAIL AND TRY AGAIN");
                    }
                }else if (splitLine[0].equals("GET") && splitLine[1].equals("EMAIL")) {
                    System.out.println(server.emails.get(splitLine[2]));
                } else {
                    server.broadcast(this, name + ": " + line);
                }
            }
            // Done
            System.out.println(name + " hung up");
            server.broadcast(this, name + " left the room");
            // Clean up -- note that also remove self from server's list of handlers so it doesn't broadcast here
            server.removeCommunicator(this);
            out.close();
            in.close();
            sock.close();
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends a message to the client
     *
     * @param
     */
    public void send(String msg) {
        out.println(msg);
    }
}