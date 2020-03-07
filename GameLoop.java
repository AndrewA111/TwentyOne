
public class GameLoop {
	public static void main(String[] args) {
		
		Model model = new Model();
		
		/*
		 * Create table and deal initial cards
		 */
		
		Player player1 = new Player(1, "Andrew");
		Player player2 = new Player(2, "John");
		Player player3 = new Player(3, "Aaron");
		Player player4 = new Player(4, "Rebecca");
		Player player5 = new Player(5, "Mike");
		
		model.getTable().addPlayer(player1, 0);
		model.getTable().addPlayer(player2, 1);
		model.getTable().addPlayer(player3, 2);
		model.getTable().addPlayer(player4, 3);
		model.getTable().addPlayer(player5, 4);
		
		model.newGame();
		
		/*
		 * Select dealer
		 */
		
		
		
		/*
		 * Deal (from dealer)
		 */
		
		
		
		
		/*
		 * Check for 21
		 */
		
		
		
		/*
		 * If no 21s - stand/draw for each person at table
		 */
		
		
		
		
	}
}
