import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
	
	private int ID;
	private String name;
	private int tablePos;
	
	private Hand hand;
	
	private int balance;
	private int stake;
	
	private final static int MIN_STAKE = 20;
	private final static int STAKE_INC = 10;
	
	private boolean ableToChangeStake;
	private boolean ableToDrawOrStand;
	
	private boolean dealer;
	
	/*
	 * -1 undecided
	 * 1 draw
	 * 2 stand
	 */
	private int drawOrStand;
	
	public Player(int ID, String name, int tablePos) {
		
		this.ID = ID;
		this.name = name;
		this.balance = 200;
		this.tablePos = tablePos;
		this.dealer = false;
		
		/*
		 * Initially undecided
		 */
		drawOrStand = -1;
		
		this.stake = 20;
		
		this.hand = new Hand();
		
		this.ableToChangeStake = true;
		this.ableToDrawOrStand = false;
		
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
	 * !! coudl include check whether can change stake in here?
	 */
	public void stakeUp() {
		if(this.balance >= STAKE_INC){
			this.stake += STAKE_INC;
			this.balance -= STAKE_INC;
		}
	}
	
	public void stakeDown() {
		if(this.stake >= MIN_STAKE + STAKE_INC) {
			this.stake -= STAKE_INC;
			this.balance += STAKE_INC;
		}
	}
	
	public void addToBalance(int ammount) {
		this.balance += ammount;
	}
	
	public void removeFromBalance(int ammount) {
		this.balance -= ammount;
	}
	
	/*
	 * Pay Player p this players balance and reset this balance to min value
	 */
	public void payStake(Player p) {
		p.addToBalance(this.stake);
		this.removeFromBalance(MIN_STAKE);
		this.stake = MIN_STAKE;
	}
	
	public void payDoubleStake(Player p) {
		p.addToBalance(2 * this.stake);
		this.removeFromBalance(MIN_STAKE + this.stake);
		this.stake = MIN_STAKE;
	}
	
	public ArrayList<Integer> handValue() {
		
		return this.hand.value();
	}
	
	/**
	 * toString
	 */
	public String toString() {
		String output = "";
		
		output += this.name;
		output += "\t" + this.hand;
		output += "\tBalance: " + this.balance;
		output += "\tStake: " + this.stake;
		output += "\tValue(s): " + this.hand.value();
		if(this.dealer) {
			output += "    (Dealer)";
		}
		
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

	public Hand getHand() {
		return hand;
	}

	public boolean isAbleToChangeStake() {
		return ableToChangeStake;
	}

	public void setAbleToChangeStake(boolean ableToChangeStake) {
		this.ableToChangeStake = ableToChangeStake;
	}

	public boolean isAbleToDrawOrStand() {
		return ableToDrawOrStand;
	}

	public void setAbleToDrawOrStand(boolean ableToDrawOrStand) {
		this.ableToDrawOrStand = ableToDrawOrStand;
	}

	public int getDrawOrStand() {
		return drawOrStand;
	}

	public void setDrawOrStand(int drawOrStand) {
		this.drawOrStand = drawOrStand;
	}

	public int getStake() {
		return stake;
	}

	public int getBalance() {
		return balance;
	}

	public boolean isDealer() {
		return dealer;
	}

	public void setDealer(boolean dealer) {
		this.dealer = dealer;
	}
	
	
	
	
	
	
	


	
	
	
	

	
	
	
	
}
