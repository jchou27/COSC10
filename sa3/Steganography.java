import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Short Assignment 3 (SA-3) provided code
 * Use steganography to hide a message in an image by subtly altering the red component of a pixel.
 * Complete hideMessage and getMessage methods.
 *
 * @author Jack Chou
 * @source Tim Pierson
 */
public class Steganography {
    private Character STOP_CHARACTER = '*';
    private int BITS_PER_CHARACTER = 7;
    private int MAX_COLOR = 255;

    /**
     * Hides a message in an image.  First add a stop character to the end of the message so when a message is recovered
     * the recovery operations knows it is at the end of the message when it encounters the stop character.
     * Convert each character in message to "1" and "0" bit.
     * For each bit, alter the red component so that "0" bits result in an even red color, while "1" bits result in an odd
     * red color.  Use Integer.toBinaryString(c) to create a String of "1" and "0" bits from Character c.
     * @param original - BufferedImage holding the image to hide the message in
     * @param message - String message to hide
     * @return - altered BufferedImage result where pixels are even if bit is "0" and odd if bit is "1"
     * @throws Exception for file not found
     */
    public BufferedImage hideMessage(BufferedImage original, String message) throws Exception {
        int x = 0, y = 0;
        message += STOP_CHARACTER;

        //make copy of original image so we don't alter the original image
        BufferedImage result = new BufferedImage(original.getColorModel(), original.copyData(null), original.getColorModel().isAlphaPremultiplied(), null);

        //TODO: Your code here
        /**
         * Set exception if length of message is greater than the total amount of pixels available in the photo
         */
        int totalPixel = result.getHeight() * result.getWidth();
        if (message.length() * BITS_PER_CHARACTER >= totalPixel){
            throw new Exception("Error: there are not enough pixels in the image to store the entire message");
        }
        /**
         * Iterate through the message length and convert the character to bits
         */
        for(int i = 0; i < message.length(); i++){
            Character c = message.charAt(i);
            String bits = Integer.toBinaryString(c);

            if(bits.length() == 6){
                bits = "0" + bits;
            }

            /**
             * Iterate through every character in the bits, get the red color and +1 to the red color bit.
             * If at Max_color - 1 since we cannot have a value greater than 255.
             * Update the pixel to the new red
             * Increment by x
             */
            for(int j = 0; j < bits.length(); j++){

                Character b = bits.charAt(j);
                Color colorPixel = new Color(result.getRGB(x, y));
                int red = colorPixel.getRed();
                int green = colorPixel.getGreen();
                int blue = colorPixel.getBlue();
                if(b == '0' && red % 2 != 0){
                    if (red == MAX_COLOR){
                        red -= 1;
                    }
                    else {
                        red += 1;
                    }

                }
                else if (b == '1' && colorPixel.getRed() % 2 == 0) {
                    red += 1;

                }
                Color newColorPixel = new Color(red, green, blue);
                result.setRGB(x, y, newColorPixel.getRGB());

                x++;

                if (x > result.getWidth() - 1) {
                    y++;
                    x = 0;
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
        //TODO: Your code here
        String message = "";
        String temp = "";
        int x = 0, y = 0;
        Character lastCharacter = null;
        /**
         * While loop until we reach '*' character
         *  Get the red color and check if even or odd and assign it bit 0 or 1
         *  Check if the length of temp bits is at 7. If at 7 then convert to alphabet in the form of a character
         *  if '*' then stop while loop, otherwise add it to the message
         *  reset the temp bits to an empty string and increment x by 1
         */
        while(lastCharacter != STOP_CHARACTER){
            Color color = new Color(img.getRGB(x, y));
            int red = color.getRed();
            if (red % 2 == 0){
                temp += "0";

            }
            else{
                temp += "1";

            }


            if(temp.length() == BITS_PER_CHARACTER){
                lastCharacter = (char)Integer.parseInt(temp, 2);
                if (lastCharacter == STOP_CHARACTER){
                    break;
                }
                else{
                    message += lastCharacter;
                }
                temp = "";

            }
            x++;
            if (x >= img.getWidth()) {
                x = 0;
                y++;

            }

        }
        return message;
    }



    public static void main(String[] args) throws Exception {
        String originalImageFileName = "pictures/baker.jpg";
        String hiddenImageFileName = "pictures/hidden.png"; //do not use lossy jpg format, corrupts message, use png
        String message = "Jack Chou";

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
