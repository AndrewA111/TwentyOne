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

public class Client2 extends JFrame implements ActionListener {
	
	private static int PORT = 8765;
	private static String server = "127.0.0.1";
	
//	JLabel label1;
//	JLabel label2;
//	JLabel label3;
//	JLabel label4;
//	JLabel label5;
	
//	JLabel[] playerLabels;
	
	JLabel gameMessage;
	
	JButton stakeUp;
	JButton stakeDown;
	
	JButton stand;
	JButton draw;
	
	HandsPanel handsPanel;
	
	
	/*
	 * Testing only !!
	 */
	JButton dealButton;
	
	private Socket socket;
	
	private static int clientID;
	
	private ObjectOutputStream os;
	
	private Player[] players = null;
	
	public Client2() {
		
		/*
		 * Initialize hashmap for valuing hands
		 */
		CardDetails cardDetails = new CardDetails();
		
		
		
		/*
		 * JFrame stuff
		 */
		this.setSize(900,700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		this.setLayout(new BorderLayout());
		
		JPanel southPanel = new JPanel(new GridLayout(2,1));
		
		/*
		 * Buttons
		 */
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,6));
		stakeUp = new JButton("Stake Up");
		stakeUp.addActionListener(this);
		buttonPanel.add(stakeUp);
		
		stakeDown = new JButton("Stake Down");
		stakeDown.addActionListener(this);
		buttonPanel.add(stakeDown);
		
		draw = new JButton("Draw");
		draw.addActionListener(this);
		buttonPanel.add(draw);
		
		stand = new JButton("Stand");
		stand.addActionListener(this);
		buttonPanel.add(stand);
		
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
		
		this.setVisible(true);
		
		/*
		 * 
		 */
		handsPanel = new HandsPanel(this.players);
		this.add(handsPanel, BorderLayout.CENTER);
		

		/*
		 * Initialize output stream
		 * 
		 * Requests are created in actionperformed method
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
	
	public class ReadWorker extends SwingWorker<Void, MessageToClient>{
		
		private Socket s;
		private ObjectInputStream is = null;
		
		public ReadWorker(Socket s) {
			this.s = s;
			try {
				/*
				 * Initialize input stream
				 */
				this.is = new ObjectInputStream(s.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		protected Void doInBackground() throws Exception {
			while(true) {
				
				
				
				/*
				 * Read and publish messages
				 */
				MessageToClient messageIn = (MessageToClient) is.readObject();
				publish(messageIn);
			}
		}
		
		protected void process(List<MessageToClient> messages) {
			
			
			
			/*
			 * Select the most recently published message
			 */
			MessageToClient mostRecent = messages.get(messages.size() -1);
			
			int ID = mostRecent.getClientID();
			
			/*
			 * !! Review
			 */
			if(Client2.this.getTitle() != ("Player: " + (ID + 1))) {
				Client2.this.setTitle("Player: " + (ID + 1));
			}
					
			gameMessage.setText(mostRecent.getGameMessage());
			
			clientID = mostRecent.getClientID();
			
			/*
			 * Activate/deactivate buttons
			 */
			Client2.this.stakeUp.setEnabled(mostRecent.getPlayers()[ID].isAbleToChangeStake());
			Client2.this.stakeDown.setEnabled(mostRecent.getPlayers()[ID].isAbleToChangeStake());
			Client2.this.draw.setEnabled(mostRecent.getPlayers()[ID].isAbleToDrawOrStand());
			Client2.this.stand.setEnabled(mostRecent.getPlayers()[ID].isAbleToDrawOrStand());
			
			/*
			 * !! needs refactoring
			 * 
			 * Update player details according to most recent message
			 */
			
			Client2.this.handsPanel.setPlayers(mostRecent.getPlayers());
			Client2.this.handsPanel.repaint();
			

			
		}
	}
	
	public static void main(String[] args) {
		
		new Client2();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource() == stakeUp) {
			try {
				os.writeObject(new MessageToServer(1, clientID));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == stakeDown) {
			try {
				os.writeObject(new MessageToServer(2, clientID));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == draw) {
			try {
				os.writeObject(new MessageToServer(3, clientID));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if(e.getSource() == stand) {
			try {
				os.writeObject(new MessageToServer(4, clientID));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
//		if(e.getSource() == dealButton) {
//			try {
//				os.writeObject(new MessageToServer(5, null));
//			} catch (IOException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}
		
	}
}
