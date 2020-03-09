import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerWriter implements Runnable{
	
	private Socket socket;
	
	private Model model;
	
	public ServerWriter(Socket s, Model m) {
		this.socket = s;
		this.model = m;
	}
	
	public void run() {
		
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(socket.getOutputStream());
			
			while(true) {
				System.out.println(Thread.currentThread().getName());

				os.writeObject(new Message(1, model.getTable().getPlayers()));
				/*
				 * !! Flush and reset
				 * Makes sure that up to date players array sent
				 * as opposed previously serialized version 
				 */
				os.flush();
				os.reset();
				for(Player player : model.getTable().getPlayers()) {
					System.out.println(player);
				}
				System.out.println(model.getTable());
				Thread.sleep(5000);
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
