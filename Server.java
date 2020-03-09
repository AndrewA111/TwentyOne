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
					 * ID logic needs updating !!
					 */
					table.getPlayers()[i] = new Player(i, playerName, i);

					break;
				}
			}

		}
		
		public void run() {
			
			/*
			 * Create and start read and write threads
			 */
			Thread readThread = new Thread(new ServerReader(this.socket, model));
			Thread writeThread = new Thread(new ServerWriter(this.socket, model));
			readThread.start();
			writeThread.start();
			
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
