import java.util.ArrayList;
import java.util.HashMap;

public class Hand {
	
	private ArrayList<Card> hand;
	
	public static HashMap<String, Integer> valueMap;
	
	
	public Hand() {
		hand = new ArrayList<Card>();
	}
	
	/**
	 * Add card to hand
	 * @param card
	 */
	public void addCard(Card card) {
		this.hand.add(card);
	}
	
	/**
	 * Empty hand
	 */
	public void empty() {
		this.hand = new ArrayList<Card>();
	}
	
	/**
	 * Hand value ideas:
	 * 
	 * ArrayList to store possible values of the hand.
	 * 
	 * If not Ace - add value to all values
	 * 
	 * If Ace - take every value and add 1, then take every value and add 0 (doubling array size
	 */
	
	public String toString() {
		String output = "Hand: ";
		
		for(Card card : hand) {
			output += card + ", ";
		}
		
		return output;
	}
}
