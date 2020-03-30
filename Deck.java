import java.util.ArrayList;
import java.util.Collections;

/**
 * Class to build and represent standard 52-card deck
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
		
		/*
		 * Get details from CardDetails helper class
		 */
		suits = CardDetails.getSuits();
		values = CardDetails.getValues();
		
		/*
		 * Create empty arrayList to hold cards
		 */
		deck = new ArrayList<Card>();
		
		/*
		 * For each suit, create one of each card value
		 */
		for(char suit : suits) {
			for(String value : values) {
				deck.add(new Card(value, suit));
			}
		}
	}
	
	/**
	 * Method to shuffle order of deck
	 */
	public void shuffle() {
		Collections.shuffle(deck);
	}
	
	/**
	 * Method to add card to end of deck
	 * @param card card to be added to deck
	 */
	public void addCard(Card card) {
		this.deck.add(card);
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
	
	/**
	 * Getter for deck
	 * @return deck
	 */
	public ArrayList<Card> getDeck() {
		return deck;
	}
	
	
}
