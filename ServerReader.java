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
				MessageToServer messageIn = (MessageToServer) is.readObject();
				
				System.out.println("Message code: " + messageIn.getCode());
				
				if(messageIn.getCode() == 1) {
					if(this.model.getTable().getPlayers()[messageIn.getID()].isAbleToChangeStake()) {
						this.model.getTable().getPlayers()[messageIn.getID()].stakeUp();
					}
				}
				if(messageIn.getCode() == 2) {
					if(this.model.getTable().getPlayers()[messageIn.getID()].isAbleToChangeStake()) {
						this.model.getTable().getPlayers()[messageIn.getID()].stakeDown();
					}
					
				}
				/*
				 * Draw
				 */
				if(messageIn.getCode() == 3) {
					if(this.model.getTable().getPlayers()[messageIn.getID()].isAbleToDrawOrStand()) {
						this.model.getTable().getPlayers()[messageIn.getID()].setDrawOrStand(1);;
					}
				}
				/*
				 * Stand
				 */
				if(messageIn.getCode() == 4) {
					if(this.model.getTable().getPlayers()[messageIn.getID()].isAbleToDrawOrStand()) {
						this.model.getTable().getPlayers()[messageIn.getID()].setDrawOrStand(2);
					}
				}
				
				if(messageIn.getCode() == 5) {
					this.model.lock();
					this.model.getTable().getDeck().dealInitialCards(this.model.getTable().getPlayers(), this.model.getTable().getPlayers()[0]);
					this.model.unlock();
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
