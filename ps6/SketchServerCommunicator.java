import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles communication between the server and one client, for SketchServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate SketchServerCommunicator
 * @author Tim Pierson Dartmouth CS 10, provided for Winter 2024
 */
public class SketchServerCommunicator extends Thread {
	private Socket sock;					// to talk with client
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client
	private SketchServer server;			// handling communication for

	public SketchServerCommunicator(Socket sock, SketchServer server) {
		this.sock = sock;
		this.server = server;
	}

	/**
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
	
	/**
	 * Keeps listening for and handling (your code) messages from the client
	 */
	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Tell the client the current state of the world
			// TODO: YOUR CODE HERE
			send(server.getSketch().toString());

			// Keep getting and handling messages from the client
			// TODO: YOUR CODE HERE
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println("recived: " + line);
				readClient(line);
			}

			// Clean up -- note that also remove self from server's list so it doesn't broadcast here
			server.removeCommunicator(this);
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	synchronized public void readClient(String msg){
		if (msg == null){
			System.err.println("Bad message");
		}
		String[] split = msg.split(" ");
		if (split.length < 2){
			System.err.println("Bad message");
			return;
		}

		if (split[0].equals("add")){
			handleMasterAdd(msg);
		}
		else if (split[0].equals("move")){
			handleMasterMove(msg);
		}
		else if (split[0].equals("recolor")){
			handleMasterRecolor(msg);
		}
		else if (split[0].equals("delete")){
			handleMasterDelete(msg);
		}
	}

	synchronized public void handleMasterAdd(String msg){
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
			var map = server.getSketch().getShapeTreeMap();
			if (map.keySet().size() == 0){
				map.put(rootID, newShape);
			}
			else {
				Integer newID = map.lastEntry().getKey() + 1;
				map.put(newID, newShape);
			}
			server.broadcast("add " + newShape);
		}
	}

	synchronized public void handleMasterMove(String msg){
		String[] split = msg.split(" ");
		if (split.length < 4){
			return;
		}
		Integer id = Integer.parseInt(split[1]);
		Shape shape = server.getSketch().getShapeTreeMap().get(id);
		if (shape != null) {
			shape.moveBy(Integer.parseInt(split[2]), Integer.parseInt(split[3]));
			server.broadcast(msg);
		}
	}

	synchronized public void handleMasterRecolor(String msg){
		String[] split = msg.split(" ");
		if (split.length < 3){
			return;
		}
		Integer id = Integer.parseInt(split[1]);
		Color color = new Color(Integer.parseInt(split[2]));
		Shape shape = server.getSketch().getShapeTreeMap().get(id);
		if (shape != null) {
			shape.setColor(color);
			server.broadcast(msg);
		}
	}

	synchronized public void handleMasterDelete(String msg){
		String[] split = msg.split(" ");
		if (split.length < 2){
			return;
		}
		Integer id = Integer.parseInt(split[1]);
		server.getSketch().deleteShape(id);
		server.broadcast(msg);
	}
}
