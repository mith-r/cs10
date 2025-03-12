import java.awt.*;
import java.net.*;
import java.util.*;
import java.io.*;
import java.util.List;

/**
 * A server to handle sketches: getting requests from the clients,
 * updating the overall state, and passing them on to the clients
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2025
 * @author Mithun Rameshkumar
 * @author Ashok Vasan
 */
public class SketchServer {
	private ServerSocket listen;						// for accepting connections
	private ArrayList<SketchServerCommunicator> comms;	// all the connections with clients
	private Sketch sketch;								// the state of the world

	public SketchServer(ServerSocket listen) {
		this.listen = listen;
		sketch = new Sketch();
		comms = new ArrayList<SketchServerCommunicator>();}
	public Sketch getSketch() {
		return sketch;
	}
	/**
	 * The usual loop of accepting connections and firing off new threads to handle them
	 */
	public void getConnections() throws IOException {
		System.out.println("server ready for connections");
		while (true) {
			SketchServerCommunicator comm = new SketchServerCommunicator(listen.accept(), this);
			comm.setDaemon(true);
			comm.start();
			addCommunicator(comm);}}
	/**
	 * Adds the communicator to the list of current communicators
	 */
	public synchronized void addCommunicator(SketchServerCommunicator comm) {
		comms.add(comm);
	}
	/**
	 * Removes the communicator from the list of current communicators
	 */
	public synchronized void removeCommunicator(SketchServerCommunicator comm) {
		comms.remove(comm);
	}
	/**
	 * Sends the message from the one communicator to all (including the originator)
	 */
	public synchronized void broadcast(String message) {
		for (SketchServerCommunicator comm : comms) {
			comm.send(message);}}
    /**
     * Sends the latest message only to the most recently connected client.
     * This method targets the most recent communicator in the list to update them with the latest command.
     * @param message the message to be sent.
     */
    public synchronized void broadcastLatest(String message) {
        comms.get(comms.size() - 1).send(message);
    }
    /**
     * Processes an update message from a client and updates the server's sketch accordingly.
     * Supports various message types including freehand drawing, drawing shapes (ellipse, rectangle, segment),
     * moving, recoloring, and deleting shapes.
     * @param message the message containing the command and its parameters.
     */
    public synchronized void update(String message) {
        if (message.startsWith("draw freehand")) {
            List<Segment> segmentList = new ArrayList<>();
            String[] line = message.split(";");
            for (int i = 1; i < line.length; i++) {
                String[] splitInfo = line[i].split(" ");
                segmentList.add(new Segment(Integer.parseInt(splitInfo[1]), Integer.parseInt(splitInfo[2]), Integer.parseInt(splitInfo[3]), Integer.parseInt(splitInfo[4]), new Color(Integer.parseInt(splitInfo[5]))));
			}
            for (int i = 1; i < segmentList.size(); i++) {
                Segment current = segmentList.get(i);
                Segment previous = segmentList.get(i - 1);
                current.setStart(previous.getX2(), previous.getY2());}
            sketch.addShape(new Polyline(segmentList));
            return;}
        String[] info = message.split(" ");
        if (info[0].equals("draw")) {
            if (info[1].equals("ellipse")) {
                sketch.addShape(new Ellipse(Integer.parseInt(info[2]), Integer.parseInt(info[3]), Integer.parseInt(info[4]), Integer.parseInt(info[5]), new Color(Integer.parseInt(info[6]))));
            } else if (info[1].equals("rectangle")) {
                sketch.addShape(new Rectangle(Integer.parseInt(info[2]), Integer.parseInt(info[3]), Integer.parseInt(info[4]), Integer.parseInt(info[5]), new Color(Integer.parseInt(info[6]))));
            } else if (info[1].equals("segment")) {
                sketch.addShape(new Segment(Integer.parseInt(info[2]), Integer.parseInt(info[3]), Integer.parseInt(info[4]), Integer.parseInt(info[5]), new Color(Integer.parseInt(info[6]))));
            }
        }
        if (info[0].equals("move")) {
            sketch.getShape(Integer.parseInt(info[1])).moveBy(Integer.parseInt(info[2]) - Integer.parseInt(info[3]),Integer.parseInt(info[4]) - Integer.parseInt(info[5]));
        }
        if (info[0].equals("recolor")) {
            sketch.getShape(Integer.parseInt(info[1])).setColor(new Color(Integer.parseInt(info[2])));
        }
        if (info[0].equals("delete")){
            sketch.removeShape(Integer.parseInt(info[1]));
        }}

	public static void main(String[] args) throws Exception {
		new SketchServer(new ServerSocket(4242)).getConnections();}}

