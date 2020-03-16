import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerWriter implements Runnable{
	
	private Socket socket;
	
	private Model model;
	
	private Table table;
	
	private int clientID;
	
	public ServerWriter(Socket s, Model m, int clientID) {
		this.socket = s;
		this.model = m;
		this.table = this.model.getTable();
		this.clientID = clientID;
	}
	
	public void run() {
		
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(socket.getOutputStream());
			
			while(true) {
//				this.model.lock();
				this.table.lock();			
				os.writeObject(new MessageToClient(1, this.clientID, this.table.getPlayers(), model.getTable().getGameMessage()));
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
