import javax.swing.JFrame;

/**
 * Test class for TablePanel
 * @author Andrew
 *
 */
public class TestFrame extends JFrame{
	
	/**
	 * Constructor
	 */
	public TestFrame() {
		
		// Setup JFrame
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(900, 700);
		
		// create players array and create Table Panel
		Player[] pArray = createPlayersArray();
		TablePanel testPanel = new TablePanel(pArray, pArray[0]);
		this.add(testPanel);
		
		// set JFrame visible
		this.setVisible(true);
		
		// set initial panel dims (for scaling)
		testPanel.initialDims();
	}

		
	/**
	 * Method to create a players array for testing	
	 * @return players array
	 */
	public Player[] createPlayersArray() {
		
		Player[] p = new Player[5];
		Player player1 = new Player(1, "Andrew", false);
		Player player2 = new Player(2, "John", false);
		Player player3 = new Player(3, "Aaron", false);
		Player player4 = new Player(4, "Rebecca", false);
		Player player5 = new Player(5, "Mike", false);
		
		player1.setDealer(true);
		
		p[0] = player1;
		p[1] = player2;
		p[2] = player3;
		p[3] = player4;
		p[4] = player5;
		
		p[0].addCard(new Card("A", 'S'));
		p[0].addCard(new Card("K", 'S'));
//		p[0].addCard(new Card("A", 'S'));
//		p[0].addCard(new Card("K", 'S'));
//		p[0].addCard(new Card("A", 'S'));
//		p[0].addCard(new Card("K", 'S'));
//		p[0].addCard(new Card("A", 'S'));
//		p[0].addCard(new Card("K", 'S'));
//		p[0].addCard(new Card("A", 'S'));
//		p[0].addCard(new Card("K", 'S'));
//		p[0].addCard(new Card("A", 'S'));
//		p[0].addCard(new Card("K", 'S'));
		p[1].addCard(new Card("8", 'D'));
		p[1].addCard(new Card("9", 'C'));
//		p[2].addCard(new Card("2", 'S'));
//		p[2].addCard(new Card("5", 'H'));
//		p[2].addCard(new Card("2", 'S'));
//		p[2].addCard(new Card("5", 'H'));
//		p[2].addCard(new Card("2", 'S'));
//		p[2].addCard(new Card("5", 'H'));
//		p[2].addCard(new Card("2", 'S'));
//		p[2].addCard(new Card("5", 'H'));
//		p[2].addCard(new Card("2", 'S'));
//		p[2].addCard(new Card("5", 'H'));
//		p[2].addCard(new Card("2", 'S'));
//		p[2].addCard(new Card("5", 'H'));
		p[2].addCard(new Card("2", 'S'));
		p[2].addCard(new Card("5", 'H'));
		p[3].addCard(new Card("7", 'D'));
		p[3].addCard(new Card("3", 'D'));
		p[4].addCard(new Card("J", 'C'));
		p[4].addCard(new Card("Q", 'H'));
		
		return p;
	}
		
		

	/**
	 * Main method to run test class
	 * @param args
	 */
	public static void main(String[] args) {
		JFrame TestFrame = new TestFrame();
	}
	
}
