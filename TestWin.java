/**
 * Class to test win condition checking for TwentyOne game
 * @author Andrew
 *
 */
public class TestWin {
	public static void main(String[] arg) {
		
		
		/*
		 * =====================================
		 * Test cases for initial deal of 21
		 * =====================================
		 */
		
		/*
		 * Dealer (Player 1) has 21, no other 21s
		 */
		
		giveDealerOnly21();
		
		/*
		 * Player 2 (not dealer) has 21, no other 21s
		 */
		givePlayer2Only21();
		
		/*
		 * Player 2 (dealer) and Player 4 have 21
		 */
		dealerAndPlayerHave21();
		
		/*
		 * Player 3 and and Player 4 
		 */
		twoPlayersHave21();
		
		/*
		 * Normal round
		 */
		regularRoundResult();
		
		
		
		
		
	}
	
	/**
	 * Creates players with up to 5 players. Dealer initially
	 * at pos 0
	 * 
	 * @param noPlayers 
	 * @return table
	 */
	public static Table createTable(int noPlayers) {
		
		// max of 5
		if(noPlayers > 5) {
			return null;
		}
		
		Table table = new Table();
		
		for(int i = 0; i < noPlayers; i++) {
			table.addPlayer(new Player(1, "Player"+ (i+1), false), i);
		}
		
		table.getPlayers()[0].setDealer(true);
		table.setDealerPos(0);
		
		
		return table;
	}
	
	public static void giveDealerOnly21() {
		
		Table t = createTable(5);
		
		System.out.println("\n======================"
				+ "\nGive dealer only 21"
				+ "\n======================\n");
		
		t.getPlayers()[0].addCard(new Card("A", 'S'));
		t.getPlayers()[0].addCard(new Card("K", 'S'));
		t.getPlayers()[1].addCard(new Card("2", 'S'));
		t.getPlayers()[1].addCard(new Card("4", 'S'));
		t.getPlayers()[2].addCard(new Card("10", 'S'));
		t.getPlayers()[2].addCard(new Card("6", 'S'));
		t.getPlayers()[3].addCard(new Card("2", 'S'));
		t.getPlayers()[3].addCard(new Card("2", 'S'));
		t.getPlayers()[4].addCard(new Card("9", 'S'));
		t.getPlayers()[4].addCard(new Card("8", 'S'));
		
		
		
		System.out.println(t);
		
		System.out.println("Dealer: " + t.dealer().getName() + "\n");
		
		System.out.println("\ncheckTwentyOne() returns: " + t.checkTwentyOne());
		
		System.out.println("\nPlayers all pay double stakes (40) to dealer:");
		
		System.out.println(t);
		
		System.out.println("New Dealer: " + t.dealer().getName() + "\n");
	}
	
	public static void givePlayer2Only21() {
		
		Table t = createTable(5);
		
		// set dealer to player 3
		t.incrementDealerPos();
		t.incrementDealerPos();
		
		System.out.println("\n============================================"
				+ "\nPlayer 2 (not dealer) has 21, no other 21s"
				+ "\n============================================\n");
		
		t.getPlayers()[0].addCard(new Card("4", 'S'));
		t.getPlayers()[0].addCard(new Card("K", 'S'));
		t.getPlayers()[1].addCard(new Card("A", 'S'));
		t.getPlayers()[1].addCard(new Card("K", 'S'));
		t.getPlayers()[2].addCard(new Card("10", 'S'));
		t.getPlayers()[2].addCard(new Card("6", 'S'));
		t.getPlayers()[3].addCard(new Card("2", 'S'));
		t.getPlayers()[3].addCard(new Card("2", 'S'));
		t.getPlayers()[4].addCard(new Card("9", 'S'));
		t.getPlayers()[4].addCard(new Card("8", 'S'));
		
		System.out.println(t);
		
		System.out.println("Dealer: " + t.dealer().getName() + "\n");
		
		System.out.println("\ncheckTwentyOne() returns: " + t.checkTwentyOne());
		
		System.out.println("\nPlayers all pay double stake (2 x 20) to player2:");
		
		System.out.println(t);
		
		System.out.println("New Dealer: " + t.dealer().getName() + "\n");
	}
	
	public static void dealerAndPlayerHave21() {
		
		Table t = createTable(5);
		
		// set dealer to player 2
		t.incrementDealerPos();
		
		System.out.println("\n========================================"
				+ "\nPlayer 2 (dealer) and Player 4 have 21"
				+ "\n========================================\n");
		
		t.getPlayers()[0].addCard(new Card("4", 'S'));
		t.getPlayers()[0].addCard(new Card("K", 'S'));
		t.getPlayers()[1].addCard(new Card("A", 'S'));
		t.getPlayers()[1].addCard(new Card("K", 'S'));
		t.getPlayers()[2].addCard(new Card("10", 'S'));
		t.getPlayers()[2].addCard(new Card("6", 'S'));
		t.getPlayers()[3].addCard(new Card("J", 'S'));
		t.getPlayers()[3].addCard(new Card("A", 'S'));
		t.getPlayers()[4].addCard(new Card("9", 'S'));
		t.getPlayers()[4].addCard(new Card("8", 'S'));
		
		System.out.println(t);
		
		System.out.println("Dealer: " + t.dealer().getName() + "\n");
		
		System.out.println("\ncheckTwentyOne()() returns: " + t.checkTwentyOne());
		
		System.out.println("\nPlayer 2 takes double stakes from everyone excluding Player 4");
		
		System.out.println(t);
		
		System.out.println("New Dealer: " + t.dealer().getName() + "\n");
	}

	public static void twoPlayersHave21() {
		
		Table t = createTable(5);
		
		// set dealer to player 2
		t.incrementDealerPos();
		
		System.out.println("\n============================================"
				+ "\nPlayer 1 and Player 4 (not dealers) have 21"
				+ "\n============================================\n");
		
		t.getPlayers()[0].addCard(new Card("A", 'S'));
		t.getPlayers()[0].addCard(new Card("K", 'S'));
		t.getPlayers()[1].addCard(new Card("4", 'S'));
		t.getPlayers()[1].addCard(new Card("K", 'S'));
		t.getPlayers()[2].addCard(new Card("10", 'S'));
		t.getPlayers()[2].addCard(new Card("5", 'S'));
		t.getPlayers()[3].addCard(new Card("J", 'S'));
		t.getPlayers()[3].addCard(new Card("A", 'S'));
		t.getPlayers()[4].addCard(new Card("9", 'S'));
		t.getPlayers()[4].addCard(new Card("8", 'S'));
		
		System.out.println(t);
		
		System.out.println("Dealer: " + t.dealer().getName() + "\n");
		
		System.out.println("\ncheckTwentyOne()() returns: " + t.checkTwentyOne());
		
		System.out.println("\nPlayer 4 takes double stakes as is first in order (from dealer)");
		
		System.out.println(t);
		
		System.out.println("New Dealer: " + t.dealer().getName() + "\n");
	}
	
	public static void regularRoundResult() {
		
		Table t = createTable(5);
		
		// set dealer to player 2
		t.incrementDealerPos();
		
		System.out.println("\n============================================"
				+ "\nStandard round"
				+ "\n============================================\n");
		
		t.getPlayers()[0].addCard(new Card("10", 'S'));
		t.getPlayers()[0].addCard(new Card("7", 'S'));
		t.getPlayers()[1].addCard(new Card("4", 'S'));
		t.getPlayers()[1].addCard(new Card("5", 'S'));
		t.getPlayers()[1].addCard(new Card("10", 'S'));
		t.getPlayers()[2].addCard(new Card("8", 'S'));
		t.getPlayers()[2].addCard(new Card("5", 'S'));
		t.getPlayers()[2].addCard(new Card("10", 'S'));
		t.getPlayers()[3].addCard(new Card("J", 'S'));
		t.getPlayers()[3].addCard(new Card("K", 'S'));
		t.getPlayers()[4].addCard(new Card("9", 'S'));
		t.getPlayers()[4].addCard(new Card("8", 'S'));
		
		System.out.println(t);
		
		System.out.println("Dealer: " + t.dealer().getName() + "\n");
		
		System.out.println("checkTwentyOne()() returns: " + t.checkTwentyOne());
		
		// check winners and exchange stakes
		System.out.println("\nChecking winners and exchanging stakes");
		t.checkingWinnerEndRound();
		
		// increment dealer
		t.incrementDealerPos();
		
		System.out.println("\n");
		
		System.out.println(t);
		
		System.out.println("New Dealer: " + t.dealer().getName() + "\n");
	}
}
