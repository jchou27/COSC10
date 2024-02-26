import java.io.*;
import java.net.*;

/**
 * Handles communication with one client for HelloMultiThreadedServer
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised 2014 to split out HelloServerCommunicator
 * @author Tim Pierson, Dartmouth CS 10, provided for Winter 2024
 */
public class HelloServerCommunicator extends Thread {
	private Socket sock = null;		// to talk with client
	private int id;					// for marking the messages (just for clarity in reading console)

	public HelloServerCommunicator(Socket sock, int id) {
		this.sock = sock;
		this.id = id;
	}

	/**
	 * The body of the thread is basically the same as what we had in main() of the single-threaded version
	 */
	public void run() {
		// Smother any exceptions, to match the signature of Thread.run()
		try {
			System.out.println("#" + id + " connected");
			
			// Communication channel
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
			
			// Talk
			out.println("who is it?");
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println("#" + id + " received:" + line);
				out.println("hi " + line + "!  anybody else there?");
			}
			System.out.println("#" + id + " hung up");

			// Clean up
			out.close();
			in.close();
			sock.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}

