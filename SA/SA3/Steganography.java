import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Short Assignment 3 (SA-3) provided code
 * Use steganography to hide a message in an image by subtly altering the red component of a pixel.
 * Complete hideMessage and getMessage methods.
 *
 * @author Tim Pierson, Dartmouth CS10, Winter 2025
 * @author Mithun Ramehskumar, Dartmouth CS10, Winter 2025;
 */
public class Steganography {
    private Character STOP_CHARACTER = '*'; //this character indicates the end of the message
    private int BITS_PER_CHARACTER = 7; //each character in a message is this many bits long
    private int MAX_COLOR = 255; //max pixel red, green, or blue color value

    /**
     * Hides a message in an image.  First add a stop character to the end of the message so when a message is recovered
     * the recovery operations knows it is at the end of the message when it encounters the stop character.
     * Convert each character in message to "1" and "0" bit.
     * For each bit, alter the red component so that "0" bits result in an even red color, while "1" bits result in an odd
     * red color.  Use Integer.toBinaryString(c) to create a String of "1" and "0" bits from Character c.
     * @param original - BufferedImage holding the image to hide the message in
     * @param message - String message to hide
     * @return - altered BufferedImage result where pixels are even if bit is "0" and odd if bit is "1"
     */
    public BufferedImage hideMessage(BufferedImage original, String message) {
        int x = 0, y = 0;
        message += STOP_CHARACTER;

        //make copy of original image so we don't alter the original image
        BufferedImage result = new BufferedImage(original.getColorModel(), original.copyData(null), original.getColorModel().isAlphaPremultiplied(), null);

        //TODO: Your code here
        int size = message.length() * BITS_PER_CHARACTER;
        int pixelCount = original.getHeight() * original.getWidth();

        if (size>pixelCount) {
            System.out.println("ERROR: INSUFFICIENT PIXELS TO ENCODE IMAGE");
            return null;
        }

        for (int i = 0; i<message.length(); i++){
            char c = message.charAt(i);

            String bits = Integer.toBinaryString(c);
            while (bits.length() <= 6) {
                bits = "0" + bits;
            }

            for (int j = 0; j<bits.length(); j++) {
                char bit = bits.charAt(j);
                Color color = new Color(original.getRGB(x, y));
                Color newColor = color;

                if (bit == '0' && color.getRed() % 2 != 0) {
                    if (color.getRed() == MAX_COLOR) {
                        newColor = new Color(color.getRed() - 1, color.getGreen(), color.getBlue());
                    } else {
                        newColor = new Color(color.getRed() + 1, color.getGreen(), color.getBlue());
                    }
                } else if (bit == '1' && color.getRed() % 2 == 0) {
                     newColor = new Color(color.getRed() + 1, color.getGreen(), color.getBlue());
                }
                result.setRGB(x,y,newColor.getRGB());
                x++;
                if (x>=original.getWidth()){
                    y++;
                    x=0;
                }
            }
        }

        return result;
    }


    /**
     * Recover message hidden in image.  Loop until stop character is encountered.
     * @param img - BufferedImage with hidden message
     * @return String with recovered message
     */
    public String getMessage(BufferedImage img) {
        String message = "";
        String temp = "";
        int x = 0, y = 0;

        while (true){
            for (int i = 0; i < 7; i++){
                Color color = new Color(img.getRGB(x,y));
                int red = color.getRed();
                if (red % 2 == 0) {
                    temp += '0';
                } else{
                    temp += '1';
                }
                x++;
                if (x>=img.getWidth()){
                    y++;
                    x=0;
                }
            }
            char lastCharacter = (char) Integer.parseInt(temp, 2);
            if (lastCharacter == STOP_CHARACTER){
                break;
            }
            message += lastCharacter;
            temp = "";

        }

        return message;
    }



    public static void main(String[] args) {
        String originalImageFileName = "pictures/pierson.png";
        String hiddenImageFileName = "pictures/hidden.png"; //do not use lossy jpg format, corrupts message, use png
        //String message = "Great job on this problem!  Good luck with the rest of the term. Now to make the message really crazy long so the message will have to go beyond a single line of pixels in the image.  This should be about enough. If the picture is only 800 pixels wide, about 115 characters would do the trick";
        String message = "Mithun Rameshkumar";

        //hide message in image
        System.out.println("Hiding message: " + message);
        BufferedImage image = ImageIOLibrary.loadImage(originalImageFileName);
        Steganography s = new Steganography();
        BufferedImage hiddenMessageImage = s.hideMessage(image, message);
        ImageGUI gui = new ImageGUI("SA-3  Can you tell the difference between images?",image, hiddenMessageImage);

        //save image with hidden message to disk
        ImageIOLibrary.saveImage(hiddenMessageImage, hiddenImageFileName,"png");

        //read image from disk and retrieve message from image
        BufferedImage img = ImageIOLibrary.loadImage(hiddenImageFileName);
        String recoveredMessage = s.getMessage(hiddenMessageImage);
        System.out.println("Recovered message: " + recoveredMessage);

    }
}
