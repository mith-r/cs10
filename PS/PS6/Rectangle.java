import java.awt.Color;
import java.awt.Graphics;

/**
 * A rectangle-shaped Shape
 * Defined by an upper-left corner (x1,y1) and a lower-right corner (x2,y2)
 * with x1<=x2 and y1<=y2
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, updated Fall 2016
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2025
 * @author Mithun Rameshkumar
 * @author Ashok Vasan
 */
public class Rectangle implements Shape {
	private int x1, y1, x2, y2;
	private Color color;

	/**
	 * Constructs an empty Rectangle with a single point.
	 * The rectangle is initialized with the given (x1, y1) as both the upper-left and lower-right corners.
	 * @param x1 the x-coordinate of the initial point.
	 * @param y1 the y-coordinate of the initial point.
	 * @param color the color of the rectangle.
	 */
	public Rectangle(int x1, int y1, Color color){
		this.x1 = x1; this.x2 = x1;
		this.y1 = y1; this.y2 = y2;
		this.color = color;
	}

	/**
	 * Constructs a Rectangle defined by two corners.
	 * @param x1 the x-coordinate of the upper-left corner.
	 * @param y1 the y-coordinate of the upper-left corner.
	 * @param x2 the x-coordinate of the lower-right corner.
	 * @param y2 the y-coordinate of the lower-right corner.
	 * @param color the color of the rectangle.
	 */
	public Rectangle(int x1, int y1, int x2, int y2, Color color){
		setCorners(x1,y1,x2,y2);
		this.color = color;
	}

	/**
	 * Redefines the rectangle's corners to ensure proper ordering.
	 * @param x1 the x-coordinate of the first point.
	 * @param y1 the y-coordinate of the first point.
	 * @param x2 the x-coordinate of the second point.
	 * @param y2 the y-coordinate of the second point.
	 */
	public void setCorners(int x1, int y1, int x2, int y2){
		this.x1 = Math.min(x1, x2);
		this.y1 = Math.min(y1, y2);
		this.x2 = Math.max(x1, x2);
		this.y2 = Math.max(y1, y2);
	}

	/**
	 * Moves the rectangle by the specified offsets.
	 * @param dx the change in the x-coordinate.
	 * @param dy the change in the y-coordinate.
	 */
	@Override
	public void moveBy(int dx, int dy) {
		x1 += dx; y1 += dy;
		x2 += dx; y2 += dy;
	}

	/**
	 * Returns the color of the rectangle.
	 * @return the rectangle's color.
	 */
	@Override
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the rectangle's color.
	 * @param color the new color for the rectangle.
	 */
	@Override
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Checks if the rectangle contains the specified point.
	 * @param x the x-coordinate of the point.
	 * @param y the y-coordinate of the point.
	 * @return true if the point lies within the rectangle, false otherwise.
	 */
	@Override
	public boolean contains(int x, int y) {
		return x >= x1 && x <= x2 && y >= y1 && y <= y2;
	}

	/**
	 * Draws the rectangle using the provided Graphics context.
	 * @param g the Graphics context to use for drawing.
	 */
	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x1, y1, x2-x1, y2-y1);
	}

	/**
	 * Returns a string representation of the rectangle for communication.
	 * @return a string encoding the rectangle's properties.
	 */
	public String toString() {
		return "rectangle "+x1+" "+y1+" "+x2+" "+y2+" "+color.getRGB();
	}
}
