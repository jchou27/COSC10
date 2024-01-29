import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Code provided for PS-1
 * Webcam-based drawing
 * Dartmouth CS 10, Winter 2024
 * @Author Jack Chou
 * @source Tim Pierson, Dartmouth CS10, Winter 2024 (based on CamPaint from previous terms)
 */
public class CamPaint extends VideoGUI {
	private char displayMode = 'w';			// what to display: 'w': live webcam, 'r': recolored image, 'p': painting
	private RegionFinder finder;			// handles the finding
	private Color targetColor;          	// color of regions of interest (set by mouse press)
	private Color paintColor = Color.blue;	// the color to put into the painting from the "brush"
	private BufferedImage painting;			// the resulting masterpiece

	private boolean paused = false; // used to pause the painting

	/**
	 * Initializes the region finder and the drawing
	 */
	public CamPaint() {
		finder = new RegionFinder();
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
		// TODO: YOUR CODE HERE
		// If it is not in webcam mode, get the image and find the regions that match the target color
		if (!paused) {
			if (displayMode == 'w') {
				super.handleImage();
			}
			finder.setImage(image);
			if (displayMode != 'w' && targetColor != null) {
				finder.findRegions(targetColor);
				// If set to recolored image mode
				if (displayMode == 'r') {
					finder.recolorImage(); // recolor the image that matches the target color
					image = finder.getRecoloredImage();
					setImage1(image); // gets the recolored image
				}
				// If set to painting mode
				if (displayMode == 'p') {
					if (painting == null) {
						clearPainting();
					}
					ArrayList<Point> brush = finder.largestRegion(); //Use the largest region as the brush
					for (Point p : brush) {
						painting.setRGB(p.x, p.y, paintColor.getRGB()); // Paint every pixel
					}
					setImage1(image); //Left Image is webcam
					setImage2(painting); //Right image is painting
				}
				super.handleImage();
			}
		}
	}


	/**
	 * Overrides the Webcam method to set the track color.
	 */
	@Override
	public void handleMousePress(int x, int y) {
		// TODO: YOUR CODE HERE
		super.handleMousePress(x, y);
		targetColor = new Color(image.getRGB(x, y));
	}

	/**
	 * Webcam method, here doing various drawing commands
	 */
	@Override
	public void handleKeyPress(char k) {
		if (k == 'p' || k == 'r' || k == 'w') { // display: painting, recolored image, or webcam
			displayMode = k;
		}
		else if (k == 'c') { // clear
			clearPainting();
		}
		else if (k == 'o') { // save the recolored image
			ImageIOLibrary.saveImage(finder.getRecoloredImage(), "pictures/recolored.png", "png");
		}
		else if (k == 's') { // save the painting
			ImageIOLibrary.saveImage(painting, "pictures/painting.png", "png");
		}
		else if (k == 'q') { // Pauses webcam and painting
			paused = !paused;
		}
		else if (k == 'b') { // change the paint color
			paintColor = JColorChooser.showDialog(this, "Choose paint color", paintColor);
		}
		else {
			System.out.println("unexpected key "+k);
		}
	}

	public static void main(String[] args) {
		new CamPaint();
	}
}
