import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Client to connect to HelloServer
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Tim Pierson, Dartmouth CS 10, Winter 2018, added code to loop until server comes up
 * @author Tim Pierson, Dartmouth CS 10, provided for Winter 2024
 */
public class HelloClient {
	public static void main(String[] args) throws Exception {
		String host = "localhost"; //"localhost" or something like "129.170.212.159"
		int port = 4242;
		int connectionDelay = 5000; //in milisecs, 5000 = 5 seconds
		Scanner console = new Scanner(System.in);

		// Open the socket with the server, and then the writer and reader
		Socket sock = null;
		boolean connected = false; 
		System.out.println("connecting...");
		while (!connected) {
			try {
				//try to connect to server, throws error if server not up (which we catch)
				sock = new Socket(host,port); 
				connected = true;
			}
			catch (Exception e) {
				//server not up, wait connectionDelay/1000 seconds and try again
				System.out.println("\t server not ready, trying again in " + connectionDelay/1000 + " seconds");
				Thread.sleep(connectionDelay); //wait
			}
		}
		
		//set up input and output over socket
		PrintWriter out = new PrintWriter(sock.getOutputStream(), true);		
		BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		System.out.println("...connected");

		// Now listen and respond
		String line;
		while ((line = in.readLine()) != null) {
            // Output what you read
		    System.out.println(line);

            // Get user input from keyboard to write to the open socket (sends to server)
			String name = console.nextLine(); //blocks execution until enter is pressed
			out.println(name);
		}
		System.out.println("server hung up");

		// Clean up shop
		console.close();
		out.close();
		in.close();
		sock.close();
	}
}
