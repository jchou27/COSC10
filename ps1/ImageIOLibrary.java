import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Class to read and write images (png, jpg) on disk
 *    loadImage reads a file and stores image in a BufferedImage
 *    saveImage writes a BufferedImage back to disk.
 *
 * @author Tim Pierson, Dartmouth CS10, Winter 2024 (based on code from prior terms)
 */
public class ImageIOLibrary {
    /**
     * Load an image from disk at fileName into memory in a BufferedReader
     * @param fileName	for the image
     */
    public static BufferedImage loadImage(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File(fileName));
        }
        catch (Exception e) {
            System.err.println("Couldn't load image from `"+fileName+"' -- make sure the file exists in that folder");
            System.exit(-1);
        }
        return image;
    }

    /**
     * Save image to disk
     * @param image save this image
     * @param filename save at this file name
     * @param format use this image format (png, jpg)
     */
    public static void saveImage(BufferedImage image, String filename, String format) {
        try {
            ImageIO.write(image, format, new File(filename));
            System.out.println("Saved a snapshot in "+filename);
        }
        catch (Exception e) {
            System.err.println("Couldn't save snapshot in `"+filename+"' -- make sure the folder exists");
        }
    }
}
