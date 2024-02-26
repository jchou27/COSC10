import java.io.*;
import java.net.*;

/**
 * Handles communication between the server and one client, for ChatServer
 * 
 *  @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised Winter 2014 to separate out ChatServerFromClient
 *  @author Tim Pierson, Dartmouth CS 10, provided for Winter 2024
 */
public class ChatServerCommunicator extends Thread {
	private Socket sock;					// each instance is in a different thread and has its own socket
	private ChatServer server;				// the main server instance
	private String name;					// client's name (first interaction with server)
	private BufferedReader in;				// from client
	private PrintWriter out;				// to client

	public ChatServerCommunicator(Socket sock, ChatServer server) {
		this.sock = sock;
		this.server = server;
	}

	public void run() {
		try {
			System.out.println("someone connected");
			
			// Communication channel
			in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			out = new PrintWriter(sock.getOutputStream(), true);

			// Identify -- first message is the name
			name = in.readLine();
			System.out.println("it's "+name);
			out.println("welcome "+name);
			server.broadcast(this, name + " entered the room");

			// Chat away
			String line;
			while ((line = in.readLine()) != null) {
				String msg = name + ":" + line;
				System.out.println(msg);
				server.broadcast(this, msg);
			}

			// Done
			System.out.println(name + " hung up");
			server.broadcast(this, name + " left the room");

			// Clean up -- note that also remove self from server's list of handlers so it doesn't broadcast here
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
	 * Sends a message to the client
	 * @param msg
	 */
	public void send(String msg) {
		out.println(msg);
	}
}

