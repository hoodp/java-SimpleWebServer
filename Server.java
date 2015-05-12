import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Paul Hood
 * Assigment 7
 */
public class Server {

	public static void handleConnection(Socket socket) throws IOException {

		// Created a BufferedReader that can read from the socket
		BufferedReader in = new BufferedReader(new InputStreamReader(
				socket.getInputStream()));

		// Create a PrintStream than can write to the socket
		// Passing "true" as the second parameter causes each write to be
		// followed by a flush.
		PrintStream out = new PrintStream(socket.getOutputStream(), true);

		// While there is in, read it in, and simply echo it back
		String line;
		line = in.readLine();

		// get requested file
		File file = new File(line.split(" ")[1]);
		
		// check if file exists
		if (!file.exists()) {

			// error message to output 
			String errorMessage = "400 NOT FOUND";
			out.printf("HTTP/1.1 %s\n", errorMessage);
			out.println("Content-Type: text/plain");
			out.printf("Content-Length: %d\n", errorMessage.length());
			out.println("Connection: close");
			out.println("\n400 NOT FOUND");
			out.println("");
		} else {

			// convert file name back to string
			String fileName = file.getName();
			
			// input stream for reading file
			InputStream fileIn;
			
			// start checking for txt or html files
			if (fileName.indexOf("txt") > 0 || fileName.indexOf("html") > 0) {
				
				// setup input stream for displaying file
				fileIn = new FileInputStream(file);
				
				// set buffer to size of the file
				byte[] buffer = new byte[fileIn.available()];
				
				// get string type 
				String type = fileName.indexOf("txt") > 0 ? "plain" : "html";
				out.println("HTTP/1.1 200 OK");
				out.println("Content-Type: text/" + type);
				out.println("Content-Length: " + fileIn.available());
				out.println("Connection: close\n");
				
				// write file based on size of buffer
				out.write(buffer, 0, fileIn.read(buffer));
				out.println("");
			}
			
			// start checking for images
			else if (fileName.indexOf("jpg") > 0) {
				
				// setup input stream for displaying image
				fileIn = new FileInputStream(file);
				
				// set buffer to size of the image
				byte[] buffer = new byte[fileIn.available()];
				
				out.println("HTTP/1.1 200 OK");
				out.println("Content-Type: image/jpg");
				out.println("Content-Length: " + fileIn.available());
				out.println("Connection: close\n");
				
				// start writing output
				out.write(buffer, 0, fileIn.read(buffer));
				out.println("");
			}
		}
		while (line != null) {

			// check for empty line 
			if (line.toLowerCase().trim().equals("")) {
				socket.close();
				break;
			}

			// print header
			System.out.printf("%s\n", line);

			// get next header
			line = in.readLine();
		}
		System.out.println("");
	}

	public static void main(String[] args) throws IOException {

		// Create a socket that listens on port 8354.handleConnection
		ServerSocket serverSocket = new ServerSocket(8356);

		// go to next connection 
		while (true)
			handleConnection(serverSocket.accept());
	}
}
