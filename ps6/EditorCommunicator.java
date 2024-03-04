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
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println("message from server: " + line);
				readMessage(line);
				editor.repaint();
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
		if (msg == null)
			return;

		String[] split = msg.split(" ");
		if (split.length < 2){
			System.err.println("Bad message");
		}
		if (split[0].equals("add")){
			handleAdd(msg);
		}
		else if (split[0].equals("move")){
			handleMove(msg);
		}
		else if (split[0].equals("recolor")){
			handleRecolor(msg);
		}
		else if (split[0].equals("delete")){
			handleDelete(msg);
		}
		else if (split[0].equals("sketch")){
			startSketch(msg);
		}
	}

	 public synchronized void handleAdd(String msg){
		String[] split = msg.split(" ");
		if (split.length < 6)
			return;

		Shape newShape = null;
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
			int size = Integer.parseInt(split[2]);
			List<Integer> vector_x = new ArrayList<>();
			List<Integer> vector_y = new ArrayList<>();
			for (int i=3; i < size - 1; i++){
				vector_x.add(Integer.parseInt(split[i]));
			}
			for (int i=size+3; i < size - 1; i++){
				vector_y.add(Integer.parseInt(split[i]));
			}
			int rgb = Integer.parseInt(split[2 * size + 3]);
			newShape = new Polyline(vector_x, vector_y, new Color(rgb));
		}

		if (newShape != null){
			Integer rootID = 1;
			var map = editor.getSketch().getShapeTreeMap();
			if (map.keySet().size() == 0){
				map.put(rootID, newShape);
			}
			else {
				Integer newID = map.lastEntry().getKey() + 1;
				map.put(newID, newShape);
			}

			editor.repaint();
		}
	}

	public synchronized void handleMove(String msg){
		String[] split = msg.split(" ");
		if (split.length < 4){
			return;
		}
		Integer id = Integer.parseInt(split[1]);
		Shape shape = editor.getSketch().getShapeTreeMap().get(id);
		if (shape != null)
			shape.moveBy(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
	}

	 public synchronized void handleRecolor(String msg){
		String[] split = msg.split(" ");
		if (split.length < 3){
			return;
		}
		Integer id = Integer.parseInt(split[1]);
		Color color = new Color(Integer.parseInt(split[2]));
		Shape shape = editor.getSketch().getShapeTreeMap().get(id);
		if (shape != null) {
			shape.setColor(color);
		}
	}

	public synchronized void handleDelete(String msg){
		String[] split = msg.split(" ");
		if (split.length < 2){
			return;
		}
		Integer id = Integer.parseInt(split[1]);
		editor.getSketch().deleteShape(id);
		editor.delete();
		editor.repaint();
	}

	// to make sure new editors know the existing shapes
	public void startSketch(String msg){
		String shapeList = msg.substring(msg.indexOf("{")+1, msg.indexOf("}"));
		String[] shapes = shapeList.split(", ");
		for (String line : shapes){
			String substring = line.substring(1);
			handleAdd(substring);
		}
	}


	
}
