import java.util.ArrayList;
import java.util.Collections;

/**
 * Class to represent standard 52-card deck
 * @author Andrew
 *
 */
public class Deck {
	
	/**
	 * ArrayList to hold cards
	 */
	private ArrayList<Card> deck;
	
	/**
	 * Suits to be used
	 */
	private static char[] suits;
	
	/**
	 * Card values to be used
	 */
	private static String[] values;
	
	/**
	 * Constructor
	 * 
	 * Generate cards objects comprising standard 52 card deck
	 */
	public Deck() {
		
		suits = CardDetails.getSuits();
		values = CardDetails.getValues();
		
		deck = new ArrayList<Card>();
		
		for(char suit : suits) {
			for(String value : values) {
				deck.add(new Card(value, suit));
			}
		}
	}
	
	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	public void addCard(Card card) {
		this.deck.add(card);
	}
	
	/**
	 * Deal 2 cards to each player, clockwise, starting immediately after dealer
	 * (Clockwise is in a positive direction through array)
	 * @param players
	 * @param dealer
	 */
	public void dealInitialCards(Player[] players, Player dealer) {
		
		/*
		 * Get dealer position
		 */
		int dealerPos = -1;
		
		for(int i = 0; i < players.length; i++) {
			if(players[i] == dealer) {
				dealerPos = i;
			}
		}
		
		for(int i = (dealerPos + 1); i < (dealerPos + (2 * players.length) + 1); i++){
			
			// if player in position
			if(players[i % players.length] != null) {
				players[i % players.length].addCard(this.deck.remove(0));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	/**
	 * Deal and single card to a player
	 * @param player
	 */
	public void dealSingleCard(Player player) {
		player.addCard((this.deck.remove(0)));
	}
	
	/**
	 * toString method
	 */
	public String toString() {
		
		String output = "";
		
		for(Card card : deck) {
			output += card + ", ";
		}
		
		return output;
	}

	public ArrayList<Card> getDeck() {
		return deck;
	}
	
	
}
