import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * PS-2 provided code
 * A point quadtree: stores an element at a 2D position, with children at the subdivided quadrants
 * E extends Point2D to ensure whatever the PointQuadTree holds, it implements getX and getY
 * 
 * @author Jack Chou
 * @source Tim Pierson, Dartmouth CS10, Winter 2024, based on prior term code
 * 
 */
public class PointQuadtree<E extends Point2D> {
	private E point;							// the point anchoring this node
	private int x1, y1;							// upper-left corner of the region
	private int x2, y2;							// bottom-right corner of the region
	private PointQuadtree<E> c1, c2, c3, c4;	// children

	/**
	 * Initializes a leaf quadtree, holding the point in the rectangle
	 */
	public PointQuadtree(E point, int x1, int y1, int x2, int y2) {
		this.point = point;
		this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2;
	}

	// Getters
	public E getPoint() { return point; }
	public int getX1() { return x1; }
	public int getY1() { return y1; }
	public int getX2() { return x2; }
	public int getY2() { return y2; }

	/**
	 * Returns the child (if any) at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 * @return child for quadrant
	 */
	public PointQuadtree<E> getChild(int quadrant) {
		if (quadrant==1) return c1;
		if (quadrant==2) return c2;
		if (quadrant==3) return c3;
		if (quadrant==4) return c4;
		return null;
	}

	/**
	 * Returns whether there is a child at the given quadrant, 1-4
	 * @param quadrant	1 through 4
	 */
	public boolean hasChild(int quadrant) {
		return (quadrant==1 && c1!=null) || (quadrant==2 && c2!=null) || (quadrant==3 && c3!=null) || (quadrant==4 && c4!=null);
	}

	/**
	 * Inserts the point into the tree
	 */
	public void insert(E p2) {
		// TODO: YOUR CODE HERE
		int p2X = (int) p2.getX();
		int p2Y = (int) p2.getY();
		int pX = (int) point.getX();
		int pY = (int) point.getY();

		boolean q1 = p2X >= pX && p2Y < pY; // p2 is in quadrant 1
		boolean q2 = p2X < pX && p2Y <= pY; // p2 is in quadrant 2
		boolean q3 = p2X <= pX && p2Y > pY; // p2 is in quadrant 3
		boolean q4 = p2X > pX && p2Y >= pY; // p2 is in quadrant 4

		if (q1) {
			if (c1 == null) { // if no children in quadrant 1, create a new quadtree with p2
				c1 = new PointQuadtree<E>(p2, pX, y1, x2, pY);
			} else { // if there is a child in quadrant 1, recursively call insert on that child
				c1.insert(p2);
			}
		} else if (q2) { // if no children in quadrant 2, create a new quadtree with p2
			if (c2 == null) {
				c2 = new PointQuadtree<E>(p2, x1, y1, pX, pY);
			} else { // if there is a child in quadrant 2, recursively call insert on that child
				c2.insert(p2);
			}
		} else if (q3) {
			if (c3 == null) { // if no children in quadrant 3, create a new quadtree with p2
				c3 = new PointQuadtree<E>(p2, x1, pY, pX, y2);
			} else { // if there is a child in quadrant 3, recursively call insert on that child
				c3.insert(p2);
			}
		} else if (q4) {
			if (c4 == null) { // if no children in quadrant 4, create a new quadtree with p2
				c4 = new PointQuadtree<E>(p2, pX, pY, x2, y2);
			} else { // if there is a child in quadrant 4, recursively call insert on that child
				c4.insert(p2);
			}
		}
	}
	
	/**
	 * Finds the number of points in the quadtree (including its descendants)
	 */
	public int size() {
		// TODO: YOUR CODE HERE
		return allPoints().size(); // size of the ArrayList of all points
	}
	
	/**
	 * Builds a list of all the points in the quadtree (including its descendants)
	 * @return List with all points in the quadtree
	 */
	public List<E> allPoints() {
		// TODO: YOUR CODE HERE
		List<E> allPoints = new ArrayList<E>(); // Create new ArrayList to hold all points
		allPoints.add(point); // add the point of the current quadtree to the ArrayList
		// add all points from the children of the current quadtree to the ArrayList
		if (c1 != null) {allPoints.addAll(c1.allPoints());}
		if (c2 != null) {allPoints.addAll(c2.allPoints());}
		if (c3 != null) {allPoints.addAll(c3.allPoints());}
		if (c4 != null) {allPoints.addAll(c4.allPoints());}
		return allPoints;
	}	

	/**
	 * Uses the quadtree to find all points within the circle
	 * @param cx	circle center x
	 * @param cy  	circle center y
	 * @param cr  	circle radius
	 * @return    	the points in the circle (and the qt's rectangle)
	 */
	public List<E> findInCircle(double cx, double cy, double cr) {
		// TODO: YOUR CODE HERE
		List<E> pointsInCircle = new ArrayList<E>(); // Create new ArrayList to hold all points in the circle
		addHit(pointsInCircle, cx, cy, cr); // add all points in the circle to the ArrayList
		return pointsInCircle;
	}

	// TODO: YOUR CODE HERE for any helper methods
	public void addHit(List<E> listOfPoints, double cx, double cy, double cr){
	// If the circle intersects the rectangle of the current quadtree, check if the point is in the circle
		if (Geometry.circleIntersectsRectangle(cx, cy, cr, x1, y1, x2, y2)) {
			// If the point is in the circle, add it to the ArrayList
			if (Geometry.pointInCircle(point.getX(), point.getY(), cx, cy, cr)) {
				listOfPoints.add(point);
			}
			// Recursively call addHit on the children of the current quadtree
			if (c1 != null) {c1.addHit(listOfPoints, cx, cy, cr);}
			if (c2 != null) {c2.addHit(listOfPoints, cx, cy, cr);}
			if (c3 != null) {c3.addHit(listOfPoints, cx, cy, cr);}
			if (c4 != null) {c4.addHit(listOfPoints, cx, cy, cr);}
		}
	}
}
