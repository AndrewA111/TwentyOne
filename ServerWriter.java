import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerWriter implements Runnable{
	
	private Socket socket;
	
	private Model model;
	
	private Table table;
	
	private Player player;
	
	private int clientID;
	
	public ServerWriter(Socket s, Model m, Player p) {
		this.socket = s;
		this.model = m;
		this.table = this.model.getTable();
		this.player = p;
	}
	
	public void run() {
		
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(socket.getOutputStream());
			
			while(true) {
//				this.model.lock();
				this.table.lock();			
				os.writeObject(new MessageToClient(1, 
													this.player.getID(), 
													this.table.getPlayers(), 
													this.player.getTablePos(), 
													model.getTable().getGameMessage()));
//				this.model.unlock();
				this.table.unlock();
				/*
				 * !! Flush and reset
				 * Makes sure that up to date players array sent
				 * as opposed previously serialized version 
				 */
				os.flush();
				os.reset();

				Thread.sleep(50);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		
		while(true) {
			
		
			
			
			
			
		}
	}

}
