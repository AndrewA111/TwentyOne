import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReader implements Runnable{
	
	private Socket socket;
	private Model model;
	private Player player;
	
	public ServerReader(Socket s, Model m, Player p) {
		this.socket = s;
		this.model = m;
		this.player = p;
	}
	
	public void run() {
		
		try {
			ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
			
			while(true) {
				MessageToServer messageIn = (MessageToServer) is.readObject();
				
				int pos = this.player.getTablePos();
				
				System.out.println("Message code: " + messageIn.getCode());
				
				if(messageIn.getCode() == 1) {
					if(this.player.isAbleToChangeStake()) {
						this.player.stakeUp();
					}
				}
				if(messageIn.getCode() == 2) {
					if(this.player.isAbleToChangeStake()) {
						this.player.stakeDown();
					}
					
				}
				/*
				 * Draw
				 */
				if(messageIn.getCode() == 3) {
					if(this.player.isAbleToDrawOrStand()) {
						this.player.setDrawOrStand(1);;
					}
				}
				/*
				 * Stand
				 */
				if(messageIn.getCode() == 4) {
					if(this.player.isAbleToDrawOrStand()) {
						this.player.setDrawOrStand(2);
					}
				}
				
				if(messageIn.getCode() == 5) {

					this.model.getTable().addPlayer(player);

				}
				
				if(messageIn.getCode() == 6) {

					this.model.getTable().removePlayer(this.player.getTablePos());

				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
