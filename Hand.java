import java.io.Serializable;
import java.util.ArrayList;


/**
 * Class to represent a player's hand of cards
 * @author Andrew
 *
 */
public class Hand implements Serializable {
	
	/**
	 * Hand - list of cards
	 */
	private ArrayList<Card> hand;
	
	/**
	 * Constructor
	 */
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
	 * Remove hand from card
	 * @param index position in hand to remove card
	 * @return card removed from hand
	 */
	public Card removeCard(int index) {
		
		Card returnCard = this.hand.remove(index);
		return returnCard;
	}
	
	/**
	 * Empty hand
	 */
	public void empty() {
		this.hand = new ArrayList<Card>();
	}
	
	
	/**
	 * Method to return all possible values for combined hand score, 
	 * this is to account for Aces (A) having possible values of 1 or 11
	 * 
	 * Returns an ArrayList of integers
	 * 
	 * @return values Array of possible total value of cards in hand
	 */
	public ArrayList<Integer> value() {
		
		// arraylist to store possible values
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		// initially value is 0
		values.add(0);
		
		// for each card in hand
		for(Card card : this.hand) {
			
			/*
			 *  ArrayList to store additional values generated 
			 *  if this card is an ace
			 */
			ArrayList<Integer> additionalValues = new ArrayList<Integer>();
			
			/*
			 *  for each possible value in 'values' so far
			 */
			for(int i = 0; i < values.size(); i++){
				
				// if not ace
				if(!card.getValue().equals("A")) {
					
					// get card value
					int cardNum = CardDetails.getValueMap().get(card.getValue());
					
					// increase current value by card value
					values.set(i, cardNum + values.get(i));
				}
				// if ace
				else {
					/*
					 * Add new value if ace value is 1 to additional values
					 */
					additionalValues.add(values.get(i) + 1);
					
					/*
					 * Increase existing value by 11
					 */
					values.set(i, values.get(i) + 11);
				}
			}
			
			/*
			 * Add any additional values due to aces to values
			 * 
			 * Additional values consider ace as 1 and are added 
			 * at the end of 'values'. Therefore when 'values' contains 
			 * multiple values they will be in descending order
			 */
			values.addAll(additionalValues);
			
		}
		
		return values;
	}
	
	/**
	 * Method to return max legal value (<= 21) for this hand
	 * 
	 * @return max legal value, or -1 if no legal value found
	 */
	public int maxLegalValue() {
		
		/*
		 * generate values arrayList
		 */
		ArrayList<Integer> values = this.value();
		
		/*
		 * Start at max value (index 0)
		 */
		int index = 0;
		
		/*
		 * While in bounds of 'values'
		 */
		while(index < values.size()) {
			
			/*
			 * If greater than 21, increment to next index
			 */
			if(values.get(index) > 21) {
				index++;
			}
			/*
			 * Legal value found, return value
			 */
			else {
				return values.get(index);
			}
		}
		
		/*
		 * No legal value found, return -1
		 */
		return -1;
	}
	
	/**
	 * Method to return min legal value (<= 21) for this hand
	 * 
	 * @return min legal value, or -1 if no legal value found
	 */
	public int minLegalValue() {
		/*
		 * generate values arrayList
		 */
		ArrayList<Integer> values = this.value();
		
		/*
		 * Get min value (last in list) 
		 */	
		int min = values.get(values.size() - 1);
		
		/*
		 * If min value is vale (<= 21) return it
		 */
		if(min <= 21) {
			return min;
		}
		
		/*
		 * No legal values, return -1
		 */
		return -1;
	}
		
	
	
	/**
	 * Method to return values list as a string
	 * @return values list as string
	 */
	public String valuesAsString() {
		
		String output = "Value: ";
		
		ArrayList<Integer> values = this.value();
		
		for(Integer val : values) {
			output += val + ", ";
		}
		
		return output;
	}

	/**
	 * toString
	 */
	public String toString() {
		String output = "Hand: ";
		
		for(Card card : hand) {
			output += card + ", ";
		}
		
		return output;
	}
	
	/**
	 * Getter for hand
	 * @return hand
	 */
	public ArrayList<Card> getHand() {
		return hand;
	}
	
	
}
