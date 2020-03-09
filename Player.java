import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
	
	private int ID;
	private String name;
	private int tablePos;
	
	private Hand hand;
	
	private int balance;
	private int stake;
	
	public Player(int ID, String name, int tablePos) {
		
		this.ID = ID;
		this.name = name;
		this.balance = 200;
		this.tablePos = tablePos;
		
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
		
		output += this.name + "\n";
		output += this.hand;
		
		return output;
	}

	public String getName() {
		return name;
	}

	public int getTablePos() {
		return tablePos;
	}

	public void setTablePos(int tablePos) {
		this.tablePos = tablePos;
	}

	
	
	
	
}
