import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientWriter implements Runnable {
	
	private Socket socket;
	
	public ClientWriter(Socket s) {
		this.socket = s;
	}
	
	
	public void run() {
		try {
			ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
			
			while(true) {
				
				/*
				 * For testing
				 * 
				 * Ping a message with code 2 every 2.5s
				 */
				os.writeObject(new Message(2,null));
				Thread.sleep(2500);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
