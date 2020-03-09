import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientReader implements Runnable {
	
	private Socket socket;
	
	public ClientReader(Socket s) {
		this.socket = s;
	}
	
	public void run() {
		try {
			ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
			
			while(true) {
				Message messageIn = (Message) is.readObject();
				
				System.out.println("Message code: " + messageIn.getCode());
				
				for(Player player : messageIn.getPlayers()) {
					System.out.println(player);
				}
				
				Thread.sleep(5000);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
