import java.io.Serializable;
import java.util.HashMap;

/**
 * Class to represent card
 * @author Andrew
 *
 */
public class Card implements Serializable {
	
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
	
	
	/**
	 * toString
	 */
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

	
	
	
	
}
