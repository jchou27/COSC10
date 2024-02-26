import java.net.*;
import java.io.*;

/**
 * Multithreaded version of the hello server
 * Now lets multiple someones connect on port 4242; for each one,
 * it repeatedly asks for their name and greets them.
 * Connect either by "telnet localhost 4242" or by running HelloClient.java
 * (multiple times)
 * 
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012; revised 2014 to split out HelloServerCommunicator
 * @author Tim Pierson, Dartmouth CS 10, provided for Winter 2024
 */

public class HelloMultithreadedServer {	
	private ServerSocket listen;	// where clients initially connect

	public HelloMultithreadedServer(ServerSocket listen) {
		this.listen = listen;
	}

	/**
	 * Listens to listen and fires off new communicators to handle the clients
	 */
	public void getConnections() throws IOException {
		System.out.println("waiting for someone to connect");

		// Just keep accepting connections and firing off new threads to handle them.
		int num = 0;
		while (true) {
			//listen.accept in next line blocks until a connection is made
			HelloServerCommunicator client = new HelloServerCommunicator(listen.accept(), num++);
			client.setDaemon(true); // handler thread terminates when main thread does
			client.start(); //start new thread running
		}

	}

	public static void main(String[] args) throws IOException {
		new HelloMultithreadedServer(new ServerSocket(4242)).getConnections();
	}
}



















