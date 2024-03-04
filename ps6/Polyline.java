import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A multi-segment Shape, with straight lines connecting "joint" points -- (x1,y1) to (x2,y2) to (x3,y3) ...
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Spring 2016
 * @author CBK, updated Fall 2016
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2024
 */
public class Polyline implements Shape {
	// TODO: YOUR CODE HERE
	public List<Integer> pointsX;
	public List<Integer> pointsY;
	private Color color;

	public Polyline(List<Integer> x, List<Integer> y, Color color) {
		pointsX = new ArrayList<>(x);
		pointsY = new ArrayList<>(y);
		this.color = color;
	}


	@Override
	public void moveBy(int dx, int dy) {
		for (Integer x : pointsX){
			x += dx;
		}
		for (Integer y : pointsY){
			y += dy;
		}
	}

	@Override
	public Color getColor() { return color; }

	@Override
	public void setColor(Color color) { this.color = color; }

	@Override
	public boolean contains(int x, int y) {
		for (int i = 0; i < pointsX.size() - 1; i++){
			int x1 = pointsX.get(i);
			int x2 = pointsX.get(i + 1);
			int y1 = pointsY.get(i);
			int y2 = pointsY.get(i + 1);
			if (Segment.pointToSegmentDistance(x, y, x1, y1, x2, y2) <= 3)
				return true;
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(color);
		for (int i = 0; i < pointsX.size() - 1; i++){
			g.drawLine(pointsX.get(i), pointsY.get(i), pointsX.get(i+1), pointsY.get(i+1));
		}
	}

	@Override
	public String toString() {
		String s = "Polyline ";
		int size = pointsX.size();
		s += size + " ";
		for (int i = 0; i < pointsX.size(); i++){
			s += pointsX.get(i) + " ";
		}
		for (int i = 0; i < pointsX.size(); i++){
			s += pointsY.get(i) + " ";
		}
		s += color.getRGB();
		return s;
	}
}
