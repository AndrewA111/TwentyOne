import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

public class Table {
	
	private Player[] players;
	private Player dealer;
	private Deck deck;
	private String gameMessage;
	
	private static final int numPlayers = 5;
	
	private int currentPlayerPos;
	private int dealerPos;
	
	private int noPlayers;
	
	/*
	 * Lock
	 */
	private ReentrantLock tableLock = new ReentrantLock();
	
	public Table() {
		
		// set number of places at table
		this.players = new Player[numPlayers];
		this.currentPlayerPos = 0;
		this.noPlayers = 0;
		this.deck = new Deck();
		System.out.println(this.deck);
		this.deck.shuffle();
		System.out.println(this.deck);
		this.gameMessage = null;
		
		/*
		 * Initialize hashmap for valuing hands
		 */
		CardDetails cardDetails = new CardDetails();
		
	}
	
	public boolean checkVingtUn() {
		
		// start at pos after dealer
		this.currentPlayerPos = this.dealerPos;
		this.incrementCurrentPlayer();
		
		
		/*
		 * if dealer has 21
		 */
		if(this.players[dealerPos].getHand().maxLegalValue() == 21) {
			
			System.out.println("Dealer has 21!");
			this.gameMessage = "Dealer has 21!";
			while(currentPlayerPos !=  dealerPos) {
				
				// if player doesn't have 21 pay dealer
				if(!(this.players[currentPlayerPos].getHand().maxLegalValue() == 21)) {
					this.players[currentPlayerPos].payStake(this.players[dealerPos]);
				}
				
				this.incrementCurrentPlayer();
			}
			
			return true;
		}
		
		/*
		 * Else (dealer does not have 21)
		 */
		else {
			while(currentPlayerPos !=  dealerPos) {
				/* 
				 * If a player has 21 all other players who 
				 * don't also have 21 pay this player
				 * 
				 * In the case another player has 21, they do not pay this 
				 * player. However, all other players still player this player
				 * as they have positional advantage (they come first after dealer)
				 * 
				 */
				if((this.players[currentPlayerPos].getHand().maxLegalValue() == 21)) {
					
					// store winner pos
					int winnerPos = currentPlayerPos;
					this.incrementCurrentPlayer();
					
					
					System.out.println("Player " + winnerPos + " has 21!");
					this.gameMessage = "Player " + winnerPos + " has 21!";
					
					while(currentPlayerPos != winnerPos) {
						// if player doesn't have 21 pay winning player
						if(!(this.players[currentPlayerPos].getHand().maxLegalValue() == 21)) {
							this.players[currentPlayerPos].payStake(this.players[winnerPos]);
						}
						this.incrementCurrentPlayer();
					}
					
					return true;
				}
				
				this.incrementCurrentPlayer();
					
			}
		}
		
		/*
		 * No 21s
		 */
		return false;
	}
	
	public void checkingWinnerEndRound() {
		
		this.currentPlayerPos = this.dealerPos;
		this.incrementCurrentPlayer();
		
		while(currentPlayerPos != dealerPos) {
			
			// if player bust
			if(this.players[currentPlayerPos].getHand().maxLegalValue() == -1) {
				this.players[currentPlayerPos].payStake(this.players[dealerPos]);
			}
			// if player has greater value than dealer
			else if(this.players[currentPlayerPos].getHand().maxLegalValue() >
					this.players[this.dealerPos].getHand().maxLegalValue()) {
				this.players[dealerPos].payStake(this.players[currentPlayerPos]);
			}
			// if player has lower value than dealer
			else if(this.players[currentPlayerPos].getHand().maxLegalValue() >
					this.players[this.dealerPos].getHand().maxLegalValue()) {
				this.players[currentPlayerPos].payStake(this.players[dealerPos]);
			}
			
			this.incrementCurrentPlayer();
		}
		
	}
	
	/**
	 * ======= Test Only ========!!
	 * Method to deal out entire deck (does not remove cards from deck
	 * @param deck
	 */
	public boolean dealAll(Deck deck) {
		
		// check number of players
		int playerCount = this.checkNumPlayers();
		
		// For testing
		System.out.println("Player count: " + playerCount);
		
		/*
		 * Return false and do not deal if less than 2 players
		 */
		if(playerCount < 2) {
			return false;
		}
		
		/*
		 * Deal cards
		 */
		
		/*
		 * Variable to track index
		 * Continually increment, using modulo to loop over players
		 */
		int index = 0;
		
		for(Card card : deck.getDeck()) {
			
			// ignore empty positions
			while(players[(index % players.length)] == null) {
				index++;
			}
			
			// deal card to positions with players
			players[(index % players.length)].addCard(card);
			index++;
		}
		
		// return true to indicate that cards have been dealt
		return true;
	}
	
	/**
	 * Add player to table
	 * @param player
	 * @param pos position in players array
	 * @return
	 */
	public boolean addPlayer(Player player, int pos) {
		
		// if position empty, add player
		if(this.players[pos] == null) {
			this.players[pos] = player;
			this.noPlayers++;
			System.out.println(noPlayers);
			return true;
		}
		
		// return false is position already taken
		return false;

	}
	
	/**
	 * Method to check number of players at the table
	 * @return playerCount
	 */
	public int checkNumPlayers() {

		int playerCount = 0; 
		
		/*
		 * Check how many players are at table
		 */
		for(Player player : players) {
			if(player != null) {
				playerCount++;
			}
		}
		
		return playerCount;
	}
	
	/*
	 * ===================
	 * Deal Methods
	 * ===================
	 */
	
	public boolean drawForAce(){
		
		/*
		 * Deal a single card to the current player
		 */
		System.out.println("Current player: " + this.players[this.currentPlayerPos].getName());
		Player playerToDealTo = this.players[this.currentPlayerPos];
		this.deck.dealSingleCard(playerToDealTo);
		
		/*
		 * Get current player hand
		 */
		ArrayList<Card> playerHand = players[currentPlayerPos].getHand().getHand();
		
		/*
		 * If most recently drawn card is Ace, set current player to dealer
		 * and return true to indicate dealer has been selected
		 */
		if(playerHand.get(playerHand.size() - 1).getValue() == "A") {
			/*
			 * !! need to decide how to handle this
			 */
			dealer = players[currentPlayerPos];
			dealerPos = currentPlayerPos;
			return true;
		}
		
		/*
		 * Increment current player
		 * 
		 * !! can probably replace this with helper method below
		 */
		currentPlayerPos++;
		if(currentPlayerPos >= numPlayers) {
			currentPlayerPos = 0;
		}
		
		/*
		 * Check if next position filled, loop until filled position found
		 */
		while(players[currentPlayerPos] == null) {
			currentPlayerPos++;
			if(currentPlayerPos >= numPlayers) {
				currentPlayerPos = 0;
			}
		}
		
		return false;
	}
	
	public void dealToCurrentPlayer(){
		this.deck.dealSingleCard(this.players[this.currentPlayerPos]);
	}
	
	/*
	 * ===========================
	 * Player access and iteration
	 * ===========================
	 */
	
	public Player dealer() {
		return this.players[this.dealerPos];
	}
	
	public Player currentPlayer() {
		return this.players[this.currentPlayerPos];
	}
	
	public void incrementCurrentPlayer() {
		/*
		 * Increment current player
		 */
		currentPlayerPos++;
		if(currentPlayerPos >= numPlayers) {
			currentPlayerPos = 0;
		}
		
		/*
		 * Check if next position filled, loop until filled position found
		 */
		while(players[currentPlayerPos] == null) {
			currentPlayerPos++;
			if(currentPlayerPos >= numPlayers) {
				currentPlayerPos = 0;
			}
		}
	}
	
	public void resetToDealerPos() {
		this.setCurrentPlayer(this.dealerPos);
	}
	
	public void incrementDealerPos() {
		/*
		 * Increment current player
		 */
		this.dealerPos++;
		if(this.dealerPos >= numPlayers) {
			this.dealerPos = 0;
		}
		
		/*
		 * Check if next position filled, loop until filled position found
		 */
		while(players[dealerPos] == null) {
			dealerPos++;
			if(dealerPos >= numPlayers) {
				dealerPos = 0;
			}
		}
	}
	
//	/*
//	 * Needs updated !!
//	 */
//	public void selectInitialDealer() {
//		this.dealer = players[0];
//	}
	
	
	public void recallPlayerHands() {
		for(Player player : this.players) {
			if(player != null) {
				
				while(player.getHand().getHand().size() > 0) {
					deck.addCard(player.getHand().removeCard(0));
				}
//				player.emptyHand();
			}
			
		}
	}
	
	public void allowStakes(boolean allow) {
		for(Player player: this.players) {
			if(player != null) {
				player.setAbleToChangeStake(allow);
			}
		}
	}
	
	public void lock(){
		tableLock.lock();
	}
	
	public void unlock() {
		tableLock.unlock();
	}
	
	/**
	 * toString method, shows all players hands
	 */
	public String toString() {
		String output = "";
		
		for(Player player : players) {
			output += player + "\n";
		}
		
		return output;
	}


	public Player[] getPlayers() {
		return players;
	}


	public Player getDealer() {
		return dealer;
	}
	
	
	public int getDealerPos() {
		return dealerPos;
	}


	public int getNoPlayers() {
		return noPlayers;
	}

	public Deck getDeck() {
		return deck;
	}


	public String getGameMessage() {
		return gameMessage;
	}


	public void setGameMessage(String gameMessage) {
		this.gameMessage = gameMessage;
	}


	public int getCurrentPlayer() {
		return currentPlayerPos;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayerPos = currentPlayer;
	}

	public void setDealerPos(int dealerPos) {
		this.dealerPos = dealerPos;
	}

	public void setDealer(Player dealer) {
		this.dealer = dealer;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
