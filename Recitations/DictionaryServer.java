import java.net.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple server that waits for someone to connect on port 4242,
 * and then repeatedly asks for their name and greets them.
 * Connect either by "telnet localhost 4242" or by running HelloClient.java
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Tim Pierson, Dartmouth CS 10, provided for Winter 2025
 */

public class DictionaryServer {
    public static void main(String[] args) throws IOException {
        // Listen on a server socket for a connection
        System.out.println("waiting for someone to connect");
        try {
            ServerSocket listen = new ServerSocket(4242);
            // When someone connects, create a specific socket for them
            Socket sock = listen.accept();
            System.out.println("someone connected");
            // Now talk with them
            PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            Map<String,String> dictionary = new HashMap<>();
            out.println("GET or SET?");
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("received:" + line);
                String[] splitLine = line.split(" ", 3);
                if (splitLine[0].equals("SET")){
                    if (splitLine[2] == null){
                        out.println("Didn't give a definition");
                    } else {
                        dictionary.put(splitLine[1], splitLine[2]);
                        out.println("Successfully Set");}
                } else if (splitLine[0].equals("GET")){
                    if (!dictionary.containsKey(splitLine[1])){
                        out.println("Word not in dictionary.");
                    } else {
                        out.println("Definition of " + splitLine[1] + " is " + dictionary.get(splitLine[1]));}}}
            System.out.println("client hung up");
            // Clean up shop
            out.close(); in.close();
            sock.close(); listen.close();}
        catch (Exception e) {System.out.println("Exception caught: " + e);}}}




















