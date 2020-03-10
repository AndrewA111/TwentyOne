import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReader implements Runnable{
	
	private Socket socket;
	private Model model;
	
	public ServerReader(Socket s, Model m) {
		this.socket = s;
		this.model = m;
	}
	
	public void run() {
		
		try {
			ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
			
			while(true) {
				Message messageIn = (Message) is.readObject();
				
				System.out.println("Message code: " + messageIn.getCode());
				
				
				if(messageIn.getCode() == 2) {
					
				}
				
				if(messageIn.getCode() == 3) {
					this.model.getTable().getDeck().dealInitialCards(this.model.getTable().getPlayers(), this.model.getTable().getPlayers()[0]);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
