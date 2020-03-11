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

public class Client extends JFrame implements ActionListener {
	
	private static int PORT = 8765;
	private static String server = "127.0.0.1";
	
	JLabel label1;
	JLabel label2;
	JLabel label3;
	JLabel label4;
	JLabel label5;
	
	JLabel gameMessage;
	
	JButton stakeUp;
	JButton stakeDown;
	JButton dealButton;
	
	private Socket socket;
	
	private static int clientID;
	
	private ObjectOutputStream os;
	
	public Client() {
		
		/*
		 * Initialize hashmap for valuing hands
		 */
		CardDetails cardDetails = new CardDetails();
		
		/*
		 * JFrame stuff
		 */
		this.setSize(800,600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		this.setLayout(new GridLayout(6,1));
		
		label1 = new JLabel("Empty");
		this.add(label1);
		label2 = new JLabel();
		this.add(label2);
		label3 = new JLabel();
		this.add(label3);
		label4 = new JLabel();
		this.add(label4);
		label5 = new JLabel();
		this.add(label5);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1,6));
		this.add(buttonPanel);
		
		stakeUp = new JButton("Stake Up");
		stakeUp.addActionListener(this);
		buttonPanel.add(stakeUp);
		
		stakeDown = new JButton("Stake Down");
		stakeDown.addActionListener(this);
		buttonPanel.add(stakeDown);
		
		dealButton = new JButton("Deal");
		dealButton.addActionListener(this);
		buttonPanel.add(dealButton);
		
		gameMessage = new JLabel("Message space", SwingConstants.CENTER);
		buttonPanel.add(gameMessage);		
		
		
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

			gameMessage.setText(mostRecent.getGameMessage());
			
			clientID = mostRecent.getClientID();
			
			/*
			 * !! needs refactoring
			 * 
			 * Update player details according to most recent message
			 */
			if(mostRecent.getPlayers()[0] != null) {
				label1.setText(mostRecent.getPlayers()[0].toString() 
						+ mostRecent.getPlayers()[0].getHand().valuesAsString());
				
			}
			if(mostRecent.getPlayers()[1] != null) {
				label2.setText(mostRecent.getPlayers()[1].toString()
						+ mostRecent.getPlayers()[1].getHand().valuesAsString());	
			}
			if(mostRecent.getPlayers()[2] != null) {
				label3.setText(mostRecent.getPlayers()[2].toString()
						+ mostRecent.getPlayers()[2].getHand().valuesAsString());
			}
			if(mostRecent.getPlayers()[3] != null) {
				label4.setText(mostRecent.getPlayers()[3].toString()
						+ mostRecent.getPlayers()[3].getHand().valuesAsString());
			}
			if(mostRecent.getPlayers()[4] != null) {
				label5.setText(mostRecent.getPlayers()[4].toString()
						+ mostRecent.getPlayers()[4].getHand().valuesAsString());
			}
			
		}
	}
	
	public static void main(String[] args) {
		
		new Client();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == dealButton) {
			try {
				os.writeObject(new MessageToServer(3, null));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
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
		
	}
}
