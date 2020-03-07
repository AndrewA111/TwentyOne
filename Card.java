import java.util.HashMap;

/**
 * Class to represent card
 * @author Andrew
 *
 */
public class Card {
	
	/**
	 * Suits
	 */
	private static final char[] suits = {'C', 'H', 'D', 'S'};
	
	/**
	 * Card values
	 */
	private static final String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	
	// 2,3...10,J,Q,K,A
	private String symbol;
	
	/*
	 * C - Clubs
	 * H - Hearts
	 * D - Diamonds
	 * S - Spades
	 */
	private char suit;
	
	/**
	 * Constructor
	 * @param value
	 * @param type
	 */
	public Card(String value, char type) {
		this.symbol = value;
		this.suit = type;
	}
	
	public String toString() {
		return suit + symbol;
	}
	
	/**
	 * Getter for value
	 * @return
	 */
	public String getValue() {
		return symbol;
	}
	
	/**
	 * Getter for type
	 * @return
	 */
	public char getType() {
		return suit;
	}

	public static char[] getSuits() {
		return suits;
	}

	public static String[] getValues() {
		return values;
	}
	
	
	
	
}
