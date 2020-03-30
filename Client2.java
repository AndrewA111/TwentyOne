import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;

/**
 * Main class to create client program
 * 
 * Sets up main JFrame, with buttons.
 * 
 * Reads server messages using SwingWorker inner class 
 * and sends responses via actionPerformed method
 * 
 * @author Andrew
 *
 */
public class Client2 extends JFrame implements ActionListener {
	
	/**
	 * Port number
	 */
	private static int PORT = 8765;
	
	/**
	 * IP address
	 */
	private static String server = "127.0.0.1";
	
	/**
	 * Game message to display
	 */
	JLabel gameMessage;
	
	/**
	 * Button to raise stake
	 */
	JButton stakeUp;
	
	/**
	 * Button to lower stake
	 */
	JButton stakeDown;
	
	/**
	 * Button to indicate player wants to 'stand'
	 * (finish turn)
	 */
	JButton stand;
	
	/**
	 * Button to indicate player wants to 
	 * draw a new card
	 */
	JButton draw;
	
	/**
	 * Button to indicate player wants to join table
	 */
	JButton join;
	
	/**
	 * Button to indicate player wants to leave table
	 */
	JButton leave;
	
	/**
	 * Panel to visually display game state
	 */
	TablePanel handsPanel;

	/**
	 * Socket associated with this client
	 */
	private Socket socket;
	
	/**
	 * ID for this client
	 */
	private static int clientID;
	
	/**
	 * Output stream to write requests to server
	 */
	private ObjectOutputStream os;
	
	/**
	 * Reference to store players array sent from server
	 */
	private Player[] players = null;
	
	/**
	 * Constructor
	 */
	public Client2() {
		
		/*
		 * Initialize hashmap for valuing hands
		 */
		CardDetails cardDetails = new CardDetails();
		
		/*
		 * Setup JFrame
		 */
		this.setSize(900,700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new BorderLayout());
		
		/*
		 * JPanel to put buttons (top row) 
		 * and gameMessage (bottom row)
		 */
		JPanel southPanel = new JPanel(new GridLayout(2,1));
		
		/*
		 * Panel to put buttons
		 */
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,6));
		
		/*
		 * Add buttons
		 */
		
		// Increase stake
		stakeUp = new JButton("Stake Up");
		stakeUp.addActionListener(this);
		buttonPanel.add(stakeUp);
		
		// Decrease stake
		stakeDown = new JButton("Stake Down");
		stakeDown.addActionListener(this);
		buttonPanel.add(stakeDown);
		
		// Draw a new card
		draw = new JButton("Draw");
		draw.addActionListener(this);
		buttonPanel.add(draw);
		
		// Stand (end turn)
		stand = new JButton("Stand");
		stand.addActionListener(this);
		buttonPanel.add(stand);
		
		// Join table
		join = new JButton("Join");
		join.addActionListener(this);
		buttonPanel.add(join);
		
		// Leave table
		leave = new JButton("Leave");
		leave.addActionListener(this);
		buttonPanel.add(leave);
		
		// Add button panel
		southPanel.add(buttonPanel);
		
		/*
		 * Game Message
		 */
		gameMessage = new JLabel("Message space", SwingConstants.CENTER);
		southPanel.add(gameMessage);
		
		/*
		 * Add southPanel to JFrame
		 */
		this.add(southPanel, BorderLayout.SOUTH);
		
		
		
		/*
		 * Create panel to display game-state (Cards, stakes etc.)
		 * 
		 * Add to centre of Jframe
		 */
		handsPanel = new TablePanel(this.players);
		this.add(handsPanel, BorderLayout.CENTER);
		
		/*
		 * Set JFrame visible
		 */
		this.setVisible(true);
		
		/*
		 * Set display initial dims (for scaling window)
		 */
		handsPanel.initialDims();
		
		/*
		 * Initialise output stream to send requests
		 * 
		 * (Requests sent from actionPerformed method in this class)
		 */
		try {
			this.socket = new Socket(server, PORT);
			os = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * Start reader thread
		 */
		ReadWorker rw = new ReadWorker(socket);
		rw.execute();
		
		

	}
	
	/**
	 * Reader SwingWorker to receive updates from server
	 * @author Andrew
	 *
	 */
	public class ReadWorker extends SwingWorker<Void, MessageToClient>{
		
		/**
		 * Socket associated with this client
		 */
		private Socket s;
		
		/**
		 * Input stream for messages
		 */
		private ObjectInputStream is = null;
		
		/**
		 * Constructor
		 * @param s socket associated with this client
		 */
		public ReadWorker(Socket s) {
			this.s = s;
			try {
				// Initialise input stream
				this.is = new ObjectInputStream(s.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		/**
		 * Read messages in background
		 */
		protected Void doInBackground() throws Exception {
			while(true) {

				/*
				 * Read and publish messages
				 */
				MessageToClient messageIn = (MessageToClient) is.readObject();
				publish(messageIn);
			}
		}
		
		/**
		 * Update game state based on most recent message received from server
		 */
		protected void process(List<MessageToClient> messages) {
			
			/*
			 * Select the most recently published message
			 */
			MessageToClient mostRecent = messages.get(messages.size() -1);
			
			/*
			 * Get ID of this client
			 */
			int ID = mostRecent.getClientID();
			

			/*
			 * ! Set title to players ID
			 */
			if(Client2.this.getTitle() != ("GameID: " + (ID))) {
				Client2.this.setTitle("GameID: " + (ID));
			}
			
			/*
			 * Update game message
			 */
			gameMessage.setText(mostRecent.getGameMessage());
			
			clientID = mostRecent.getClientID();
			
			/*
			 * Activate/deactivate buttons using flags on player
			 */
			Client2.this.join.setEnabled(mostRecent.getPlayer().isAbleToJoin());
			Client2.this.leave.setEnabled(mostRecent.getPlayer().isAbleToLeave());
			Client2.this.stakeUp.setEnabled(mostRecent.getPlayer().isAbleToChangeStake());
			Client2.this.stakeDown.setEnabled(mostRecent.getPlayer().isAbleToChangeStake());
			Client2.this.draw.setEnabled(mostRecent.getPlayer().isAbleToDrawOrStand());
			Client2.this.stand.setEnabled(mostRecent.getPlayer().isAbleToDrawOrStand());
			
			/*
			 * Update player details according to most recent message
			 */
			if(mostRecent.getPlayers() != null) {
				Client2.this.handsPanel.setPlayers(mostRecent.getPlayers());
			}
			
			// force panel repaint
			Client2.this.handsPanel.repaint();
			
			// force buttons/text area to refresh
			Client2.this.revalidate(); 

	
		}
	}

	@Override
	/**
	 * Action performed method. 
	 * 
	 * Waits for buttons to be pressed then sends 
	 * appropriate message to the server 
	 */
	public void actionPerformed(ActionEvent e) {
		
		// Increase stake
		if(e.getSource() == stakeUp) {
			try {
				os.writeObject(new MessageToServer(1, clientID));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// Decrease stake
		if(e.getSource() == stakeDown) {
			try {
				os.writeObject(new MessageToServer(2, clientID));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// Draw a new card
		if(e.getSource() == draw) {
			try {
				os.writeObject(new MessageToServer(3, clientID));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// Stand (end turn)
		if(e.getSource() == stand) {
			try {
				os.writeObject(new MessageToServer(4, clientID));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// Join table
		if(e.getSource() == join) {
			try {
				os.writeObject(new MessageToServer(5, clientID));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		// Leave table
		if(e.getSource() == leave) {
			try {
				os.writeObject(new MessageToServer(6, clientID));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		
	}
	
	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Create main JFrame
		new Client2();
	}
}
