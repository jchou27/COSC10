import java.awt.*;

/**
 * PS-2 provided code
 * Extends Point with a radius and the ability to move and draw itself.
 * If the point attempts to move off-screen, rebounds in the opposite direction to stay on-screen.
 *
 * @author Jack Chou
 * @source Tim Pierson, Dartmouth CS10, Winter 2024, based on code from prior terms
 */
public class MovingPoint extends Point {
    protected int r=5;                      // radius of this point
	protected double deltaX =0, deltaY =0;	// move this many pixels in the x and y direction when move method called
    protected  int xmax, ymax;			    // size of the graphics window

    public MovingPoint(int x, int y, int xmax, int ymax) {
        super(x, y);
        this.xmax = xmax; this.ymax = ymax;

        // move size randomly between -r and +r
        deltaX = 2 * r * (Math.random() - 0.5);
        deltaY = 2 * r * (Math.random() - 0.5);
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }


    /**
     * move the point by deltaX in the x axis, and deltaY in the y axis.
     * If the movement would cause the point to go off screen, rebound in the opposite direction
     * by setting deltaX to -deltaX or deltaY to -deltaY.
     */
    public void move() {
        //add dx and dy to x and y
        x += deltaX;
        y += deltaY;

        // keep point on screen, accounting for radius.
        if (x > xmax - r) { //going off right side
            x = xmax - r;
            deltaX = -deltaX;
        }
        else if (x < r) { //going off left side
            x = r;
            deltaX = -deltaX;
        }
        if (y > ymax - r) { //going off bottom
            y = ymax - r;
            deltaY = -deltaY;
        }
        else if (y < r) { //going off top
            y = r;
            deltaY = -deltaY;
        }
    }

	/**
	 * Draws the point on the graphics window centered on location x,y with radius r
	 * @param g reference to the window on which to draw
	 */
	public void draw(Graphics g) {
        g.fillOval((int)(x-r), (int)(y-r), (int)(2*r), (int)(2*r));
	}
}
