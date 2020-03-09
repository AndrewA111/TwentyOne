import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReader implements Runnable{
	
	private Socket socket;
	
	public ServerReader(Socket s) {
		this.socket = s;
	}
	
	public void run() {
		
		try {
			ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
			
			while(true) {
				Message messageIn = (Message) is.readObject();
				
				System.out.println("Message code: " + messageIn.getCode());
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
