import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A simple echo server to demonstrate how to set up a server.
 * This server handles one connection then terminates.
 * Created by kurmasz on 5/4/15.
 */
public class Server {

	public static void handleConnection(Socket socket) throws IOException {

		// Created a BufferedReader that can read from the socket
		BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		// Create a PrintStream than can write to the socket
		// Passing "true" as the second parameter causes each write to be followed by a flush.
		PrintStream output = new PrintStream(socket.getOutputStream(), true);

		// While there is input, read it in, and simply echo it back
		String line;
		line = input.readLine();
		output.println("HTTP/1.1 200 OK");
		output.println("Content-Type: text/plain");
		output.println("Content-Length: 70");
		output.println("Connection: close");
		output.println("\nThis is not the real content because this server"
				+ " is not yet complete.");
		output.println("");
		while (line != null) {
			if (line.toLowerCase().trim().equals("")) {
				socket.close();
				break;
			}
			line = input.readLine();
		}
	}
	
	public static void main(String[] args) throws IOException {
		
		// Create a socket that listens on port 8354.handleConnection
		ServerSocket serverSocket = new ServerSocket(7777);
		
		// go to next connection 
		while (true)
			handleConnection(serverSocket.accept());
	}
}
