import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerWriter implements Runnable{
	
	/**
	 * Socket for client
	 */
	private Socket socket;
	
	/**
	 * Game model
	 */
	private Model model;
	
	/**
	 * Table
	 */
	private Table table;
	
	/**
	 * Player associated with client
	 */
	private Player player;
	
	
	/**
	 * COnstructor
	 * @param s socket 
	 * @param m	model
	 * @param p player
	 */
	public ServerWriter(Socket s, Model m, Player p) {
		this.socket = s;
		this.model = m;
		this.table = this.model.getTable();
		this.player = p;
	}
	
	public void run() {

			/*
			 * Output stream
			 */
			ObjectOutputStream os;
			
			
			try {
				/*
				 * Create new output stream to serialize objects 
				 * and send them to clients
				 */
				os = new ObjectOutputStream(socket.getOutputStream());
				
				while(true) {
					
					// synchronize writing to prevent concurrent modification
					synchronized(this.table) {
						
						/*
						 * write message to client
						 */
						os.writeObject(new MessageToClient(1, 
								this.player.getID(), 
								this.table.getPlayers(), 
								this.player, 
								model.getTable().getGameMessage()));

					/*
					 * Flush and reset to ensure that up-to-date players array 
					 * sent as opposed previously serialized version 
					 */
					os.flush();
					os.reset();
					
					/*
					 * Thread set to wait. 
					 * 
					 * notifyAll() is called on table when updates are made, to allow 
					 * loop to continue and game-state to be sent to client again
					 */
					this.table.wait();
					}	
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

	}

}
