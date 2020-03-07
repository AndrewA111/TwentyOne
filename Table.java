import java.util.ArrayList;
import java.util.Iterator;

public class Table {
	
	ArrayList<Player> players;
	
	/*
	 * !!!
	 * This will only come into use if static table position implemented
	 */
	private static int numPlayers = 5;
	
	public Table() {
		
		// set number of places at table
		players = new ArrayList<Player>();
		
	}
	
	/**
	 * Method to deal out entire deck
	 * @param deck
	 */
	public void dealAll(Deck deck) {
		
		Iterator<Player> playerIt = this.players.iterator();
		
		for(Card card : deck.getDeck()) {
			
			if(playerIt.hasNext()) {
				playerIt.next().addCard(card);
			}
			else {
				playerIt = this.players.iterator();
				playerIt.next().addCard(card);
			}
			
			
		}
	}
	
	/**
	 * Add player to players ArrayList
	 * @param player
	 */
	public void addPlayer(Player player) {
		this.players.add(player);
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
