import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Code provided for PS-1
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Dartmouth CS 10, Winter 2025
 *
 * @author Tim Pierson, Dartmouth CS10, Winter 2025, based on prior terms RegionFinder
 * @author Mithun Rameshkumar
 * @author Ashok Vasan
 */
public class RegionFinder {
    private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
    private static final int minRegion = 50; 				// how many points in a region to be worth considering

    private BufferedImage image;                            // the image in which to find regions
    private BufferedImage recoloredImage;                   // the image with identified regions recolored
    private Color targetColor;                              // stores targetColor as global for use in Color recolor

    private ArrayList<ArrayList<Point>> regions = new ArrayList<>();			// a region is a list of points
    // so the identified regions are in a list of lists of points

    public RegionFinder() {
        this.image = null;
    }

    public RegionFinder(BufferedImage image) {
        this.image = image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getRecoloredImage() {
        return recoloredImage;
    }

    /**
     * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
     */
    public void findRegions(Color targetColor) {
        // TODO: YOUR CODE HERE
        BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB); //creates new image
        this.targetColor = targetColor; //sets target color

        //loop through every pixel on image:
        for (int x = 0; x <image.getWidth();x++){
            for (int y= 0; y< image.getHeight();y++){

                Color startingColor = new Color(image.getRGB(x,y)); //color of current pixel in image
                ArrayList<Point> currentRegion = new ArrayList<>(); //creates new ArrayList

                if (visited.getRGB(x,y) != 1 && colorMatch(targetColor,startingColor) ){ //if not visited and similar color:

                    ArrayList<Point> toVisit = new ArrayList<>(); //creates new ArrayList of neighboring pixels to visit
                    toVisit.add(new Point(x, y)); //add current pixel to toVisit

                    while (!toVisit.isEmpty()){ //As long as there is some pixel that needs to be visited
                        Point current = toVisit.remove(0); //remove and store the pixel currently being visited
                        currentRegion.add(current); //Add this to the arrayList of current Region

                        int cx = current.x; //current x value
                        int cy = current.y; // current y value

                        for (int ix = -1; ix<=1; ix++){ //loop over all neighbors
                            for(int iy = -1; iy<=1; iy++){

                                int nx = cx+ix; //x value of neighbor
                                int ny = cy +iy; //y value of neighbor

                                if (nx < image.getWidth() && nx >= 0 && ny < image.getHeight() && ny >= 0 && visited.getRGB(nx, ny) != 1){ // if neighbor is in range and not visited

                                    Color adjacentPixel = new Color(image.getRGB(nx,ny)); //Color of the neighbor pixel

                                    if (colorMatch(targetColor, adjacentPixel) ){ //if neighbor is of correct color

                                        toVisit.add(new Point(nx,ny)); //add neighbor to toVisit array
                                        visited.setRGB(nx,ny,1); //set neighbor as being visited
                                    }
                                }
                            }
                        }
                    }
                }

                if (currentRegion.size() > minRegion) { //if current region is larger than minimum region size (50)
                    regions.add(new ArrayList<>(currentRegion)); //add to arrayList of arraylists
                }
            }
        }
    }

    /**
     * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
     */
    protected static boolean colorMatch(Color c1, Color c2) {
        // TODO: YOUR CODE HERE

        boolean redDifference = Math.abs(c1.getRed() - c2.getRed()) < maxColorDiff; //checks if red values are similar
        boolean blueDifference = Math.abs(c1.getBlue() - c2.getBlue()) < maxColorDiff; //checks if blue values are similar
        boolean greenDifference = Math.abs(c1.getGreen() - c2.getGreen()) < maxColorDiff; //checks if green values are similar
        return redDifference && blueDifference && greenDifference; //if all similar, return true; else, return false
    }

    /**
     * Returns the largest region detected (if any region has been detected)
     */
    public ArrayList<Point> largestRegion() {
        // TODO: YOUR CODE HERE
        int max = 0; //stores size of largest region so far
        ArrayList<Point> largest = null; //initiates ArrayList for largest region to null

        for (ArrayList<Point> region: regions){ //loops through all regions in ArrayList of ArrayLists
            if (region.size()>max){ //if size of region is larger than the size of the largest region so far

                max = region.size(); //set size of largest region so far to current region size
                largest = new ArrayList<>(region); //sets ArrayList largest to current region
            }
        }
        return largest; //returns largest region
    }

    /**
     * Sets recoloredImage to be a copy of image,
     * but with each region a uniform random color,
     * so we can see where they are
     */
    public void recolorImage() {
        // First copy the original
        recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
        // Now recolor the regions in it

        int red = targetColor.getRed(); //gets red value of targetColor
        int green = targetColor.getGreen(); //gets green value of targetColor
        int blue = targetColor.getBlue(); //gets blue value of target color

        //finds which hue is most prominent and amplifies for recolored image
        if (red > green && red> blue){
            red = 255;
        } else if( green > red && green > blue){
            green = 255;
        } else{
            blue = 255;
        }
        Color recolor = new Color(red,green,blue); //sets color to amplified hue

        for (ArrayList<Point> region: regions) { //loops through arraylist of arraylists
            for (Point point : region) { //loops through arraylist
                recoloredImage.setRGB(point.x, point.y, recolor.getRGB()); //sets RGB value to be recolored
            }
        }
    }
}
