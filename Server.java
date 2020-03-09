import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	private static int PORT = 8765;
	
	private static Model model;
	private static Table table;
	private static Deck deck;
	
	
	
	private static class ClientThread extends Thread {
		
		private Socket socket;

		
		public ClientThread(Socket s) {
			this.socket = s;
			
			/*
			 * Assign player to first empty seat
			 */
			for(int i = 0; i < table.getPlayers().length; i++) {
				if(table.getPlayers()[i] == null) {
					
					/*
					 * Autogenerate player names for now !!
					 */
					String playerName = "Player" + (i+1);
					
					/*
					 * ID logic need updating !!
					 */
					table.getPlayers()[i] = new Player(i, playerName, i);

					break;
				}
			}

		}
		
		public void run() {
			
			try {
				
				/*
				 * Input and output streams
				 */
				ObjectOutputStream os = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream is = new ObjectInputStream(socket.getInputStream());
				
				
				while(true) {
					System.out.println(Thread.currentThread().getName());
					os.writeObject(new Message(1, table.getPlayers()));
					
					/*
					 * !! Flush and reset
					 * Makes sure that up to date players array sent
					 * as opposed previously serialized version 
					 */
					os.flush();
					os.reset();
					
					for(Player player : table.getPlayers()) {
						System.out.println(player);
					}
					System.out.println(table);
					Thread.sleep(5000);
				}
				
				
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}

	}
	
	public static void main(String[] args) {
		
		ServerSocket listener = null;
		
		model = new Model();
		table = model.getTable();
		deck = model.getDeck();
		
		try {
			/*
			 * Server
			 */
			listener = new ServerSocket(PORT);
			
			/*
			 * Continually look for clients
			 */
			while(true) {
				Socket s;
				
				/*
				 * Scan for sockets and start new thread when one found
				 */
				s = listener.accept();
				new ClientThread(s).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			
			/*
			 * Close listener
			 */
			try {
				listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}


}
