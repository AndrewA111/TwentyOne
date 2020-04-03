import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Class to create thread to read messages from client
 * @author Andrew
 *
 */
public class ServerReader implements Runnable{
	
	/**
	 * Socket for client
	 */
	private Socket socket;
	
	/**
	 * Game model
	 */
	private Model model;
	
	/**
	 * Player associated with client
	 */
	private Player player;
	
	/**
	 * Table
	 */
	private Table table;
	
	/*
	 * Object to synchronize on and use to notify game-loop 
	 * when a player has selected draw or stand
	 */
	private Object drawStandNotifier;
	
	/**
	 * Constructor
	 * @param s socket
	 * @param m model
	 * @param p player
	 * @param drawStandNotifier
	 */
	public ServerReader(Socket s, Model m, Player p, Object drawStandNotifier) {
		this.socket = s;
		this.model = m;
		this.player = p;
		this.table = this.model.getTable();
		this.drawStandNotifier = drawStandNotifier;
	}
	
	public void run() {
		
		try {
			
			/*
			 * Create input stream to read incoming serialized messages
			 */
			ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
			
			while(true) {
				
				// read incoming message
				MessageToServer messageIn = (MessageToServer) is.readObject();
				System.out.println("Message code: " + messageIn.getCode());
				
				/*
				 * Message code 1 - Increase stake
				 */
				if(messageIn.getCode() == 1) {
					
					/*
					 * Synchronized to prevent last-minute 
					 * state change
					 */
					synchronized(this.table) {
						
						if(this.player.isAbleToChangeStake()) {
							this.player.stakeUp();
						}
						
					}
	
					// send update to clients
					this.table.sendUpdate();
				}
				/*
				 * Message code 2 - Decrease stake
				 */
				if(messageIn.getCode() == 2) {
					
					/*
					 * Synchronized to prevent last-minute 
					 * state change
					 */
					synchronized(this.table){
						
						if(this.player.isAbleToChangeStake()) {
							this.player.stakeDown();
						}
					}
					
					
					// send update to clients
					this.table.sendUpdate();
					
				}
				/*
				 * Message code 3 - Draw card
				 */
				if(messageIn.getCode() == 3) {
					
					/*
					 * Synchronized to prevent last-minute 
					 * state change
					 */
					synchronized(this.table) {
						
						if(this.player.isAbleToDrawOrStand()) {
							this.player.setDrawOrStand(1);;
						}
					}
					
					
					synchronized(this.drawStandNotifier) {
						this.drawStandNotifier.notifyAll();
					}
				}
				/*
				 * Message code 4 - Stand (end turn)
				 */
				if(messageIn.getCode() == 4) {
					
					/*
					 * Synchronized to prevent last-minute 
					 * state change
					 */
					synchronized(this.table) {
						
						if(this.player.isAbleToDrawOrStand()) {
							this.player.setDrawOrStand(2);
						}
					}
					
					
					synchronized(this.drawStandNotifier) {
						this.drawStandNotifier.notifyAll();
					}
				}
				
				/*
				 * Message code 5 - Join table
				 */
				if(messageIn.getCode() == 5) {
					
					/*
					 *  synchronize to avoid race between multiple 
					 *  clients to take final position and last-minute joining
					 */
					synchronized(this.table) {
						
						// boolean to check whether player added
						boolean added = false;
						
						// join if joining allowed
						if(this.player.isAbleToJoin()) {
							added = this.model.getTable().addPlayer(player);
						}
						
						/* 
						 * players can only join between rounds, 
						 * so allow stake changing
						 */
						if(added) {
							this.player.setAbleToChangeStake(true);
						}
						
						/* 
						 * if table now full don't allow players to join
						 */
						if(this.model.getTable().getNoPlayers() >= 5) {
							for(Player p : this.model.getGlobalPlayers()) {
								p.setAbleToJoin(false);
							}
						}
						
						// send update to clients
						this.table.sendUpdate();
					}
					

				}
				
				/*
				 * Message code 6 - Leave table
				 */
				if(messageIn.getCode() == 6) {
					
					/*
					 *  synchronize to avoid last-minute leaving
					 */
					synchronized(this.table) {
						
						// leave if leaving allowed
						if(this.player.isAbleToLeave()) {
							this.model.getTable().removePlayer(this.player.getTablePos());
						}
						
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
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
