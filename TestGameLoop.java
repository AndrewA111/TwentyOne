
public class TestGameLoop {
	public static void main(String[] args) {
		
		
		
		/*
		 * Create model. Add people to table.
		 */
		
		Model model = new Model();
		
		Player player1 = new Player(1, "Andrew");
		Player player2 = new Player(2, "John");
		Player player3 = new Player(3, "Aaron");
		Player player4 = new Player(4, "Rebecca");
		Player player5 = new Player(5, "Mike");
		
		Table table = model.getTable();
		Deck deck = model.getDeck();
		
		table.addPlayer(player1, 0);
		table.addPlayer(player2, 1);
		table.addPlayer(player3, 2);
		table.addPlayer(player4, 3);
		table.addPlayer(player5, 4);
		
		/*
		 * Select dealer
		 */
		
		deck.dealSingleCard(table.getPlayers()[0]);
		System.out.println(table.getPlayers()[0]);
		
		// currently just sets to p1
//		table.selectInitialDealer();
//		System.out.println("\nDealer: " + table.getDealer().getName());
//		
//		/*
//		 * Deal initial cards
//		 */
//		
//		deck.dealInitialCards(table.getPlayers(), table.getDealer());
//		
//		System.out.println("\n" + table);
//		
//		
		
		/*
		 * Check for 21
		 */
		
		
		
		/*
		 * If no 21s - stand/draw for each person at table
		 */
		
		
		
		
	}
}
