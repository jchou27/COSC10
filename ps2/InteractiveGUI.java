import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * PS-2 provided code
 * An interactive version of the simple ImageGUI class
 * Introduces a timer that when started, ticks every 100 milliseconds by default (change with 'f' or 's' keys)
 * Adds methods for handleKeyPress, handleMousePress, handleMouseMotion, and handleTimer
 *
 * @author Jack Chou
 * @source Tim Pierson, Dartmouth CS10, Winter 2024, based on DrawingGUI from prior terms
 */
public class InteractiveGUI extends ImageGUI {
	protected Timer timer;							// timer to tick every delay milliseconds
	private static final int delay = 100;			// default delay for the timer (milliseconds)


	public InteractiveGUI(String title, int width, int height) {
		super(title);

		//remove the panel added by ImageGUI
		getContentPane().remove(panel);

		// Create a JPanel that calls draw on repaint
		panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				draw(g);
			}
		};

		// Listener to ImageGUI's JPanel for events
		panel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent event) {
				handleMousePress(event.getPoint().x, event.getPoint().y);
			}
		});

		panel.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent event) {
				handleMouseMotion(event.getPoint().x, event.getPoint().y);
			}
		});

		addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent event) {
				handleKeyPress(event.getKeyChar());
			}
		});

		timer = new Timer(delay, new AbstractAction("update") {
			public void actionPerformed(ActionEvent e) {
				handleTimer();
			}
		});

		// Boilerplate to finish initializing the GUI to the specified size
		panel.setSize(width, height);
		getContentPane().add(panel);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	/**
	 * Set the delay of the timer.
	 * @param delay
	 */
	public void setTimerDelay(int delay) {
		timer.setDelay(delay);
	}

	/**
	 * Call back method on repaint
	 * Override for alternative functionality.
	 * @param g reference to window on which to draw
	 */
	public void draw(Graphics g) {

	}

	/**
	 * Called back when the mouse is pressed.
	 * Override for alternative functionality
	 * @param x mouse x location when pressed
	 * @param y mouse y location when pressed
	 */
	public void handleMousePress(int x, int y) {
		System.out.println("Got mouse " + x + ", " + y);
	}

	/**
	 * Method to handle mouse motion
	 * Override for alternative functionality.
	 * @param x		x coordinate of mouse as it moves
	 * @param y		y coordinate of mouse as it moves
	 */
	public void handleMouseMotion(int x, int y) {

	}

	/**
	 * Called back when a key is pressed
	 * Override for alternative functionality
	 * @param key key that was pressed
	 */
	public void handleKeyPress(char key) {
		System.out.println("Key pressed: " + key);
	}

	/**
	 * Start the timer ticking.
	 */
	public void startTimer() {
		System.out.println("timer started");
		timer.start();
	}


	/**
	 * Method to respond to the timer ticking, called on every timer tick
	 * Override for alternative functionality.
	 */
	public void handleTimer() {
		System.out.println("timer ticked");

	}
}
