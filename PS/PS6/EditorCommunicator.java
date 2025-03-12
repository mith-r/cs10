import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Handles communication to/from the server for the editor
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Chris Bailey-Kellogg; overall structure substantially revised Winter 2014
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2025
 * @author Mithun Rameshkumar
 * @author Ashok Vasan
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out; BufferedReader in;	// to/from server
	protected Editor editor;		// handling communication for
	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			//Socket sock = new Socket(serverIP, 4242);
			Socket sock = new Socket();
			sock.connect(new InetSocketAddress(serverIP, 4242), 2000);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		} catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);}}
	/**
	 * Sends message to the server
	 */
	public void send(String msg) {out.println(msg);}
	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {// Continuously read and process incoming messages
            String line;
            while ((line = in.readLine()) != null){
                editor.interpretMessage(line);}} //pass received messages to server
		catch (IOException e) {e.printStackTrace();}
		finally {System.out.println("server hung up");}}} //notify when connection terminated
