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
	
	/*
	 * 
	 */
	private static int globalID = 0;
	
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
		 * Object reference to track player associated with this thread
		 */
		private Player player;
		
		/*
		 * Constructor
		 */
		public ClientThread(Socket s) {
			this.socket = s;
			

			// add next available ID		
			this.clientID = Server.globalID;
			
			
		
			
			// increment global ID
			Server.globalID++;
			
			/*
			 * Auto-generate player names for now !!
			 */
			String playerName = "Player" + (this.clientID);
			
			/*
			 * Create player
			 */
			this.player = new Player(clientID, playerName);
			
			/*
			 * Add player to model
			 */
			model.addPlayer(this.player);


			

		}
		
		public void run() {
			
			/*
			 * Create and start read and write threads
			 */
			Thread readThread = new Thread(new ServerReader(this.socket, model));
			Thread writeThread = new Thread(new ServerWriter(this.socket, 
											model, 
											this.player));
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
