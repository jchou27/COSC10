import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Client-server graphical editor
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; loosely based on CS 5 code by Tom Cormen
 * @author CBK, winter 2014, overall structure substantially revised
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 * @author CBK, spring 2016 and Fall 2016, restructured Shape and some of the GUI
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2024
 * @author Jack Chou, modified TODO sections and added handle methods
 */

public class Editor extends JFrame {	
	private static String serverIP = "localhost";			// IP address of sketch server
	// "localhost" for your own machine;
	// or ask a friend for their IP address

	private static final int width = 800, height = 800;		// canvas size

	// Current settings on GUI
	public enum Mode {
		DRAW, MOVE, RECOLOR, DELETE
	}
	private Mode mode = Mode.DRAW;				// drawing/moving/recoloring/deleting objects
	private String shapeType = "ellipse";		// type of object to add
	private Color color = Color.black;			// current drawing color

	// Drawing state
	// these are remnants of my implementation; take them as possible suggestions or ignore them
	private Shape curr = null;					// current shape (if any) being drawn
	private Sketch sketch;						// holds and handles all the completed objects
	private int movingId = -1;					// current shape id (if any; else -1) being moved
	private Point drawFrom = null;				// where the drawing started
	private Point moveFrom = null;				// where object is as it's being dragged


	// Communication
	private EditorCommunicator comm;			// communication with the sketch server

	public Editor() {
		super("Graphical Editor");

		sketch = new Sketch();

		// Connect to server
		comm = new EditorCommunicator(serverIP, this);
		comm.start();

		// Helpers to create the canvas and GUI (buttons, etc.)
		JComponent canvas = setupCanvas();
		JComponent gui = setupGUI();

		// Put the buttons and canvas together into the window
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());
		cp.add(canvas, BorderLayout.CENTER);
		cp.add(gui, BorderLayout.NORTH);

		// Usual initialization
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * Creates a component to draw into
	 */
	private JComponent setupCanvas() {
		JComponent canvas = new JComponent() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				drawSketch(g);
			}
		};
		
		canvas.setPreferredSize(new Dimension(width, height));

		canvas.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				handlePress(event.getPoint());
			}

			public void mouseReleased(MouseEvent event) {
				handleRelease();
			}
		});		

		canvas.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent event) {
				handleDrag(event.getPoint());
			}
		});
		
		return canvas;
	}

	/**
	 * Creates a panel with all the buttons
	 */
	private JComponent setupGUI() {
		// Select type of shape
		String[] shapes = {"ellipse", "freehand", "rectangle", "segment"};
		JComboBox<String> shapeB = new JComboBox<String>(shapes);
		shapeB.addActionListener(e -> shapeType = (String)((JComboBox<String>)e.getSource()).getSelectedItem());

		// Select drawing/recoloring color
		// Following Oracle example
		JButton chooseColorB = new JButton("choose color");
		JColorChooser colorChooser = new JColorChooser();
		JLabel colorL = new JLabel();
		colorL.setBackground(Color.black);
		colorL.setOpaque(true);
		colorL.setBorder(BorderFactory.createLineBorder(Color.black));
		colorL.setPreferredSize(new Dimension(25, 25));
		JDialog colorDialog = JColorChooser.createDialog(chooseColorB,
				"Pick a Color",
				true,  //modal
				colorChooser,
				e -> { color = colorChooser.getColor(); colorL.setBackground(color); },  // OK button
				null); // no CANCEL button handler
		chooseColorB.addActionListener(e -> colorDialog.setVisible(true));

		// Mode: draw, move, recolor, or delete
		JRadioButton drawB = new JRadioButton("draw");
		drawB.addActionListener(e -> mode = Mode.DRAW);
		drawB.setSelected(true);
		JRadioButton moveB = new JRadioButton("move");
		moveB.addActionListener(e -> mode = Mode.MOVE);
		JRadioButton recolorB = new JRadioButton("recolor");
		recolorB.addActionListener(e -> mode = Mode.RECOLOR);
		JRadioButton deleteB = new JRadioButton("delete");
		deleteB.addActionListener(e -> mode = Mode.DELETE);
		ButtonGroup modes = new ButtonGroup(); // make them act as radios -- only one selected
		modes.add(drawB);
		modes.add(moveB);
		modes.add(recolorB);
		modes.add(deleteB);
		JPanel modesP = new JPanel(new GridLayout(1, 0)); // group them on the GUI
		modesP.add(drawB);
		modesP.add(moveB);
		modesP.add(recolorB);
		modesP.add(deleteB);

		// Put all the stuff into a panel
		JComponent gui = new JPanel();
		gui.setLayout(new FlowLayout());
		gui.add(shapeB);
		gui.add(chooseColorB);
		gui.add(colorL);
		gui.add(modesP);
		return gui;
	}

	/**
	 * Getter for the sketch instance variable
	 */
	public Sketch getSketch() {
		return sketch;
	}

	/**
	 * Draws all the shapes in the sketch,
	 * along with the object currently being drawn in this editor (not yet part of the sketch)
	 */
	public void drawSketch(Graphics g) {
		// TODO: YOUR CODE HERE
		sketch.draw(g); // draw the sketch and store the shapes in the sketch
		if (curr != null) {
			curr.draw(g); // draw the shape that is being manipulated by the user
		}
	}

	// Helpers for event handlers
	
	/**
	 * Helper method for press at point
	 * In drawing mode, start a new object;
	 * in moving mode, (request to) start dragging if clicked in a shape;
	 * in recoloring mode, (request to) change clicked shape's color
	 * in deleting mode, (request to) delete clicked shape
	 */
	private void handlePress(Point p) {
		// TODO: YOUR CODE HERE
		if (mode == Mode.DRAW) { // draw new shape and set drawFrom
			drawFrom = p; // set drawFrom to the point
			curr = null; // set the current shape to null
			moveFrom = p; // set moveFrom to the point
            switch (shapeType) { // check the shape type
                case "ellipse" -> curr = new Ellipse(drawFrom.x, drawFrom.y, color);
                case "freehand" -> {
                    List<Integer> xs = new ArrayList<>(); // create a new list of x-coordinate of points
                    List<Integer> ys = new ArrayList<>(); // create a new list of y-coordinate of points
                    xs.add(p.x); // add the x-coordinate to the list
                    ys.add(p.y); // add the y-coordinate to the list
                    curr = new Polyline(xs, ys, color); // create a new polyline shape
                }
                case "rectangle" -> curr = new Rectangle(drawFrom.x, drawFrom.y, color);
                case "segment" -> curr = new Segment(drawFrom.x, drawFrom.y, color);
            }
		}
		else if (mode == Mode.MOVE) { // move if clicked in shape
			movingId = sketch.contains(p.x, p.y); // get the id of the shape that is being moved
			if (movingId > 0) { // if the id is greater than 0
				curr = sketch.getShapeTreeMap().get(movingId); // get the shape from the sketch
				moveFrom = p; // set moveFrom to the point
			}
		}
		else if (mode == Mode.RECOLOR) { // recolor
			movingId = sketch.contains(p.x, p.y); // get the id of the shape that is being recolored
			if (movingId > 0) { // if the id is greater than 0
				comm.send("recolor " + movingId + " " + color.getRGB()); // send the recolor message to the server
			}
		}
		else if (mode == Mode.DELETE) { // delete
			movingId = sketch.contains(p.x,p.y); // get the id of the shape that is being deleted
			if (sketch != null && movingId > 0){ // if the sketch is not null and the id is greater than 0
				comm.send("delete " + movingId); // send the delete message to the server
			}
		}
	}


	/**
	 * Helper method for drag to new point
	 * In drawing mode, update the other corner of the object;
	 * in moving mode, (request to) drag the object
	 */
	private void handleDrag(Point p) {
		// TODO: YOUR CODE HERE
		if (mode == Mode.DRAW) { // draw
			if (curr != null) { // if the current shape is not null
                switch (shapeType) { // check the shape type
                    case "ellipse" -> {
                        ((Ellipse) curr).setCorners(drawFrom.x, drawFrom.y, p.x, p.y);
						//creates a new point in the center of the shape
                        moveFrom = new Point(drawFrom.x + (p.x - drawFrom.x) / 2, drawFrom.y + (p.y - drawFrom.y) / 2);
                    }
                    case "rectangle" -> {
                        ((Rectangle) curr).setCorners(drawFrom.x, drawFrom.y, p.x, p.y);
                        moveFrom = new Point(drawFrom.x + (p.x - drawFrom.x) / 2, drawFrom.y + (p.y - drawFrom.y) / 2);
                    }
                    case "segment" -> {
                        ((Segment) curr).setEnd(p.x, p.y);
                        moveFrom = new Point((p.x - drawFrom.x) / 2, (p.y - drawFrom.y) / 2);
                    }
                    case "freehand" -> {
                        ((Polyline) curr).pointsX.add(p.x);
                        ((Polyline) curr).pointsY.add(p.y);
                        moveFrom = new Point((p.x - drawFrom.x) / 2, (p.y - drawFrom.y) / 2);
                    }
                }
			}
		} else if (mode == Mode.MOVE) { // move
			if (curr != null && moveFrom != null && curr.contains(p.x, p.y)) { // if there is a current shape and a moveFrom point and the current shape contains the point
				comm.send("move " + movingId + " " + (p.x - moveFrom.x) + " " + (p.y - moveFrom.y)); // send the move message to the server
				moveFrom = p; // set moveFrom to the point
			}
		}
		repaint(); // refresh the canvas
	}

	/**
	 * Helper method for release
	 * In drawing mode, pass the add new object request on to the server;
	 * in moving mode, release it		
	 */
	private void handleRelease() {
		// TODO: YOUR CODE HERE
		if (mode == Mode.DRAW){ // draw
			comm.send("add " + curr.toString()); // send the add message to the server
			curr = null; // set the current shape to null so it does not draw the shape again
		}
		else if (mode == Mode.MOVE) { // move
			moveFrom = null; // set moveFrom to null
		}
		repaint();
	}

	public void delete(){
		curr = null;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Editor();
			}
		});	
	}
}
