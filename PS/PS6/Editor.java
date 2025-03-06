import org.opencv.core.Rect;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

/**
 * Client-server graphical editor
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; loosely based on CS 5 code by Tom Cormen
 * @author CBK, winter 2014, overall structure substantially revised
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 * @author CBK, spring 2016 and Fall 2016, restructured Shape and some of the GUI
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2025
 * @author Mithun Rameshkumar
 * @author Ashok Vasan
 */

public class Editor extends JFrame {
	private static String serverIP = "localhost";			// IP address of sketch server
	// "localhost" for your own machine;
	// or ask a friend for their IP address

	private static final int width = 800, height = 800;		// canvas size

	// Current settings on GUI
	public enum Mode {
		DRAW, MOVE, RECOLOR, DELETE
	}
	private Mode mode = Mode.DRAW;				// drawing/moving/recoloring/deleting objects
	private String shapeType = "ellipse";		// type of object to add
	private Color color = Color.black;			// current drawing color

	// Drawing state
	// these are remnants of my implementation; take them as possible suggestions or ignore them
	private Shape curr = null;					// current shape (if any) being drawn
	private Sketch sketch;						// holds and handles all the completed objects
	private int movingId = -1;					// current shape id (if any; else -1) being moved
	private Point drawFrom = null;				// where the drawing started
	private Point moveFrom = null;				// where object is as it's being dragged
	private Point sendMoveFrom = null;          //Records initial point of move operation for server operation
	private Point moveTo = null;                //Stores current target position while moving
	private boolean alreadyMoved = false;       //ensures no duplicate moves

	// Communication
	/**
	 * Handles communication with the sketch server.
	 */
	private EditorCommunicator comm;			// communication with the sketch server

	public Editor() {
		super("Graphical Editor");

		sketch = new Sketch();

		//Connect to server
		comm = new EditorCommunicator(serverIP, this);
		// Set up communication with the sketch server.
		comm.start();
		// Begin server communication thread.


		JComponent canvas = setupCanvas();

		JComponent gui = setupGUI();


		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(canvas, BorderLayout.CENTER);
		cp.add(gui, BorderLayout.NORTH);

		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Creates a component to draw into
	 */
	private JComponent setupCanvas() {
		JComponent canvas = new JComponent() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				drawSketch(g);
			}
		};

		canvas.setPreferredSize(new Dimension(width, height));

		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				handlePress(event.getPoint());
			}

			public void mouseReleased(MouseEvent event) {
				handleRelease();
			}
		});

		canvas.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent event) {
				handleDrag(event.getPoint());
			}
		});

		return canvas;
	}

	/**
	 * Creates a panel with all the buttons
	 *
	 * @return a JPanel containing buttons for shape selection, color selection, and mode selection
	 */
	private JComponent setupGUI() {
		// Select type of shape
		String[] shapes = {"ellipse", "freehand", "rectangle", "segment"};
		JComboBox<String> shapeB = new JComboBox<String>(shapes);
		shapeB.addActionListener(e -> shapeType = (String)((JComboBox<String>)e.getSource()).getSelectedItem());

		// Select drawing/recoloring color
		// Following Oracle example
		JButton chooseColorB = new JButton("choose color");
		JColorChooser colorChooser = new JColorChooser();
		JLabel colorL = new JLabel();
		colorL.setBackground(Color.black);
		colorL.setOpaque(true);
		colorL.setBorder(BorderFactory.createLineBorder(Color.black));
		colorL.setPreferredSize(new Dimension(25, 25));
		JDialog colorDialog = JColorChooser.createDialog(chooseColorB,
				"Pick a Color",
				true,  //modal
				colorChooser,
				e -> { color = colorChooser.getColor(); colorL.setBackground(color); },  // OK button
				null); // no CANCEL button handler
		chooseColorB.addActionListener(e -> colorDialog.setVisible(true));

		// Mode: draw, move, recolor, or delete
		JRadioButton drawB = new JRadioButton("draw");
		drawB.addActionListener(e -> mode = Mode.DRAW);
		drawB.setSelected(true);
		JRadioButton moveB = new JRadioButton("move");
		moveB.addActionListener(e -> mode = Mode.MOVE);
		JRadioButton recolorB = new JRadioButton("recolor");
		recolorB.addActionListener(e -> mode = Mode.RECOLOR);
		JRadioButton deleteB = new JRadioButton("delete");
		deleteB.addActionListener(e -> mode = Mode.DELETE);
		ButtonGroup modes = new ButtonGroup(); // make them act as radios -- only one selected
		modes.add(drawB);
		modes.add(moveB);
		modes.add(recolorB);
		modes.add(deleteB);
		JPanel modesP = new JPanel(new GridLayout(1, 0)); // group them on the GUI
		modesP.add(drawB);
		modesP.add(moveB);
		modesP.add(recolorB);
		modesP.add(deleteB);

		// Put all the stuff into a panel
		JComponent gui = new JPanel();
		gui.setLayout(new FlowLayout());
		gui.add(shapeB);
		gui.add(chooseColorB);
		gui.add(colorL);
		gui.add(modesP);
		return gui;
	}

	/**
	 * Getter for the sketch instance variable
	 *
	 * @return the current sketch instance
	 */
	public Sketch getSketch() {
		return sketch;
	}

	/**
	 * Renders all shapes in the sketch onto the canvas.
	 * Also draws any shape currently being created.
	 *
	 * @param g the Graphics context to draw on
	 */
	public void drawSketch(Graphics g) {
		// TODO: YOUR CODE HERE
		// Draw all completed shapes from the sketch.
		for (Shape shape: sketch.getShape().values()){
			shape.draw(g);
		}
		// Draw the shape currently being drawn.
		if (curr != null){
			curr.draw(g);
		}
	}

	// Helpers for event handlers

	/**
	 * Helper method for press at point
	 * In drawing mode, start a new object;
	 * in moving mode, (request to) start dragging if clicked in a shape;
	 * in recoloring mode, (request to) change clicked shape's color;
	 * in deleting mode, (request to) delete clicked shape.
	 *
	 * @param p the point where the mouse was pressed
	 */
	private void handlePress(Point p) {
		// Handles mouse press events to initiate drawing, moving, recoloring, or deleting shapes.
		// Determine if a shape exists at the press location.
		movingId = click(p);

		if (mode == Mode.DRAW){
			// In draw mode, create a new shape based on the selected type starting at the press point.
			if (shapeType.equals("freehand")){
				List<Segment> segmentList = new ArrayList<>();
				segmentList.add(new Segment(p.x, p.y, color));
				curr = new Polyline(segmentList);
			} else if (shapeType.equals("segment")) {
				curr = new Segment(p.x,p.y,color);
			} else if (shapeType.equals("ellipse")) {
				curr = new Ellipse(p.x,p.y, color);
			} else if (shapeType.equals("rectangle")) {
				curr = new Rectangle(p.x, p.y, color);
			}

			drawFrom = p;
		} else if (mode == Mode.MOVE){
			// In move mode, record the starting point for moving the shape.
			if (movingId != -1) {
				moveFrom = p;
				sendMoveFrom = p;
			}
		} else if (mode == Mode.DELETE){
			// In delete mode, remove the selected shape.
			if (movingId != -1){
				sketch.removeShape(movingId);
			}
		}
		repaint();
	}

	/**
	 * Returns the highest ID of a shape at the given point.
	 *
	 * @param p the point to check for shapes
	 * @return the highest shape ID at the point, or -1 if no shape is found
	 */
	private int click(Point p){
		int highestID = -1;
		for (int num: sketch.getShape().keySet()){
			if (sketch.getShape().get(num).contains(p.x, p.y) && num>highestID){
				highestID = num;
			}
		}
		return highestID;
	}

	/**
	 * Handles mouse dragging behavior.
	 * - In DRAW mode, updates the dimensions of the current shape.
	 * - In MOVE mode, moves the selected shape.
	 *
	 * @param p the current point of the drag event
	 */
	private void handleDrag(Point p) {
		// Handles mouse drag events to update drawing or moving of shapes.
		if (mode == Mode.DRAW){
			// For freehand drawing, add new segments and connect them.
			if (shapeType.equals("freehand")){
				((Polyline)curr).addSegment(new Segment(p.x, p.y, color));
				for (int i = 1; i < ((Polyline)curr).getSegments().size(); i ++){
					Segment current = ((Polyline)curr).getSegments().get(i);
					Segment previous = ((Polyline)curr).getSegments().get(i-1);

					current.setStart(previous.getX2(), previous.getY2());
				}
			} else if (shapeType.equals("segment")){
				// For segment drawing, update the start and end points of the segment.
				((Segment)curr).setStart(drawFrom.x,drawFrom.y);
				((Segment)curr).setEnd(p.x, p.y);
			}
			else {
				// Update shape dimensions based on drag from the initial point.
				if (shapeType.equals("ellipse")) ((Ellipse)curr).setCorners(drawFrom.x, drawFrom.y, p.x, p.y);
				if (shapeType.equals("rectangle")) ((Rectangle)curr).setCorners(drawFrom.x, drawFrom.y, p.x, p.y);
			}

		} else if (mode == Mode.MOVE){
			// For moving mode, update the selected shape's position based on drag movement.
			if (movingId != -1){
				sketch.getShape(movingId).moveBy(p.x-moveFrom.x, p.y - moveFrom.y);
				alreadyMoved = true;
				moveTo = p;
				moveFrom = p;
			}
		}
		repaint();
	}

	/**
	 * Handles mouse release events.
	 * - In DRAW mode, finalizes the new shape and sends it to the server.
	 * - In MOVE mode, finalizes the movement and sends the update to the server.
	 * - In RECOLOR mode, sends a recolor command to the server.
	 * - In DELETE mode, sends a delete command to the server.
	 */
	private void handleRelease() {
		// Finalizes drawing, moving, recoloring, or deleting actions and sends corresponding commands to the server.
		if (mode == Mode.DRAW){
			// Send the draw command for the newly created shape to the server.
			comm.send("draw " + curr.toString());
		}

		if (mode == Mode.MOVE){
			// Send the move command with updated coordinates to the server.
			comm.send ("move " + movingId + " " + moveTo.x + " " + sendMoveFrom.x + " " + moveTo.y + " " + sendMoveFrom.y);
		}

		if (mode == Mode.RECOLOR){
			// Send the recolor command to update the shape's color on the server.
			comm.send("recolor " + movingId + " " + color.getRGB());
		}

		if (mode == Mode.DELETE){
			// Send the delete command to remove the shape on the server.
			comm.send("delete " + movingId);
		}

		moveFrom = null;
		moveTo = null;
		sendMoveFrom = null;
		movingId = -1;
		drawFrom = null;
		curr = null;
		repaint();
	}

	/**
	 * Processes incoming messages from the sketch server and applies the corresponding actions.
	 * - "draw" messages add new shapes.
	 * - "move" messages update shape positions.
	 * - "recolor" messages change shape colors.
	 * - "delete" messages remove shapes from the sketch.
	 *
	 * @param message the message received from the server
	 */
	public void interpretMessage(String message) {
		// Processes incoming messages from the server to update the sketch.
		if (message.startsWith("draw freehand")) {
			// Parse the freehand drawing message to reconstruct the polyline.
			List<Segment> segmentList = new ArrayList<>();
			String[] line = message.split(";");
			for (int i = 1; i < line.length; i++) {
				String[] splitInfo = line[i].split(" ");
				segmentList.add(new Segment(Integer.parseInt(splitInfo[1]), Integer.parseInt(splitInfo[2]), Integer.parseInt(splitInfo[3]), Integer.parseInt(splitInfo[4]), new Color(Integer.parseInt(splitInfo[5]))));
			}

			// Connect segments to form a continuous freehand line.
			for (int i = 1; i < segmentList.size(); i++) {
				Segment current = segmentList.get(i);
				Segment previous = segmentList.get(i - 1);

				current.setStart(previous.getX2(), previous.getY2());
			}
			sketch.addShape(new Polyline(segmentList));
		}

		String[] info = message.split(" ");

		if (info[0].equals("draw")) {
			// Process standard draw commands for ellipse, rectangle, or segment shapes.
			if (info[1].equals("ellipse")) {
				sketch.addShape(new Ellipse(Integer.parseInt(info[2]), Integer.parseInt(info[3]), Integer.parseInt(info[4]), Integer.parseInt(info[5]), new Color(Integer.parseInt(info[6]))));
			} else if (info[1].equals("rectangle")) {
				sketch.addShape(new Rectangle(Integer.parseInt(info[2]), Integer.parseInt(info[3]), Integer.parseInt(info[4]), Integer.parseInt(info[5]), new Color(Integer.parseInt(info[6]))));
			} else if (info[1].equals("segment")) {
				sketch.addShape(new Segment(Integer.parseInt(info[2]), Integer.parseInt(info[3]), Integer.parseInt(info[4]), Integer.parseInt(info[5]), new Color(Integer.parseInt(info[6]))));
			}
		}

		if (info[0].equals("move")) {
			// Process move command to update a shape's position.
			if (!alreadyMoved) sketch.getShape(Integer.parseInt(info[1])).moveBy(Integer.parseInt(info[2]) - Integer.parseInt(info[3]),Integer.parseInt(info[4]) - Integer.parseInt(info[5]));
			alreadyMoved = false;
		}

		if (info[0].equals("recolor")) {
			// Process recolor command to update a shape's color.
			sketch.getShape(Integer.parseInt(info[1])).setColor(new Color(Integer.parseInt(info[2])));
		}

		if (info[0].equals("delete")){
			// Process delete command to remove a shape from the sketch.
			sketch.removeShape(Integer.parseInt(info[1]));
		}
		repaint();
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Editor();
			}
		});
	}
}
