import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


/**
 * Handles communication to/from the server for the editor
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Chris Bailey-Kellogg; overall structure substantially revised Winter 2014
 * @author Travis Peters, Dartmouth CS 10, Winter 2015; remove EditorCommunicatorStandalone (use echo server for testing)
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2024
 * @author Jack Chou, modified TODO sections and added handle methods
 */
public class EditorCommunicator extends Thread {
	private PrintWriter out;		// to server
	private BufferedReader in;		// from server
	protected Editor editor;		// handling communication for

	/**
	 * Establishes connection and in/out pair
	 */
	public EditorCommunicator(String serverIP, Editor editor) {
		this.editor = editor;
		System.out.println("connecting to " + serverIP + "...");
		try {
			Socket sock = new Socket(serverIP, 4242);
			out = new PrintWriter(sock.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("...connected");
		}
		catch (IOException e) {
			System.err.println("couldn't connect");
			System.exit(-1);
		}
	}

	/**
	 * Sends message to the server
	 */
	public void send(String msg) {
		out.println(msg);
	}

	/**
	 * Keeps listening for and handling (your code) messages from the server
	 */
	public void run() {
		try {
			// Handle messages
			// TODO: YOUR CODE HERE
			String line; // read the message from the server
			while ((line = in.readLine()) != null) { // keep reading messages from the server
				System.out.println("message from server: " + line); // print the message from the server
				readMessage(line); // handle the message from the server
				editor.repaint(); // update the GUI
			}

		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("server hung up");
		}
	}	

	// Send editor requests to the server
	// TODO: YOUR CODE HERE
	public void readMessage(String msg) {
		if (msg == null){ // if the message is null, return and do nothing
			return;
		}
		String[] split = msg.split(" "); // split the message by spaces
		if (split.length < 2){ // if the message is less than 2, print "Bad message"
			System.err.println("Bad message");
		}
		if (split[0].equals("add")){ // if the first word of the message is "add", call handleAdd with the message
			handleAdd(msg);
		}
		else if (split[0].equals("move")){ // if the first word of the message is "move", call handleMove with the message
			handleMove(msg);
		}
		else if (split[0].equals("recolor")){ // if the first word of the message is "recolor", call handleRecolor with the message
			handleRecolor(msg);
		}
		else if (split[0].equals("delete")){ // if the first word of the message is "delete", call handleDelete with the message
			handleDelete(msg);
		}
		else if (split[0].equals("sketch")){ // if the first word of the message is "sketch", call startSketch with the message
			startSketch(msg);
		}
	}

	/**
	 * Handles an "add" message from the server
	 * @param msg
	 */
	public synchronized void handleAdd(String msg){
		String[] split = msg.split(" "); // split the message by spaces
		if (split.length < 6) // if the message is less than 6, return and do nothing
			return;

		Shape newShape = null; // create a new shape and set it to null
		if (split[1].equals("ellipse")){
			newShape = new Ellipse(Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]),
					Integer.parseInt(split[5]), new Color(Integer.parseInt(split[6])));
		}
		else if (split[1].equals("rectangle")){
			newShape = new Rectangle(Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]),
					Integer.parseInt(split[5]), new Color(Integer.parseInt(split[6])));
		}
		else if (split[1].equals("segment")){
			newShape = new Segment(Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]),
					Integer.parseInt(split[5]), new Color(Integer.parseInt(split[6])));
		}
		else if (split[1].equals("freehand")){
			int size = Integer.parseInt(split[2]); // get the size of the message from the second index
			List<Integer> vector_x = new ArrayList<>(); // create a new list of x-coordinate of points
			List<Integer> vector_y = new ArrayList<>(); // create a new list of y-coordinate of points
			for (int i = 3; i < size + 3; i++){ // loop through the message, since X coordinate starts at the third index until end of X coordinate list (size + 3)
				vector_x.add(Integer.parseInt(split[i])); // add the x-coordinate to the list
			}
			for (int i = size+3; i < 2 * size + 3; i++){ // loop through the message, since Y coordinate starts at the size + 3 index until end of Y coordinate list (2 * size + 3)
				vector_y.add(Integer.parseInt(split[i])); // add the y-coordinate to the list
			}
			int rgb = Integer.parseInt(split[2 * size + 3]); // get the color of the message from the last index of the msg representation
			newShape = new Polyline(vector_x, vector_y, new Color(rgb)); // create a new polyline shape
		}
		// add the new shape to the sketch
		if (newShape != null){ // if the new shape is not null
			Integer rootID = 1; // set the rootID to 1
			var map = editor.getSketch().getShapeTreeMap(); // get the shape tree map from the editor
			if (map.keySet().isEmpty()){ // if the map is empty
				map.put(rootID, newShape); // put the new shape to the map
			}
			else { //if there is already a shape in the map
				Integer newID = map.lastEntry().getKey() + 1; // set the new id to the last id + 1
				map.put(newID, newShape); // put the new shape to the map
			}
			editor.repaint(); // update the GUI
		}
	}

	/**
	 * Handles a "move" message from the server
	 * @param msg
	 */
	public synchronized void handleMove(String msg){
		String[] split = msg.split(" "); // split the message by spaces
		if (split.length < 4){ // if the message is less than 4, return and do nothing
			return;
		}
		Integer id = Integer.parseInt(split[1]); // get the id of the shape from the message
		Shape shape = editor.getSketch().getShapeTreeMap().get(id); // get the shape from the editor
		if (shape != null) { // if there is a shape
			shape.moveBy(Integer.parseInt(split[2]), Integer.parseInt(split[3])); // move the shape by the x and y coordinates from the message
		}
	}

	/**
	 * Handles a "recolor" message from the server
	 * @param msg
	 */
	 public synchronized void handleRecolor(String msg){
		String[] split = msg.split(" "); // split the message by spaces
		if (split.length < 3){ // if the message is less than 3, return and do nothing
			return;
		}
		Integer id = Integer.parseInt(split[1]); // get the id of the shape from the message
		Color color = new Color(Integer.parseInt(split[2])); // get the color of the shape from the message
		Shape shape = editor.getSketch().getShapeTreeMap().get(id); // get the shape from the editor
		if (shape != null) { // if there is a shape
			shape.setColor(color); // set the color of the shape to the color from the message
		}
	}

	/**
	 * Handles a "delete" message from the server
	 * @param msg
	 */
	public synchronized void handleDelete(String msg){
		String[] split = msg.split(" "); // split the message by spaces
		if (split.length < 2){ // if the message is less than 2, return and do nothing
			return;
		}
		Integer id = Integer.parseInt(split[1]); // get the id of the shape from the message
		editor.getSketch().deleteShape(id); // delete the shape from the editor
		editor.delete(); // update the GUI
		editor.repaint(); // update the GUI
	}

	/**
	 * Handles a "sketch" message from the server that gives multiple ids and its respective shape as input
	 * sketch { " + id1 + " " + shape1 + ", " + id2 + " " + shape2 + ", ... + " }
	 * @param msg
	 */
	public void startSketch(String msg){
		String shapeList = msg.substring(msg.indexOf("{")+1, msg.indexOf("}")); // get the list of shapes from the message
		String[] shapes = shapeList.split(", "); // split the list of shapes by comma and space
		for (String line : shapes){ // loop through the list of shapes
			String substring = line.substring(1); // get the substring of the line
			handleAdd(substring); // add the shape to the editor
		}
	}


	
}
