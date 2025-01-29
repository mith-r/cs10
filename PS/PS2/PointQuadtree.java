import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * A point quadtree: stores an element at a 2D position, with children at the subdivided quadrants
 * E extends Point2D to ensure whatever the PointQuadTree holds implements getX and getY
 *
 * @author Ashok Vasan
 * @author Mithun Rameshkumar
 *
 *
 */
public class PointQuadtree<E extends Point2D> {
	private E point;                            // the point anchoring this node
	private int x1, y1;                            // upper-left corner of the region
	private int x2, y2;                            // bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;    // children

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}

	// Getters
	public E getPoint() {
		return point;
	}

	public int getX1() {
		return x1;
	}

	public int getY1() {
		return y1;
	}

	public int getX2() {
		return x2;
	}

	public int getY2() {
		return y2;
	}

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 *
	 * @param quadrant 1 through 4
	 * @return child for quadrant
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant == 1) return c1;
		if (quadrant == 2) return c2;
		if (quadrant == 3) return c3;
		if (quadrant == 4) return c4;
		return null;
	}

	/**
	 * Returns whether there is a child at the given quadrant, 1-4
	 *
	 * @param quadrant 1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant == 1 && c1 != null) || (quadrant == 2 && c2 != null) || (quadrant == 3 && c3 != null) || (quadrant == 4 && c4 != null);
	}

	/**
	 * Inserts the point into the tree
	 */
	public void insert(E p2) {
		//Check if point p2 is in quadrant 1
		if (p2.getX() >= point.getX() && p2.getY() <= point.getY()) {
			//If child 1 exists, recursively insert into it
			if (c1 != null) { //if child 1 exists
				c1.insert(p2); //then recursively insert p in child 1
			} else {
				//If no child 1, create a new tree for this quadrant
				c1 = new PointQuadtree<E>(p2, (int) point.getX(), y1, x2, (int) point.getY());
			}
		}
		//And similarly with the other quadrants / children
		else if (p2.getX() <= point.getX() && p2.getY() <= point.getY()) { //checking quadrant 2
			if (c2 != null) {
				c2.insert(p2);
			} else {
				c2 = new PointQuadtree<E>(p2, x1, y1, (int) point.getX(), (int) point.getY());
			}
		}

		else if (p2.getX() <= point.getX() && p2.getY() >= point.getY()) { //checking quadrant 3
			if (c3 != null) {
				c3.insert(p2);
			} else {
				c3 = new PointQuadtree<E>(p2, x1, (int) point.getY(), (int) point.getX(), y2);
			}
		}

		else if (p2.getX() >= point.getX() && p2.getY() >= point.getY()) { //checking quadrant 4
			if (c4 != null) {
				c4.insert(p2);
			} else {
				c4 = new PointQuadtree<E>(p2, (int) point.getX(), (int) point.getY(), x2, y2);
			}
		}
	}

	public String toString() {
		return toStringHelper(this, "", "");
	}

	/**
	 * Recursively constructs a String representation of the tree from this node,
	 * starting with the given indentation and indenting further going down the tree
	 */
	public String toStringHelper(PointQuadtree node, String indent, String childIndent) {
		String res = indent + node.point + "\n";
		if (node.c1 != null) res += toStringHelper(node.c1, childIndent + "├── ", childIndent + "│   ");
		if (node.c2 != null) res += toStringHelper(node.c2, childIndent + "├── ", childIndent + "│   ");
		if (node.c3 != null) res += toStringHelper(node.c3, childIndent + "├── ", childIndent + "│   ");
		if (node.c4 != null) res += toStringHelper(node.c4, childIndent + "└── ", childIndent + "    ");
		return res;
	}

	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		int num = 1; //adds current point

		//Check each child and add their size if it exists
		if (c1 != null) {
			num += c1.size();
		}
		//repeat for all children
		if (c2 != null) {
			num += c2.size();
		}

		if (c3 != null) {
			num += c3.size();
		}

		if (c4 != null) {
			num += c4.size();
		}

		return num;
	}


	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 *
	 * @return List with all points in the quadtree
	 */
	public List<E> allPoints() {
		List<E> points = new ArrayList<>(); // initializes list
		addToAllPoints(points);//adds current point to list
		return points;
	}

	/**
	 * Helper function for allPoints (accumulator)
	 * @param points list of all points
	 */

	public void addToAllPoints(List<E> points){
		points.add(this.point); //adds current point to list

		//recurse through non-null children and add their points
		if (c1 != null) {
			c1.addToAllPoints(points);
		}
		if (c2 != null) {
			c2.addToAllPoints(points);
		}
		if (c3 != null) {
			c3.addToAllPoints(points);
		}
		if (c4 != null) {
			c4.addToAllPoints(points);
		}
	}

	/**
	 * Uses the quadtree to find all points within the circle
	 *
	 * @param cx circle center x
	 * @param cy circle center y
	 * @param cr circle radius
	 * @return the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		List<E> inCircle = new ArrayList<E>(); //initializes list
		addToInCircle(cx, cy, cr, inCircle);//storing points
		return inCircle;
	}

	/**
	 * Helper function for findInCircle (accumulator)
	 *
	 * @param cx circle center x
	 * @param cy circle center y
	 * @param cr circle radius
	 * @param inCircle list of points in circle

	 */
	private void addToInCircle(double cx, double cy, double cr, List<E> inCircle) {
		//Check if circle intersects quadrant's rectangle
		if (Geometry.circleIntersectsRectangle(cx, cy, cr, x1, y1, x2, y2)) {
			//If the point is inside the circle, add it to the list
			if (Geometry.pointInCircle(point.getX(), point.getY(), cx, cy, cr)) {
				inCircle.add(point);
			}
			//Recurse and repeat with non-null children.
			if (c1 != null) {
				c1.addToInCircle(cx, cy, cr,inCircle);
			}
			//repeat with each child quadrant
			if (c2 != null) {
				c2.addToInCircle(cx, cy, cr,inCircle);
			}
			if (c3 != null) {
				c3.addToInCircle(cx, cy, cr, inCircle);
			}
			if (c4 != null) {
				c4.addToInCircle(cx, cy, cr,inCircle);;
			}
		}
	}
}
