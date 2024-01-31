import java.awt.*;
import java.util.List;
import java.util.ArrayList;

/**
 * PS-2 provided code
 * Using a quadtree for collision detection
 *
 * @author Jack Chou
 * @source Tim Pierson, Dartmouth CS10, Winter 2024, based on prior term code
 */
public class CollisionGUI extends InteractiveGUI {
	private static final int width=800, height=600;		// size of the window
	private List<MovingPoint> points;					// all the points
	private List<MovingPoint> colliders;				// the points that collided at this time
	private char collisionHandler = 'c';				// when there's a collision, 'c'olor them, or 'd'estroy them
	private int delay = 100;							// timer control

	public CollisionGUI() {
		super("super-collider", width, height);

		points = new ArrayList<MovingPoint>();

		// Timer drives the animation. Ticks every delay milliseconds
		startTimer();
	}

	/**
	 * Adds a MovingPoint at the (x,y) location
	 */
	private void add(int x, int y) {
		points.add(new MovingPoint(x,y,width,height));
	}

	/**
	 * InteractiveGUI method, here creating a new MovingPoint when the mouse is clicked
	 * repaint method erases the window and calls draw
	 * @param x,y location of the mouse when pressed
	 */
    @Override
	public void handleMousePress(int x, int y) {
		add(x,y);
		repaint();
	}

	/**
	 * InteractiveGUI method, get the key pressed
	 * @param k the key pressed
	 */
    @Override
	public void handleKeyPress(char k) {
		if (k == 'f') { // faster
			if (delay>1) delay /= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 's') { // slower
			delay *= 2;
			setTimerDelay(delay);
			System.out.println("delay:"+delay);
		}
		else if (k == 'r') { // add some new points at random positions
			for (int i=0; i<10; i++) {
				add((int)(width*Math.random()), (int)(height*Math.random()));
			}
			repaint();
		}
		else if (k == 'c' || k == 'd') { // control how collisions are handled
			collisionHandler = k;
			System.out.println("collision:"+k);
		}
		else if (k == 't') { // test case
			test0();
		}
	}

	/**
	 * InteractiveGUI method, here drawing all the points and then re-drawing the colliders in red
	 * @param g a reference to the window on which to draw the MovingPoints
	 */
    @Override
	public void draw(Graphics g) {
		// Ask all the points to draw themselves.
		g.setColor(Color.BLACK);
		if (points != null) {
			for (MovingPoint point : points) {
				point.draw(g);
			}
		}
		// Ask the colliders to draw themselves in red.
		if (colliders != null) {
			g.setColor(Color.RED);
			for (MovingPoint point : colliders) {
				point.draw(g);
			}
		}
	}

	/**
	 * Sets colliders to include all points in contact with another point
	 */
	private void findColliders() {
		// TODO: YOUR CODE HERE
		// Create a quadtree with the 0th point as root
		PointQuadtree<MovingPoint> tree = new PointQuadtree<MovingPoint>(points.get(0), 0, 0, width, height);

		// Now insert the rest of the points into the quadtree
		for (int i = 1; i < points.size(); i++) {tree.insert(points.get(i));}
		// Create a list to hold the colliders
		colliders = new ArrayList<MovingPoint>();

		// For each point, see if it collided with another point
		for (int i = 0; i < points.size(); i++) {
			// If the point collided with another point, add it to the list of colliders
			List<MovingPoint> collided = tree.findInCircle(points.get(i).getX(), points.get(i).getY(), points.get(i).getR() * 2);
			if (collided.size() > 1) {
				colliders.add(points.get(i));
			}
		}
	}

	/**
	 * InteractiveGUI method, here moving all the points and checking for collisions
	 */
    @Override
	public void handleTimer() {
		// Ask all the points to move themselves.
		for (MovingPoint point : points) {point.move();}
		// Check for collisions
		if (points.size() > 0) {
			findColliders();
			if (collisionHandler=='d') {
				points.removeAll(colliders);
				colliders = null;
			}
		}
		// Now update the drawing
		repaint();
	}

	// Test case, press t to activate
	public void test0(){
		// two points touching each other
		add(300,100);
		points.get(0).deltaX = 0;
		points.get(0).deltaY = 0;
		add(298,98);
		points.get(1).deltaX = 0;
		points.get(1).deltaY = 0;

		// two points not touching each other
		add(100,100);
		points.get(2).deltaX = 0;
		points.get(2).deltaY = 0;
		add(200,200);
		points.get(3).deltaX = 0;
		points.get(3).deltaY = 0;

		// three points touching each other
		add(399,399);
		points.get(4).deltaX = 0;
		points.get(4).deltaY = 0;
		add(402,402);
		points.get(5).deltaX = 0;
		points.get(5).deltaY = 0;
		add(406,406);
		points.get(6).deltaX = 0;
		points.get(6).deltaY = 0;
	}

	public static void main(String[] args) {
        new CollisionGUI();
	}
}
