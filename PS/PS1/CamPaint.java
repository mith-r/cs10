import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Code provided for PS-1
 * Webcam-based drawing
 * Dartmouth CS 10, Winter 2025
 *
 * @author Tim Pierson, Dartmouth CS10, Winter 2025 (based on CamPaint from previous terms)
 * @author Ashok Vasan
 * @author Mithun Rameshkumar
 */
public class CamPaint extends VideoGUI {
    private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
    private RegionFinder finder;			// handles the finding
    private Color targetColor;          	// color of regions of interest (set by mouse press)
    private Color paintColor = Color.red;	// the color to put into the painting from the "brush"
    private BufferedImage painting;			// the resulting masterpiece
    private ArrayList<Point> marked = new ArrayList<>(); //ArrayList of pixels that have been painted
    private ArrayList<Color> colorsToPaint = new ArrayList<>(); //ArrayList storing colors that were selected
    private boolean isPaused = false; //boolean to see if painting should be paused

    /**
     * Initializes the region finder and the drawing
     */
    public CamPaint() {
        finder = new RegionFinder(image);
        clearPainting();
    }

    /**
     * Resets the painting to a blank image
     */
    protected void clearPainting() {
        painting = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * VideoGUI method, here drawing one of live webcam, recolored image, or painting,
     * depending on display variable ('w', 'r', or 'p')
     */
    @Override
    public void handleImage() {

        finder = new RegionFinder(image); //creates object of RegionFinder class

        if (displayMode == 'w') { //if w is clicked
            setImage1(image); //show unaltered webcam
        }

        if (displayMode == 'r') { // if r is clicked

            if (targetColor == null) { // if targetColor not set
                setImage1(image); //shows unaltered webcam
            } else {
                finder.findRegions(targetColor); //finds regions similar in color to targetColor
                finder.recolorImage(); //recolors those regions
                painting = finder.getRecoloredImage(); //stores recolored image as painting

                setImage1(painting);//shows recolored image
            }
        }

        if (displayMode == 'p') {// if p is selected
            if (targetColor == null) {//if targetColor is not set
                setImage1(image);//shows unaltered webcam
            } else {

                clearPainting(); //sets background to white
                finder.findRegions(targetColor); //finds regions similar in color to targetColor

                ArrayList<Point> locationToPaint = finder.largestRegion(); //stores largest region as the paintbrush


                if (locationToPaint != null && !isPaused) { //if toPaint is not null
                    marked.addAll(locationToPaint); //add pixels that toPaint has crossed to marked
                    for (int i = 0; i < locationToPaint.size(); i++){
                        colorsToPaint.add(paintColor); //store the color selected
                    }
                }

                for (int i = 0; i < marked.size(); i++) { //for all points stored in marked
                    painting.setRGB(marked.get(i).x, marked.get(i).y, colorsToPaint.get(i).getRGB()); //paint that area with stored colors
                }

                setImage1(painting); //displays altered image
            }
        }
    }


    /**
     * Overrides the Webcam method to set the track color.
     */
    @Override
    public void handleMousePress(int x, int y) {
        // TODO: YOUR CODE HERE
        targetColor = new Color(image.getRGB(x,y)); //sets targetColor to color pixel of mouse press
    }

    /**
     * Webcam method, here doing various drawing commands
     */
    //@Override
    public void handleKeyPress(char k) {
        if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
            displayMode = k;
        }
        else if (k == '1' || k == '2' || k == '3' || k == '4' || k == '5' || k == '6' || k == '7' || k == '8'){
            changeColor(k);
        }
        else if (k == ' '){
            setPause();
        }

        else if (k == 'c') { // clears painted area
            marked = new ArrayList<>();
            colorsToPaint = new ArrayList<>();
        }
        else if (k == 'o') { // save the recolored image
            ImageIOLibrary.saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
        }
        else if (k == 's') { // save the painting
            ImageIOLibrary.saveImage(painting, "pictures/painting.png", "png");
        }
        else {
            System.out.println("unexpected key "+k);
        }
    }

    public void setPause(){
        if (isPaused == false){
            isPaused = true;
        } else{
            isPaused = false;
        }
    }
    public void changeColor(char k){
        if (k == '1'){
            paintColor = Color.red;
        }
        else if (k == '2'){
            paintColor = Color.orange;
        }
        else if (k == '3'){
            paintColor = Color.yellow;
        }
        else if (k == '4'){
            paintColor = Color.green;
        }
        else if (k == '5'){
            paintColor = Color.blue;
        }
        else if (k == '6'){
            Color purple = new Color(128,0,128);
            paintColor = purple;
        }
        else if (k == '7'){
            paintColor = Color.black;
        }
        else if (k == '8'){
            paintColor = Color.white;
        }
    }
    public static void main(String[] args) {
        new CamPaint();
    }
}
