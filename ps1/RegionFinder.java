import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Code provided for PS-1
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Dartmouth CS 10, Winter 2024
 *
 * @Author: Jack Chou
 * @source Tim Pierson, Dartmouth CS10, Winter 2024, based on prior terms RegionFinder
 */
public class RegionFinder {
	private static final int maxColorDiff = 20;                // how similar a pixel color must be to the target color, to belong to a region
	private static final int minRegion = 50;                // how many points in a region to be worth considering

	private BufferedImage image;                            // the image in which to find regions
	private BufferedImage recoloredImage;                   // the image with identified regions recolored

	private ArrayList<ArrayList<Point>> regions;

	// a region is a list of points
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
		regions = new ArrayList<>();
		// Initialize image structure holding all the visited pixels within the original image
		BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		// Looping through every pixel in the image
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				Color currColor = new Color(image.getRGB(x, y));
				//If the color does not match or the region is visited, go to the next pixel
				if (!colorMatch(currColor, targetColor) || visited.getRGB(x, y) != 0) {
					continue; // Skips current iteration of the loop
				}
				// Initialize a region to store pixels and a stack that stores points to go visit
				ArrayList<Point> currRegion = new ArrayList<Point>();
				Stack<Point> toVisit = new Stack<Point>(); // used stack to pop a value (remove and return the element value)
				toVisit.add(new Point(x, y));    //Add the current point to the points to go visit
				// Search for neighbors until all the points are visited
				while (!toVisit.isEmpty()) {
					Point currPoint = toVisit.pop();// get a point to visit
					currRegion.add(currPoint);            // add the current point to visit to the current region
					//Loop through neighbors, add neighbor points to the to visit stack
					for (int nY = Math.max(0, currPoint.y - 1); nY <= Math.min(currPoint.y + 1, image.getHeight() - 1); nY++) {
						for (int nX = Math.max(0, currPoint.x - 1); nX <= Math.min(currPoint.x + 1, image.getWidth() - 1); nX++) {
							Color nColor = new Color(image.getRGB(nX, nY));
							if (colorMatch(nColor, targetColor) && visited.getRGB(nX, nY) == 0) {
								toVisit.add(new Point(nX, nY));
								visited.setRGB(currPoint.x, currPoint.y, 1);    // Mark point as visited
							}
						}
					}
				}
				// if region size is big enough, add it to the regions
				if (currRegion.size() >= minRegion) {
					regions.add(currRegion);
					System.out.println("added curr region to regions");
				}
			}
		}
	}

	/**
	 * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
	 */
	protected static boolean colorMatch(Color c1, Color c2) {
		// TODO: YOUR CODE HERE
		// Matches color depending on the difference of c1 and c2
		if (Math.abs(c1.getRed() - c2.getRed())     <= maxColorDiff &&
			Math.abs(c1.getGreen() - c2.getGreen())   <= maxColorDiff &&
			Math.abs(c1.getBlue() - c2.getBlue()) <= maxColorDiff) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the largest region detected (if any region has been detected)
	 */
	public ArrayList<Point> largestRegion() {
		// TODO: YOUR CODE HERE
		ArrayList<Point> largest = new ArrayList<>();
		for (ArrayList<Point> currRegion: regions){
			if (currRegion.size() > largest.size()){
				largest = currRegion;
			}
		}
		return largest;
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
		// TODO: YOUR CODE HERE


		for (ArrayList<Point> region : regions) {
			int randR = (int)(Math.random() * 256);
			int randG = (int)(Math.random() * 256);
			int randB = (int)(Math.random() * 256);
			Color newColor = new Color(randR, randG, randB);
			// The region is guaranteed to be bigger than minimum size because we had checked that in findRegions
			for (Point currPoint : region) {
				recoloredImage.setRGB(currPoint.x, currPoint.y, newColor.getRGB());
			}
		}
	}

}
