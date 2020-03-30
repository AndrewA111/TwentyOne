import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Helper class to store and access data for representing cards
 * 
 * Used for building deck and referencing cards and their values
 * 
 * @author Andrew
 *
 */
public class CardDetails {
	/**
	 * Suits
	 */
	private static final char[] suits = {'C', 'H', 'D', 'S'};
	
	/**
	 * Card values
	 */
	private static final String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	
	
	/**
	 * HashMap to store numerical values for card values
	 * 
	 * Note, only 2 - K considered, as A can have two values (1,11)
	 */
	private static HashMap<String, Integer> valueMap;
	
	/**
	 * Ace high value
	 */
	private static final int aceHigh = 11;
	
	/**
	 * Ace low value
	 */
	private static final int aceLow = 1;

	/**
	 * Constructor
	 */
	public CardDetails() {

		/*
		 * Populate hashmap with value pairing on creation of class
		 * 
		 * (Excludes aces, these are dealt with using 'aceLow' 
		 * and 'aceHigh' variables)
		 */
		valueMap = new HashMap<String, Integer>();
		CardDetails.valueMap.put("2", 2);
		CardDetails.valueMap.put("3", 3);
		CardDetails.valueMap.put("4", 4);
		CardDetails.valueMap.put("5", 5);
		CardDetails.valueMap.put("6", 6);
		CardDetails.valueMap.put("7", 7);
		CardDetails.valueMap.put("8", 8);
		CardDetails.valueMap.put("9", 9);
		CardDetails.valueMap.put("10", 10);
		CardDetails.valueMap.put("J", 10);
		CardDetails.valueMap.put("Q", 10);
		CardDetails.valueMap.put("K", 10);
		
	}
	



	/**
	 * Getter for suits array
	 * @return suits
	 */
	public static char[] getSuits() {
		return suits;
	}
	
	/**
	 * Getter for values array
	 * @return values
	 */
	public static String[] getValues() {
		return values;
	}
	
	/**
	 * Getter for valueMap
	 * @return valueMap
	 */
	public static HashMap<String, Integer> getValueMap() {
		return valueMap;
	}
	
	/**
	 * Getter for aceHigh value
	 * @return
	 */
	public static int getAcehigh() {
		return aceHigh;
	}
	
	/**
	 * Getter for aceLow value
	 * @return
	 */
	public static int getAcelow() {
		return aceLow;
	}
	
	

}
