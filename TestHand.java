/**
 * Text class for Hand class
 * @author Andrew
 *
 */
public class TestHand {
	public static void main(String[] args) {
		
		// Test players
		Player player1 = new Player(1, "Player1", false);
		Player player2 = new Player(2, "Player2", false);
		Player player3 = new Player(3, "Player3", false);
		
		CardDetails cardDetails = new CardDetails();
		
		/*
		 * =====================
		 * Hand with 2 aces
		 * ====================
		 */
		
		player1.addCard(new Card("10", 'S'));
		player1.addCard(new Card("A", 'S'));
		player1.addCard(new Card("2", 'D'));
		player1.addCard(new Card("A", 'S'));
		
		System.out.println("Player 1, valueAsString: " 
							+ player1.getHand().valuesAsString());
		System.out.println("Player 1, maxLegalValue: " 
							+ player1.getHand().maxLegalValue());
		System.out.println("Player 1, minLegalValue: " 
							+ player1.getHand().minLegalValue());
		
		System.out.println();
		
		/*
		 * ====================
		 * Hand with 4 aces
		 * ====================
		 */
		
		player2.addCard(new Card("A", 'S'));
		player2.addCard(new Card("A", 'C'));
		player2.addCard(new Card("A", 'H'));
		player2.addCard(new Card("A", 'D'));
		
		System.out.println("Player 2, valueAsString: " 
							+ player2.getHand().valuesAsString());
		System.out.println("Player 2, maxLegalValue: " 
							+ player2.getHand().maxLegalValue());
		System.out.println("Player 2, minLegalValue: " 
							+ player2.getHand().minLegalValue());
		
		System.out.println();
		
		/*
		 * ==========================
		 * Hand with score 30 (bust)
		 * ==========================
		 */
		player3.addCard(new Card("J", 'C'));
		player3.addCard(new Card("Q", 'H'));
		player3.addCard(new Card("K", 'D'));
		
		System.out.println("Player 3, valueAsString: " 
							+ player3.getHand().valuesAsString());
		System.out.println("Player 3, maxLegalValue: " 
							+ player3.getHand().maxLegalValue());
		System.out.println("Player 3, minLegalValue: " 
							+ player3.getHand().minLegalValue());
	}
}
