import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChatServer {
    private ServerSocket listen; // for accepting connections
    private ArrayList<ChatServerCommunicator> comms; // all the connections with clients
    public Map<String, ChatServerCommunicator> clients;
    public Map<String, String> emails;
    private int passcode;
    //TODO: your code here


    public ChatServer(ServerSocket listen) {
        this.listen = listen;
        comms = new ArrayList<ChatServerCommunicator>();
        clients = new HashMap<>();
        passcode = 1787;
//TODO: your code here
    }
    /**
     * The usual loop of accepting connections and firing off new threads to handle them
     */
    public void getConnections() throws IOException {
        while (true) {
            //listen.accept in next line blocks until new connection
            ChatServerCommunicator comm = new ChatServerCommunicator(listen.accept(), this);
            comm.setDaemon(true);
            comm.start();
            addCommunicator(comm);
        }
    }
    /**
     * Adds the handler to the list of current client handlers
     */
    public synchronized void addCommunicator(ChatServerCommunicator comm) {
        comms.add(comm);
    }

    /**
     * Removes the handler from the list of current client handlers
     */
    public synchronized void removeCommunicator(ChatServerCommunicator comm) throws IOException {
        comm.kick();
        comms.remove(comm);
    }
    /**
     * Sends the message from the one client handler to all the others (but not to the originator)
     */
    public synchronized void broadcast(ChatServerCommunicator from, String msg) {
        for (ChatServerCommunicator comm: comms){
            if (comm != from) {
                comm.send(msg);
            }

        }
    }

    public boolean checkPasscode(int passcode){
        if (this.passcode == passcode){
            return true;
        }
        return false;
    }


    public static void main(String[] args) throws Exception {
        System.out.println("waiting for connections");
        new ChatServer(new ServerSocket(4245)).getConnections();
    }
}
