
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
