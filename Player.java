import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
	
	/**
	 * Player ID
	 */
	private int ID;
	
	/**
	 * Player name
	 */
	private String name;
	
	/**
	 * Position at table
	 */
	private int tablePos = -1;
	
	/**
	 * Hand of cards
	 */
	private Hand hand;
	
	/**
	 * Current balance
	 */
	private int balance;
	
	/**
	 * Current stake
	 */
	private int stake;
	
	/**
	 * Flag for whether stake can be changed
	 */
	private boolean ableToChangeStake;
	
	/**
	 * Flag for whether player can draw/stand
	 */
	private boolean ableToDrawOrStand;
	
	/**
	 * Flag for whether player can join table
	 */
	private boolean ableToJoin;
	
	/**
	 * Flag for whether player can leave table
	 */
	private boolean ableToLeave;
	
	/**
	 * Flag for whether player is dealer
	 */
	private boolean dealer;
	
	/*
	 * Integer to indicate whether player wishes to draw or stand
	 * 
	 * Code:
	 * -1 undecided
	 * 1 draw
	 * 2 stand
	 */
	private int drawOrStand;
	
	/**
	 * Minimum allowed stake
	 */
	private final static int MIN_STAKE = 20;
	
	/**
	 * Increment for stake changes
	 */
	private final static int STAKE_INC = 10;
	
	/**
	 * Constructor
	 * @param ID Player ID
	 * @param name Player name
	 */
	public Player(int ID, String name) {
		
		this.ID = ID;
		this.name = name;
		
		// set initial balance
		this.balance = 200;
		
		// initially set player as not being dealer
		this.dealer = false;
		
		// initially set undecided as to whether to draw/stand
		drawOrStand = -1;
		
		// initially set stake to MIN_STAKE
		this.stake = MIN_STAKE;
		
		// create new, empty hand
		this.hand = new Hand();
		
		// disable stake changing
		this.ableToChangeStake = false;
		
		// disable draw/stand
		this.ableToDrawOrStand = false;
		
		// enable joining
		this.ableToJoin = true;
		
		// disable leaving
		this.ableToLeave = false;
		
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
	 * Method to increase stake by set increment
	 */
	public void stakeUp() {
		if(this.balance >= STAKE_INC){
			this.stake += STAKE_INC;
			this.balance -= STAKE_INC;
		}
	}
	
	/**
	 * Method to decrease stake by set increment
	 */
	public void stakeDown() {
		if(this.stake >= MIN_STAKE + STAKE_INC) {
			this.stake -= STAKE_INC;
			this.balance += STAKE_INC;
		}
	}
	
	/**
	 * Method to add given amount to balance
	 * @param ammount
	 */
	public void addToBalance(int ammount) {
		this.balance += ammount;
	}
	
	/**
	 * Method to remove given amount from balance
	 * @param ammount
	 */
	public void removeFromBalance(int ammount) {
		this.balance -= ammount;
	}
	
	/**
	 * Method to pay Player p this players stake and 
	 * reset this stake to min value
	 * @param p Player to pay
	 */
	public void payStake(Player p) {
		p.addToBalance(this.stake);
		this.removeFromBalance(MIN_STAKE);
		this.stake = MIN_STAKE;
	}
	
	/**
	 * Method to pay Player p double this players 
	 * stake and reset this stake to min value
	 * @param p Player to pay
	 */
	public void payDoubleStake(Player p) {
		p.addToBalance(2 * this.stake);
		this.removeFromBalance(MIN_STAKE + this.stake);
		this.stake = MIN_STAKE;
	}
	
	/**
	 * Method to get the value(s) of this players hand
	 * @return values arrayList of possible values for hand
	 */
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

	
	/**
	 * Getter for player ID
	 * @return ID
	 */
	public int getID() {
		return ID;
	}
	
	/**
	 * Getter for player name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Getter for table position
	 * @return tablePos
	 */
	public int getTablePos() {
		return tablePos;
	}
	
	/**
	 * Setter for table position
	 * @param tablePos
	 */
	public void setTablePos(int tablePos) {
		this.tablePos = tablePos;
	}
	
	/**
	 * Getter for player's hand
	 * @return hand
	 */
	public Hand getHand() {
		return hand;
	}
	
	/**
	 * Getter for change stake flag
	 * @return ableToChangeStake
	 */
	public boolean isAbleToChangeStake() {
		return ableToChangeStake;
	}
	
	/**
	 * Setter for change stake flag
	 * @param ableToChangeStake
	 */
	public void setAbleToChangeStake(boolean ableToChangeStake) {
		this.ableToChangeStake = ableToChangeStake;
	}
	
	/**
	 * Getter for draw/stand flag
	 * @return ableToDrawOrStand
	 */
	public boolean isAbleToDrawOrStand() {
		return ableToDrawOrStand;
	}
	
	/**
	 * Setter for draw/stand flag
	 * @param ableToDrawOrStand
	 */
	public void setAbleToDrawOrStand(boolean ableToDrawOrStand) {
		this.ableToDrawOrStand = ableToDrawOrStand;
	}
	
	/**
	 * Getter for draw/stand indication
	 * @return drawOrStand
	 */
	public int getDrawOrStand() {
		return drawOrStand;
	}
	
	/**
	 * Setter for draw/stand indication
	 * @param drawOrStand
	 */
	public void setDrawOrStand(int drawOrStand) {
		this.drawOrStand = drawOrStand;
	}
	
	/**
	 * Getter for stake
	 * @return stake
	 */
	public int getStake() {
		return stake;
	}
	
	/**
	 * Getter for balance
	 * @return balance
	 */
	public int getBalance() {
		return balance;
	}
	
	/**
	 * Getter for dealer flag
	 * @return dealer
	 */
	public boolean isDealer() {
		return dealer;
	}
	
	/**
	 * Setter for dealer flag
	 * @param dealer
	 */
	public void setDealer(boolean dealer) {
		this.dealer = dealer;
	}
	
	/**
	 * Getter for joining flag
	 * @return ableToJoin
	 */
	public boolean isAbleToJoin() {
		return ableToJoin;
	}
	
	/**
	 * Setter for joining flag
	 * @param ableToJoin
	 */
	public void setAbleToJoin(boolean ableToJoin) {
		this.ableToJoin = ableToJoin;
	}
	
	/**
	 * Getter for leaving flag
	 * @return
	 */
	public boolean isAbleToLeave() {
		return ableToLeave;
	}
	
	/**
	 * Setter for leaving flag
	 * @param ableToLeave
	 */
	public void setAbleToLeave(boolean ableToLeave) {
		this.ableToLeave = ableToLeave;
	}
	
	
	
	
	
	
	
	


	
	
	
	

	
	
	
	
}
