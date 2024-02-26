import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;

/**
 * A helper class provided for Dartmouth's CS 10 -- allows you to easily get your 
 * IP address as it is inside/outside your current network.
 * 
 * @author Travis Peters, Dartmouth CS 10, Winter 2015
 * @author Tim Pierson, Dartmouth CS 10, Fall 2022 - updated local address
 *          based on https://stackoverflow.com/questions/9481865/getting-the-ip-address-of-the-current-machine-using-java
 *              answer from "a problem solver" on Jan 24, 2017
 *@author Tim Pierson, Dartmouth CS 10, provided for Winter 2024
 */
public class MyIPAddressHelper {

    /**
     * Get your machine's IP address as it is INSIDE of your current network (e.g., Dartmouth's network).
     * Not super robust...
     */
    public static String getMyLocalIP() {
        //InetAddress localHost;
        try {
            // Get your machine's local IP address.
            //return Inet4Address.getLocalHost().getHostAddress();
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("google.com", 80));
            String ip = socket.getLocalAddress().toString();
            socket.close();
            return ip.replace("/","");
        } catch (Exception e) {
            // do nothing...
        }
        return "unable to get local IP address.";
    }
    
    /**
     * Get your machine's IP address as it is OUTSIDE of my current network (e.g., Dartmouth's network).
     */
    public static String getMyGlobalIP() {
        try {
            URL whatismyip;
            whatismyip = new URL("http://checkip.amazonaws.com/");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine(); // Get the IP address as a String
            in.close();
            return ip;
        } catch (Exception e) {
            // do nothing...
        }
        return "unable to get global IP address.";
    }
    
    public static void main(String[] args) {
        System.out.println("What is my IP Address INSIDE of my current network : " + getMyLocalIP());
        System.out.println("What is my IP Address OUTSIDE of my current network: " + getMyGlobalIP());
    }
    
}