import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple server that waits for someone to connect on port 4242,
 * and then repeatedly asks for their name and greets them.
 * Connect either by "telnet localhost 4242" or by running HelloClient.java
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Tim Pierson, Dartmouth CS 10, provided for Winter 2024
 */

public class HelloServerPD8 {
	public static void main(String[] args) throws IOException {
		// Listen on a server socket for a connection
		System.out.println("waiting for someone to connect");
		ServerSocket listen = new ServerSocket(4242);
		// When someone connects, create a specific socket for them
		Socket sock = listen.accept();
		System.out.println("someone connected");
		Map<String, String> dictionary = new HashMap<>();

		// Now talk with them
		PrintWriter out = new PrintWriter(sock.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out.println("What word would you like to send?");
		String line;
		while ((line = in.readLine()) != null) {
			String[] parts = line.split(" ", 3);
			String command = parts[0];
			String word = parts[1];

			if ("GET".equals(command)) {
				String definition = dictionary.get(word);
				if (definition == null) {
					out.println("Word not found");
				} else {
					out.println(definition);
				}
			} else if ("SET".equals(command)) {
				String definition = parts[2];
				dictionary.put(word, definition);
				out.println("Word added");
			} else {
				out.println("Invalid command");
			}
		}
		System.out.println("client hung up");

		// Clean up shop
		out.close();
		in.close();
		sock.close();
		listen.close();
	}
}


















