import java.util.ArrayList;

public class Player {
	
	private int ID;
	private String name;
	
	private Hand hand;
	
	private int balance;
	private int stake;
	
	public Player(int ID, String name) {
		
		this.ID = ID;
		this.name = name;
		this.balance = 200;
		
		this.hand = new Hand();
		
	}
	
	/**
	 * Method to add card to players hand
	 * @param card
	 */
	public void addCard(Card card) {
		this.hand.addCard(card);
	}
	
	/**
	 * Method to empty players hand
	 */
	public void emptyHand() {
		this.hand.empty();
	}
	
	
	public ArrayList<Integer> handValue() {
		
		return this.hand.value();
	}
	
	/**
	 * toString
	 */
	public String toString() {
		String output = "";
		
		output += this.name + "\t";
		output += this.hand;
		
		return output;
	}
	
}
