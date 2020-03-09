import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	
	private static int PORT = 8765;
	private static String server = "127.0.0.1";
	
	public static void main(String[] args) {
		
		Socket socket = null;
		
		try {
			socket = new Socket(server, PORT);
			
			/*
			 * Create and start read and write threads
			 */
			Thread readThread = new Thread(new ClientReader(socket));
			Thread writeThread = new Thread(new ClientWriter(socket));
			readThread.start();
			writeThread.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
