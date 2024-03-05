import java.awt.Color;
import java.awt.Graphics;

/**
 * A rectangle-shaped Shape
 * Defined by an upper-left corner (x1,y1) and a lower-right corner (x2,y2)
 * with x1<=x2 and y1<=y2
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author CBK, updated Fall 2016
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2024
 * @author Jack Chou, modified TODO sections
 */
public class Rectangle implements Shape {
	// TODO: YOUR CODE HERE
	private int x1, y1, x2, y2;
	private Color color;

	/**
	 * An "empty" rectangle, with only one point set so far
	 * @param x1
	 * @param y1
	 * @param color
	 */
	public Rectangle(int x1, int y1, Color color) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x1;
		this.y2 = y1;
		this.color = color;
	}

	/**
	 * A rectangle defined by two corners
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param color
	 */
	public Rectangle(int x1, int y1, int x2, int y2, Color color) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.color = color;

	}

	/**
	 * Redefines the rectangle based on new corners
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */
	public void setCorners(int x1, int y1, int x2, int y2) {
		// Ensure correct upper left and lower right
		this.x1 = Math.min(x1, x2);
		this.y1 = Math.min(y1, y2);
		this.x2 = Math.max(x1, x2);
		this.y2 = Math.max(y1, y2);
	}

	@Override
	public void moveBy(int dx, int dy) {
		x1 += dx;
		y1 += dy;
		x2 += dx;
		y2 += dy;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void setColor(Color color) {
		this.color = color;
	}
		
	@Override
	public boolean contains(int x, int y) {
		return x >= x1 && x <= x2 && y >= y1 && y <= y2; // return true if the point is within the rectangle, else false
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x1, y1, x2 - x1, y2 - y1); // draw the rectangle
	}

	/**
	 * Returns a string representation of the rectangle msg to communicator
	 * Format: "rectangle " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + color.getRGB()
	 * @return
	 */
	public String toString() {
		return "rectangle " + x1 + " " + y1 + " " + x2 + " " + y2 + " " + color.getRGB();
	}
}
