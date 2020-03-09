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
			
			ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
			
			
			while(true) {
				Message messageIn = (Message) is.readObject();
				
				System.out.println("Message code: " + messageIn.getCode());
				
				for(Player player : messageIn.getPlayers()) {
					System.out.println(player);
				}
				
				Thread.sleep(5000);
			}
			
			
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
