import java.util.ArrayList;
import java.util.HashMap;

public class Hand {
	
	/**
	 * Hand - list of cards
	 */
	private ArrayList<Card> hand;
	
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
	 * Method to return all possible values for combined hand score
	 * 
	 * Returns an ArrayList of integers, this is to account for 
	 * Aces (A) having possible values of 1 or 11
	 * 
	 * 
	 * @return values Array of possible total value of cards in hand
	 */
	public ArrayList<Integer> value() {
		
		ArrayList<Integer> values = new ArrayList<Integer>();
		
		values.add(0);
		
		// for each card
		for(Card card : this.hand) {
			
			// ArrayList to store additional values generated due to Aces
			ArrayList<Integer> additionalValues = new ArrayList<Integer>();
			
			// for each possible value so far
			for(int i = 0; i < values.size(); i++){
				
				// if not ace
				if(!card.getValue().equals("A")) {
					
					int cardNum = CardDetails.getValueMap().get(card.getValue());
					
					values.set(i, cardNum + values.get(i));
				}
				// if ace
				else {
					
					additionalValues.add(values.get(i) + 1);
					
					values.set(i, values.get(i) + 11);
				}
			}
			
			values.addAll(additionalValues);
			
		}
		
		return values;
	}
		
		

	
	public String toString() {
		String output = "Hand: ";
		
		for(Card card : hand) {
			output += card + ", ";
		}
		
		return output;
	}
}
