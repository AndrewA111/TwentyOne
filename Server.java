import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server class for TwentyOne game
 * @author Andrew
 *
 */
public class Server {
	
	/**
	 * Port
	 */
	private static int PORT = 8765;
	
	/**
	 * Model
	 */
	private static Model model;
	
	/**
	 * Table
	 */
	private static Table table;
	
	/**
	 * !
	 */
	private static Deck deck;
	
	/**
	 * Object to synchronize on and use to notify gameloop 
	 * when a player has selected draw or stand
	 */
	private static Object drawStandNotifier;
	
	/**
	 * Integer to represent the greatest 
	 * ID value given out so far
	 */
	private static int globalID = 0;
	
	/**
	 * Class to produce threads to communicate with clients
	 * @author Andrew
	 *
	 */
	private static class ClientThread extends Thread {
		
		/**
		 * Socket for connection
		 */
		private Socket socket;
		
		/**
		 * ID of client associated with this thread
		 */
		private int clientID;
		
		/**
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
			 * ! Auto-generate player names
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
			 * Create and start read and write threads for this client
			 */
			Thread readThread = new Thread(new ServerReader(this.socket, 
											model,
											this.player,
											drawStandNotifier));
			
			Thread writeThread = new Thread(new ServerWriter(this.socket, 
											model, 
											this.player));
			readThread.start();
			writeThread.start();
			
		}

	}
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		
		/**
		 * ServerSocket to listen for clients
		 */
		ServerSocket listener = null;
		
		/*
		 * Set model
		 */
		model = new Model();
		
		/*
		 * Set table to model's table
		 */
		table = model.getTable();
		
		/*
		 * !
		 */
		deck = model.getDeck();
		
		/*
		 * Initialise object to synchronise on to 
		 * control draw/stand request communication
		 */
		drawStandNotifier = new Object();
		
		try {
			/*
			 * Server
			 */
			listener = new ServerSocket(PORT);
			
			/*
			 * Game logic thread
			 */
			Thread gameThread = new Thread(new GameLoopThread(model, drawStandNotifier));
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
				
				/*
				 * Start a ClientThread thread for each client found
				 */
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
