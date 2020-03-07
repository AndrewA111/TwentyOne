import java.util.Iterator;

public class Test {
	
	/*
	 * TODO:
	 * 
	 * What happens when not enough cards to deal out?
	 * 
	 * Need to think about removing cards from deck when dealt
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	
	
	public static void main(String[] args) {
		
		/*
		 * Create CardDetails object.
		 * 
		 * This calls the class constructor which populates the card value hashmap
		 */
		CardDetails cardDetails = new CardDetails();
		
		/*
		 * Create deck
		 */
		Deck deck = new Deck();
		
		Player player1 = new Player(1, "Andrew");
		Player player2 = new Player(2, "John");
		Player player3 = new Player(3, "Aaron");
//		Player player4 = new Player(4, "Rebecca");
		Player player5 = new Player(5, "Mike");
		
		/*
		 * Create table and add players
		 */
		Table table = new Table();
		
		table.addPlayer(player1, 0);
		table.addPlayer(player2, 1);
		table.addPlayer(player3, 2);
//		table.addPlayer(player4, 3);
		table.addPlayer(player5, 4);
		
		
		/*
		 * Deal
		 */
		table.dealAll(deck);
		
		System.out.println(table);
		
		System.out.println("Original deck:\n" + deck);
		
		/*
		 * Shuffle
		 */
		deck.shuffle();
		
		System.out.println("\nShuffled deck:\n" + deck);
		
		
		/*
		 * Check numerical value matching working
		 */
		System.out.println("\nCheck numerical value matches card:");
		for(Card card : deck.getDeck()) {
			
			System.out.print(card.getType() + card.getValue() + ": ");
			
			// if within 2,3 ... King
			if(!card.getValue().equals("A")) {
				System.out.print(CardDetails.getValueMap().get(card.getValue()) + ", ");
			}
			// if Ace
			else {
				System.out.print(CardDetails.getAcelow() + " || " + CardDetails.getAcehigh() + ", ");
			}
		
		}
		System.out.println();
		
		/*
		 * Check hand value functionality
		 */
		
		System.out.println("\nCheck hand value functionality");
		
		player1.emptyHand();
		player2.emptyHand();
		player3.emptyHand();
//		player4.emptyHand();
		player5.emptyHand();
		
		table.dealAll(deck);
		
		System.out.println("\n" + table);
		
		
		System.out.println("\nPlayer1:");
		for(Integer i: player1.handValue()) {
			System.out.println(i);
		}
		System.out.println("\nPlayer2:");
		for(Integer i: player2.handValue()) {
			System.out.println(i);
		}
		System.out.println("\nPlayer3:");
		for(Integer i: player3.handValue()) {
			System.out.println(i);
		}
//		System.out.println("\nPlayer4:");
//		for(Integer i: player4.handValue()) {
//			System.out.println(i);
//		}
		System.out.println("\nPlayer5:");
		for(Integer i: player5.handValue()) {
			System.out.println(i);
		}
		
		System.out.println("\nCheck initial functionality.\n"
				+ "Cards should be removed from deck and added to hands.");
		
		player1.emptyHand();
		player2.emptyHand();
		player3.emptyHand();
//		player4.emptyHand();
		player5.emptyHand();
		
		System.out.println("\nDeck (before): " + deck);
		
		deck.dealInitialCards(table.getPlayers());
		System.out.println("\nPlayer hands (after):\n" + table);
		System.out.println("Deck (After): " + deck);

	}
}
