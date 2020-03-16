import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	/*
	 * Port
	 */
	private static int PORT = 8765;
	
	/*
	 * Game objects
	 */
	private static Model model;
	private static Table table;
	private static Deck deck;
	
	
	/**
	 * Class to produce threads to communicate with clients
	 * @author Andrew
	 *
	 */
	private static class ClientThread extends Thread {
		
		/*
		 * Socket for connection
		 */
		private Socket socket;
		
		/*
		 * ID of client associated with this thread
		 */
		private int clientID;

		/*
		 * Constructor
		 */
		public ClientThread(Socket s) {
			this.socket = s;
			
			/*
			 * Assign player to first empty seat
			 */
			for(int i = 0; i < table.getPlayers().length; i++) {
				if(table.getPlayers()[i] == null) {
					
					this.clientID = i;
					
					/*
					 * Auto-generate player names for now !!
					 */
					String playerName = "Player" + (i+1);
					
					/*
					 * ID logic needs updating !!
					 */
					System.out.println(table.addPlayer(new Player(i, playerName, i), i));
//					table.getPlayers()[i] = new Player(i, playerName, i);

					break;
				}
			}

		}
		
		public void run() {
			
			/*
			 * Create and start read and write threads
			 */
			Thread readThread = new Thread(new ServerReader(this.socket, model));
			Thread writeThread = new Thread(new ServerWriter(this.socket, model, this.clientID));
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
			 * Main game logic thread
			 */
			Thread gameThread = new Thread(new GameLoopThread(model));
			gameThread.start();
			
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
