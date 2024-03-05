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
 * @author Jack Chou, modified TODO sections and added handle master methods
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
			send(server.getSketch().toString()); // send the current state of the world to the client

			// Keep getting and handling messages from the client
			// TODO: YOUR CODE HERE
			String line; // read the message from the client
			while ((line = in.readLine()) != null) { // keep reading messages from the client
				System.out.println("received: " + line); // print the message from the client
				readClient(line); // handle the message from the client
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

	/**
	 * Handles a message from the client
	 * @param msg
	 */
	 public synchronized void readClient(String msg){
		if (msg == null){ // if no message, print "Bad message"
			System.err.println("Bad message");
		}
		String[] split = msg.split(" "); // split the message
		if (split.length < 2){ // if the message is less than 2, print "Bad message" and return
			System.err.println("Bad message");
			return;
		}

         switch (split[0]) { // check the message at the zero index
             case "add" -> handleMasterAdd(msg); // if the message is "add", call the handleMasterAdd method
             case "move" -> handleMasterMove(msg); // if the message is "move", call the handleMasterMove method
             case "recolor" -> handleMasterRecolor(msg); // if the message is "recolor", call the handleMasterRecolor method
             case "delete" -> handleMasterDelete(msg); // if the message is "delete", call the handleMasterDelete method
         }
	}

	/**
	 * Handles a message from the client to add a new shape to the sketch
	 * @param msg
	 */
	public synchronized void handleMasterAdd(String msg){
		String[] split = msg.split(" "); // split the message
		if (split.length < 6) // if the message is less than 6, return and do nothing
			return;

		Shape newShape = null; // create a new shape and set it to null

        switch (split[1]) { // check the message at the first index
            case "ellipse" ->
                    newShape = new Ellipse(Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]),
                            Integer.parseInt(split[5]), new Color(Integer.parseInt(split[6])));
            case "rectangle" ->
                    newShape = new Rectangle(Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]),
                            Integer.parseInt(split[5]), new Color(Integer.parseInt(split[6])));
            case "segment" ->
                    newShape = new Segment(Integer.parseInt(split[2]), Integer.parseInt(split[3]), Integer.parseInt(split[4]),
                            Integer.parseInt(split[5]), new Color(Integer.parseInt(split[6])));
            case "freehand" -> {
                int size = Integer.parseInt(split[2]); // get the size of the message from the second index
                List<Integer> pointsX = new ArrayList<>(); // create a new list of x-coordinate of points
                List<Integer> pointsY = new ArrayList<>(); // create a new list of y-coordinate of points
                for (int i = 3; i < size + 3; i++) { // loop through the message, since X coordinate starts at the third index until end of X coordinate list (size + 3)
                    pointsX.add(Integer.parseInt(split[i])); // add the x-coordinate to the list
                }
                for (int i = size + 3; i < 2 * size + 3 ; i++) { // loop through the message, since Y coordinate ends at the 2 * (size + 3) index
                    pointsY.add(Integer.parseInt(split[i])); // add the y-coordinate to the list
                }
                int rgb = Integer.parseInt(split[2 * size + 3]); // get the color of the message from the last index of the msg representation
                newShape = new Polyline(pointsX, pointsY, new Color(rgb)); // create a new polyline shape
            }
        }

		if (newShape != null){ // if the new shape is not null
			Integer rootID = 1; // set the rootID to 1
			var map = server.getSketch().getShapeTreeMap(); // get the shape tree map from the server
			if (map.keySet().isEmpty()){ // if the map is empty
				map.put(rootID, newShape); // put the new shape to the map
			}
			else { // if the map is not empty
				Integer newID = map.lastEntry().getKey() + 1; // get the last id of the map and add 1 to it
				map.put(newID, newShape); // put the new shape to the map
			}
			server.broadcast("add " + newShape); // broadcast the new shape to the server
		}
	}

	/**
	 * Handles a message from the client to move a shape in the sketch
	 * @param msg
	 */
	 public synchronized void handleMasterMove(String msg){
		String[] split = msg.split(" "); // split the message
		if (split.length < 4){ // if the message is less than 4, return and do nothing
			return;
		}
		Integer id = Integer.parseInt(split[1]); // get the id of the message from the first index
		Shape shape = server.getSketch().getShapeTreeMap().get(id); // get the shape from the server
		if (shape != null) { // if there is a shape
			shape.moveBy(Integer.parseInt(split[2]), Integer.parseInt(split[3])); // move the shape by the dx and dy  (p - moveFrom)
			server.broadcast(msg); // broadcast the message to the server
		}
	}

	/**
	 * Handles a message from the client to recolor a shape in the sketch
	 * @param msg
	 */
	 public synchronized void handleMasterRecolor(String msg){
		String[] split = msg.split(" "); // split the message
		if (split.length < 3){ // if the message is less than 3, return and do nothing
			return;
		}
		Integer id = Integer.parseInt(split[1]); // get the id of the message from the first index
		Color color = new Color(Integer.parseInt(split[2])); // get the color of the message from the second index
		Shape shape = server.getSketch().getShapeTreeMap().get(id); // get the shape from the server
		if (shape != null) { // if there is a shape
			shape.setColor(color); // set the color of the shape
			server.broadcast(msg); // broadcast the message to the server
		}
	}

	/**
	 * Handles a message from the client to delete a shape from the sketch
	 * @param msg
	 */
	 public synchronized void handleMasterDelete(String msg){
		String[] split = msg.split(" "); // split the message
		if (split.length < 2){ // if the message is less than 2, return and do nothing
			return;
		}
		Integer id = Integer.parseInt(split[1]); // get the id of the message from the first index
		server.getSketch().deleteShape(id); // delete the shape from the server
		server.broadcast(msg); // broadcast the message to the server
	}
}
