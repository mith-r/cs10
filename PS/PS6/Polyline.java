import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, updated Fall 2016
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2025
 * @author Mithun Rameshkumar
 * @author Ashok Vasan
 */

public class Polyline implements Shape {
	// TODO: YOUR CODE HERE
    private List<Segment> segments;
    private Color color;

    /**
     * Constructs a Polyline with the specified list of segments.
     * The color is taken from the first segment in the list.
     * @param segments the list of segments forming the polyline.
     */
    public Polyline(List<Segment> segments) {
        this.segments = segments;
		color = segments.get(0).getColor();
    }

    /**
     * Adds a segment to this polyline.
     * @param segment the segment to add.
     */
	public void addSegment(Segment segment){
		segments.add(segment);
	}

    /**
     * Returns the list of segments that make up this polyline.
     * @return the segments of the polyline.
     */
	public List<Segment> getSegments(){
		return segments;
	}

    /**
     * Moves the polyline by the specified offsets.
     * @param dx the amount to move in the x direction.
     * @param dy the amount to move in the y direction.
     */
	@Override
	public void moveBy(int dx, int dy) {
		for (Segment segment: segments){
			segment.moveBy(dx,dy);
		}
	}

    /**
     * Returns the color of the polyline.
     * @return the polyline's color.
     */
	@Override
	public Color getColor() {
		return color;
	}

    /**
     * Sets the color of the polyline and updates the color of all its segments.
     * @param color the new color for the polyline.
     */
	@Override
	public void setColor(Color color) {
		this.color = color;
		for (Segment segment: segments){
			segment.setColor(color);
		}
	}

    /**
     * Checks whether the polyline contains the specified point.
     * @param x the x-coordinate of the point.
     * @param y the y-coordinate of the point.
     * @return true if the point is contained within any segment, false otherwise.
     */
	@Override
	public boolean contains(int x, int y) {
		for (Segment segment: segments){
			if (segment.contains(x,y)){
				return true;
			}
		}

		return false;
	}

    /**
     * Draws the polyline by drawing each of its segments.
     * @param g the Graphics context to draw on.
     */
	@Override
	public void draw(Graphics g) {
		for (Segment segment: segments){
			segment.draw(g);
		}
	}

    /**
     * Returns a string representation of the polyline.
     * @return a string encoding the polyline.
     */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("freehand ;");
		for (Segment segment: segments){
			sb.append(segment.toString());
			sb.append(";");
		}
		return sb.toString();
	}
}
