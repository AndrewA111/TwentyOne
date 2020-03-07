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
	
	
	public void dealInitialCards(Player[] players) {
		
		// two loops of adding cards
		for(int i = 0; i < 2; i++) {
			
			// for each position in players array
			for(int j = 0; j < players.length; j++) {
				
				// if player in position
				if(players[j] != null) {
					players[j].addCard(this.deck.remove(0));
				}
			}
		}
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
