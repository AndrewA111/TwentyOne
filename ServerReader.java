import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ServerReader implements Runnable{
	
	private Socket socket;
	private Model model;
	private Player player;
	private Table table;
	
	/*
	 * Object to synchronize on and use to notify gameloop 
	 * when a player has selected draw or stand
	 */
	private Object drawStandNotifier;
	
	public ServerReader(Socket s, Model m, Player p, Object drawStandNotifier) {
		this.socket = s;
		this.model = m;
		this.player = p;
		this.table = this.model.getTable();
		this.drawStandNotifier = drawStandNotifier;
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
					
					// send update to clients
					this.table.sendUpdate();
				}
				if(messageIn.getCode() == 2) {
					if(this.player.isAbleToChangeStake()) {
						this.player.stakeDown();
					}
					
					// send update to clients
					this.table.sendUpdate();
					
				}
				/*
				 * Draw
				 */
				if(messageIn.getCode() == 3) {
					if(this.player.isAbleToDrawOrStand()) {
						this.player.setDrawOrStand(1);;
					}
					
					synchronized(this.drawStandNotifier) {
						this.drawStandNotifier.notifyAll();
					}
				}
				/*
				 * Stand
				 */
				if(messageIn.getCode() == 4) {
					if(this.player.isAbleToDrawOrStand()) {
						this.player.setDrawOrStand(2);
					}
					
					synchronized(this.drawStandNotifier) {
						this.drawStandNotifier.notifyAll();
					}
				}
				
				if(messageIn.getCode() == 5) {

					this.model.getTable().addPlayer(player);
					
					// if full don't allow players to join
					/*
					 * ! check this is enforced on server
					 */
					if(this.model.getTable().getNoPlayers() >= 5) {
						for(Player p : this.model.getGlobalPlayers()) {
							p.setAbleToJoin(false);
						}
					}
					
					// send update to clients
					this.table.sendUpdate();

				}
				
				if(messageIn.getCode() == 6) {

					this.model.getTable().removePlayer(this.player.getTablePos());
					
					/*
					 * make sure new players can join
					 */
					for(Player p : this.model.getGlobalPlayers()) {
						
						// select only players not currently sitting at table
						if(p.getTablePos() == -1) {
							p.setAbleToJoin(true);
						}
						
					}
					
					// send update to clients
					this.table.sendUpdate();

				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
