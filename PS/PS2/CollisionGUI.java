import java.awt.*;
import java.awt.geom.Point2D;
import java.sql.Array;
import java.util.List;
import java.util.ArrayList;

/**
 * Using a quadtree for collision detection
 *
 * @author Mithun Rameshkumar
 * @author Ashok Vasan
 */
public class CollisionGUI extends InteractiveGUI {
	private static final int width=800, height=600;		// size of the window
	private List<MovingPoint> points;					// all the points
	private List<MovingPoint> colliders;				// the points that collided at this time
	private char collisionHandler = 'c';				// when there's a collision, 'color them, or 'destroy them
	private int delay = 100;							// timer control

	public CollisionGUI() {
		super("super-collider", width, height);
		points = new ArrayList<MovingPoint>();

		// Timer drives the animation.
		startTimer();
	}

	/**
	 * Adds a point at the (x,y) location
	 */
	private void add(int x, int y) {
		points.add(new MovingPoint(x,y,width,height));
	}

	/**
	 * InteractiveGUI method, here creating a new point
	 */
    @Override
	public void handleMousePress(int x, int y) {
		add(x,y);
		repaint();
	}

	/**
	 * InteractiveGUI method
	 */
    @Override
	public void handleKeyPress(char k) {
		if (k == 'f') { // faster
			if (delay>1) delay /= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 's') { // slower
			delay *= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 'r') { // add some new points at random positions
			for (int i=0; i<10; i++) {
				add((int)(width*Math.random()), (int)(height*Math.random()));
			}
			repaint();
		}
		else if (k == 'c' || k == 'd') { // control how collisions are handled
			collisionHandler = k;
			System.out.println("collision:"+k);
		}
	}

	/**
	 * InteractiveGUI method, here drawing all the points and then re-drawing the colliders in red
	 */
    @Override
	public void draw(Graphics g) {
		//Check if list of points not null before drawing
		if (points != null){
			//Set drawing color to black for points
			g.setColor(Color.black);

			//Iterate through list of points and draw each one
			for (int i = 0; i < points.size(); i++){
				points.get(i).draw(g);
			}
		}

		//check if list of colliders is not null before drawing
		if (colliders != null){
			//set drawing color to red for colliders
			g.setColor(Color.red);

			//Iterate through list of colliders and draw each one
			for (int i = 0; i < colliders.size(); i++){
				colliders.get(i).draw(g);
			}
		}
	}

	/**
	 * Sets colliders to include all points in contact with another point
	 */
	private void findColliders() {
		// Initialize an empty list to store colliders
		colliders = new ArrayList<>();

		// Initialize the quadtree to store moving points; starts as null
		PointQuadtree<MovingPoint> pointsTree = null;

		//Iterate through list of points starting from second point
		for (int i = 1; i < points.size(); i++) {

			//Initialize quadtree with first point and dimensions if not already done
			if (pointsTree == null){
				pointsTree = new PointQuadtree<>(points.get(0), 0, 0, width, height);
			}

			//insert current point into quadtree
			pointsTree.insert(points.get(i));

			//get current point and calculate diameter
			MovingPoint point = points.get(i);
			int diameter = point.getR() * 2;

			//find points colliding with current circle
			List<MovingPoint> collided = pointsTree.findInCircle(point.getX(), point.getY(), diameter);

			//if two or more points found, add to colliders list
			if (collided.size() >= 2) {
				colliders.addAll(collided);
			}
		}
	}



	/**
	 * InteractiveGUI method, here moving all the points and checking for collisions
	 */
    @Override
	public void handleTimer() {
		// Ask all the points to move themselves.
		for (MovingPoint point : points) {
			point.move();
		}
		// Check for collisions
		if (points.size() > 0) {
			findColliders();
			if (collisionHandler=='d') {
				points.removeAll(colliders);
				colliders = null;
			}
		}
		// Now update the drawing
		repaint();
	}

	public static void main(String[] args) {
        new CollisionGUI();
	}
}
