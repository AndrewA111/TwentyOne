import java.util.ArrayList;
import java.util.Iterator;

public class Table {
	
	Player[] players;
	
	/*
	 * !!!
	 * This will only come into use if static table position implemented
	 */
	private static final int numPlayers = 5;
	
	public Table() {
		
		// set number of places at table
		players = new Player[numPlayers];
		
	}
	
//	public boolean dealNCards(int n) {
//		
//		// check number of players
//		int playerCount = this.checkNumPlayers();
//		
//		// For testings
//		System.out.println("Player count: " + playerCount);
//		
//		/*
//		 * Return false and do not deal if no players
//		 */
//		if(playerCount < 1) {
//			return false;
//		}
//		
//		/*
//		 * Variable to track index
//		 * Continually increment, using modulo to loop over players
//		 */
//		int index = 0;
//		int loops = 0;
//		
//		while(loops < n) {
//			
//		}
//		
//		for(Card card : deck.getDeck()) {
//			
//			// ignore empty positions
//			while(players[(index % players.length)] == null) {
//				index++;
//			}
//			
//			// deal card to positions with players
//			players[(index % players.length)].addCard(card);
//			index++;
//		}
//		
//		
//	}
	
	/**
	 * Method to deal out entire deck
	 * @param deck
	 */
	public boolean dealAll(Deck deck) {
		
		// check number of players
		int playerCount = this.checkNumPlayers();
		
		// For testings
		System.out.println("Player count: " + playerCount);
		
		/*
		 * Return false and do not deal if less than 2 players
		 */
		if(playerCount < 2) {
			return false;
		}
		
		/*
		 * Deal cards
		 */
		
		/*
		 * Variable to track index
		 * Continually increment, using modulo to loop over players
		 */
		int index = 0;
		
		for(Card card : deck.getDeck()) {
			
			// ignore empty positions
			while(players[(index % players.length)] == null) {
				index++;
			}
			
			// deal card to positions with players
			players[(index % players.length)].addCard(card);
			index++;
		}
		
		// return true to indicate that cards have been dealt
		return true;
	}
	
	/**
	 * Add player to table
	 * @param player
	 * @param pos position in players array
	 * @return
	 */
	public boolean addPlayer(Player player, int pos) {
		
		// if position empty, add player
		if(this.players[pos] == null) {
			this.players[pos] = player;
			return true;
		}
		
		// return false is position already taken
		return false;

	}
	
	/**
	 * Method to check number of players at the table
	 * @return playerCount
	 */
	public int checkNumPlayers() {

		int playerCount = 0; 
		
		/*
		 * Check how many players are at table
		 */
		for(Player player : players) {
			if(player != null) {
				playerCount++;
			}
		}
		
		return playerCount;
	}
	
	/**
	 * toString method, shows all players hands
	 */
	public String toString() {
		String output = "";
		
		for(Player player : players) {
			output += player + "\n";
		}
		
		return output;
	}
	

}
