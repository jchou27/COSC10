import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * Demonstration of getting a page from www.cs.dartmouth.edu via a socket
 *
 * @author Chris Bailey-Kellogg, Dartmouth CS 10, Fall 2012
 * @author Travis Peters, Dartmouth CS 10, Winter 2015 (updated)
 * @author Tim Pierson, Dartmouth CS 10, Winter 2018 (updated PrintWriter to properly output end of line)
 * @author Tim Pierson, Dartmouth CS 10, provided for Winter 2024
 */
public class WWWSocket {
	public static void main(String[] args) throws Exception {
	    // System.in is kind of the counterpart to System.out (for user input); 
	    // A scanner obj. allows you to read input using the .nextLine() method.
		Scanner console = new Scanner(System.in);
		while (true) {
			System.out.print("what page from www.cs.dartmouth.edu? (e.g., ~tjp/cs10_<year><term>/index.php): ");
			String page = console.nextLine();

			/*
			 * OUPUT STREAM == *your* output stream (i.e., what you write to some server).
			 * INPUT STREAM == *your* input stream (i.e., what other computers (server) are writing to you).
			 */
			
			// Open a socket to WWW (port 80) and ask it to GET the page
			String host = "www.cs.dartmouth.edu";
			Socket sock = new Socket(host, 80);
			PrintWriter out = new PrintWriter(sock.getOutputStream());
			out.print("GET /"+page+" HTTP/1.1\r\n");
			out.print("Host: "+host+"\r\n\r\n");
			out.flush();


			// Read back the response from WWW on the socket
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

			// Clean up shop
			out.close();
			in.close();
			sock.close();
		}
	}
}
