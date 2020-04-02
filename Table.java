import java.util.ArrayList;

/**
 * Class to represent table for TwentOne game
 * @author Andrew
 *
 */
public class Table {
	
	/**
	 * Array of players at table
	 */
	private Player[] players;
	
	
	/**
	 * Deck of cards
	 */
	private Deck deck;
	
	/**
	 * Game message to be displayed
	 */
	private String gameMessage;
	
	/**
	 * Number of players allowed at table
	 */
	private static final int numPlayers = 5;
	
	/**
	 * Integer to track position of active player
	 */
	private int currentPlayerPos;
	
	/**
	 * Integer to track position of the dealer
	 */
	private int dealerPos;
	
	/**
	 * Integer to track number of players at table
	 */
	private int noPlayers;
	
	/**
	 * Boolean to indicate whether game is at a 
	 * stage where table can be joined
	 */
	private boolean joinable;
	
	/*
	 * Method to call notifyAll() on this table object
	 * 
	 * This notifies the waiting ServerWriter threads to 
	 * continue and send an updated game state to clients
	 */
	public synchronized void sendUpdate() {
		this.notifyAll();
	}
	
	/**
	 * Constructor
	 */
	public Table() {
		
		// set number of places at table
		this.players = new Player[numPlayers];
		
		// initially set current player position to 0
		this.currentPlayerPos = 0;
		
		// table is empty to start
		this.noPlayers = 0;
		
		/* 
		 * create a deck and shuffle
		 */
		this.deck = new Deck();
		System.out.println("Unshuffled deck:\n" + this.deck);
		this.deck.shuffle();
		System.out.println("Unshuffled deck:\n" + this.deck);
		this.gameMessage = null;
		
		// initially no dealer
		this.dealerPos = -1;
		
		// allow joining
		this.joinable = true;
		
		/*
		 * Initialize hashmap for valuing hands
		 */
		CardDetails cardDetails = new CardDetails();
	}
	
	/*
	 * =====================================
	 * players array access and manipulation
	 * =====================================
	 */
	
	
	/**
	 * Add player to table
	 * @param player
	 * @param pos position in players array
	 * @return true if player added, false otherwise
	 */
	public boolean addPlayer(Player player, int pos) {
		
		// if position empty, add player
		if(this.players[pos] == null) {
			this.players[pos] = player;
			this.noPlayers++;
			return true;
		}
		
		// return false is position already taken
		return false;

	}
	
	/**
	 * Add player to first available position
	 * @param player
	 * @return true if player added, false otherwise
	 */
	public boolean addPlayer(Player player) {
		
		// loop through positions in players array
		for(int i = 0; i <this.players.length; i++) {
			
			// when empty space found
			if(this.players[i] == null) {
				
				// add player and set position
				this.players[i] = player;
				player.setTablePos(i);
				
				// set flags on player
				player.setAbleToJoin(false);
				player.setAbleToLeave(true);
				
				// increment noPlayers
				this.noPlayers++;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Remove player from a given position
	 * @param pos player position
	 */
	public void removePlayer(int pos) {
		
		// if player is dealer increment dealer position
		if(pos == this.dealerPos) {
			this.incrementDealerPos();
		}
		
		// set player flags
		this.players[pos].setAbleToChangeStake(false);
		this.players[pos].setAbleToDrawOrStand(false);
		this.players[pos].setAbleToLeave(false);
		this.players[pos].setAbleToJoin(true);
		
		// indicate player no longer at table
		this.players[pos].setTablePos(-1);
		
		// remove table reference to player
		this.players[pos] = null;
		
		// update no players
		this.noPlayers--;
	}
	
	
	/**
	 * Method to get player in dealer position
	 * @return dealer
	 */
	public Player dealer() {
		return this.players[this.dealerPos];
	}
	
	/**
	 * Method to get player in currentPlayer position
	 * @return currentPlayer
	 */
	public Player currentPlayer() {
		return this.players[this.currentPlayerPos];
	}
	
	
	/**
	 * Method to increment currentPlayer position
	 */
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
	
	/**
	 * Method to set initial current player
	 */
	public void setInitialCurrentPlayer() {
		
		// start at position 0
		currentPlayerPos = 0;
		
		// increment until player found
		if(this.players[this.currentPlayerPos] == null) {
			this.incrementCurrentPlayer();
		}
	}
	
	/**
	 * Method to set currentPlayerPos to dealer position
	 */
	public void resetToDealerPos() {
		this.setCurrentPlayer(this.dealerPos);
	}
	
	/**
	 * Method to increment dealer position
	 */
	public void incrementDealerPos() {
		
		// unset flag for current dealer
		this.players[dealerPos].setDealer(false);
		
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
		
		// set flag for new dealer
		this.players[dealerPos].setDealer(true);
	}
	
	/**
	 * Method to recall the hands of all players at the table
	 */
	public void recallPlayerHands() {
		
		// for all positions in players array
		for(Player player : this.players) {
			
			// if position occupied
			if(player != null) {
				
				// while cards in hand
				while(player.getHand().getHand().size() > 0) {
					
					// remove first card and place back in deck
					deck.addCard(player.getHand().removeCard(0));
				}
			}
			
		}
	}
	
	/**
	 * Method to set all players at table's 
	 * ableToChangeStake flags to given boolean
	 * @param allow boolean for whether staking allowed or not
	 */
	public void allowStakes(boolean allow) {
		for(Player player: this.players) {
			if(player != null) {
				player.setAbleToChangeStake(allow);
			}
		}
	}
	
	/**
	 * Method to set all players at table's ableToLeave flags
	 * @param canLeave
	 */
	public void allowLeaving(boolean canLeave) {
		for(Player player: this.players) {
			if(player != null) {
				player.setAbleToLeave(canLeave);
			}
		}
	}
	
	/**
	 * Method to set all players'cards visibility flag
	 * @param visible
	 */
	public void cardsVisible(boolean visible) {
		for(Player player: this.players) {
			if(player != null) {
				player.setCardsVisible(visible);
			}
		}
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
			
			// check dealerPos has been set
			if(this.dealerPos != -1) {
				
				// reset current dealer flag to false
				this.players[this.dealerPos].setDealer(false);
			}
			
			// set new dealer
			dealerPos = currentPlayerPos;
			this.players[this.dealerPos].setDealer(true);
			return true;
		}
		
		/*
		 * Increment current player
		 */
		this.incrementCurrentPlayer();
		
		return false;
	}
	
	/**
	 * Method to deal a card to current player
	 */
	public void dealToCurrentPlayer(){
		this.deck.dealSingleCard(this.players[this.currentPlayerPos]);
	}
	
	
	/*
	 * ========================
	 * Round result methods
	 * ========================
	 */
	
	/**
	 * Method to check if a player has 21 on initial draw
	 * @return true if 21 drawn, false otherwise
	 */
	public boolean checkTwentyOne() {
		
		// start at pos after dealer
		this.currentPlayerPos = this.dealerPos;
		this.incrementCurrentPlayer();
		
		
		/*
		 * if dealer has 21
		 */
		if(this.players[dealerPos].getHand().maxLegalValue() == 21) {
			
			System.out.println("Dealer has 21!");
			this.gameMessage = "Dealer has 21!";
			
			// loop through other players
			while(currentPlayerPos !=  dealerPos) {
				
				// if player doesn't also have 21, pay dealer
				if(!(this.players[currentPlayerPos].getHand().maxLegalValue() == 21)) {
					
					// pay dealer, checking if player has sufficient funds to proceed
					boolean fundsRemaining = this.players[currentPlayerPos].payDoubleOwnStake(this.players[dealerPos]);
					
					if(!fundsRemaining) {
						
						// remove player if not enough funds to stake
						this.removePlayer(currentPlayerPos);
					}
					
					
				}
				
				// increment to player after dealer for start of next round
				this.incrementCurrentPlayer();
			}
			
			return true;
		}
		
		/*
		 * Else (dealer does not have 21)
		 */
		else {
			
			// loop through non-dealer players
			while(currentPlayerPos !=  dealerPos) {
				
				/* 
				 * If a player has 21 all other players who 
				 * don't also have 21 pay this player
				 * 
				 * In the case another player has 21, they do not pay this 
				 * player. However, all other players still player this player
				 * as this player has positional advantage (they come first after dealer)
				 * 
				 */
				if((this.players[currentPlayerPos].getHand().maxLegalValue() == 21)) {
					
					// store winner pos
					int winnerPos = currentPlayerPos;
					
					// remove flag on current dealer
					this.dealer().setDealer(false);
					
					// winning player is new dealer
					this.dealerPos = winnerPos;
					
					// set flag for new dealer
					this.dealer().setDealer(true);

					System.out.println(this.players[winnerPos].getName() + " has 21!");
					this.gameMessage = this.players[winnerPos].getName()  + " has 21!";
					
					/*
					 * Check hands of remaining players
					 */
					
					// move to next player
					this.incrementCurrentPlayer();
					
					// loop through remaining players
					while(currentPlayerPos != winnerPos) {
						
						// if player doesn't have 21 pay winning player
						if(!(this.players[currentPlayerPos].getHand().maxLegalValue() == 21)) {
							
							/*
							 *  pay winner double their stake, checking if paying player 
							 *  has sufficient funds to proceed
							 */
							boolean fundsRemaining = this.players[currentPlayerPos].payDoubleOpponentsStake(this.players[winnerPos]);
							
							if(!fundsRemaining) {
								
								// remove player if not enough funds to stake
								this.removePlayer(currentPlayerPos);
							}
						}
						this.incrementCurrentPlayer();
					}
					
					return true;
				}
				
				// increment to player after new dealer for start of next round
				this.incrementCurrentPlayer();
					
			}
		}
		
		/*
		 * No 21s
		 */
		return false;
	}
	
	/**
	 * Method to assess result between players and dealer at end of round
	 */
	public void checkingWinnerEndRound() {
		
		/*
		 * Start from position after dealer
		 */
		this.currentPlayerPos = this.dealerPos;
		this.incrementCurrentPlayer();
		
		/*
		 * Variable to check if dealer busts
		 */
		boolean dealerBust = false;
		/*
		 * Loop through all players except dealer
		 */
		while((currentPlayerPos != dealerPos) && !dealerBust) {
			
			// if player bust
			if(this.players[currentPlayerPos].getHand().maxLegalValue() == -1) {
				
				// pay dealer, checking if player has sufficient funds to proceed
				boolean fundsRemaining = this.players[currentPlayerPos].payOwnStake(this.players[dealerPos]);
				
				if(!fundsRemaining) {
					
					// remove player if not enough funds to stake
					this.removePlayer(currentPlayerPos);
				}
			}
			// if player has greater value than dealer
			else if(this.players[currentPlayerPos].getHand().maxLegalValue() >
					this.players[this.dealerPos].getHand().maxLegalValue()) {
				
				// dealer pays player
				boolean fundsRemaining = this.players[dealerPos].payOpponentsStake(this.players[currentPlayerPos]);
				
				// if dealer has insufficient funds to play
				if(!fundsRemaining) {
					
					// flag dealer bust, so round finishes
					dealerBust = true;
					
					System.out.println("Dealer out of funds! Round over.");
					this.gameMessage = "Dealer out of funds! Round over.";
					
					this.cardsVisible(true);
					this.sendUpdate();
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					// remove dealer
					this.removePlayer(dealerPos);
				}
			}
			// if player has lower value than dealer
			else if(this.players[currentPlayerPos].getHand().maxLegalValue() <
					this.players[this.dealerPos].getHand().maxLegalValue()) {
				
				// pay dealer, checking if player has sufficient funds to proceed
				boolean fundsRemaining = this.players[currentPlayerPos].payOwnStake(this.players[dealerPos]);
				
				if(!fundsRemaining) {
					
					// remove player if not enough funds to stake
					this.removePlayer(currentPlayerPos);
				}
			}
			
			this.incrementCurrentPlayer();
		}
		
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

	/**
	 * Getter for players array
	 * @return players players array
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	/**
	 * Getter for dealer position
	 * @return dealerPos
	 */
	public int getDealerPos() {
		return dealerPos;
	}
	
	/**
	 * Setter for dealerPos
	 * @param dealerPos
	 */
	public void setDealerPos(int dealerPos) {
		this.dealerPos = dealerPos;
	}

	/**
	 * Getter for number of players
	 * @return noPlayers
	 */
	public int getNoPlayers() {
		return noPlayers;
	}
	
	/**
	 * Getter for deck
	 * @return deck
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * Getter for game message
	 * @return game Message
	 */
	public String getGameMessage() {
		return gameMessage;
	}

	/**
	 * Setter for game message
	 * @param gameMessage
	 */
	public void setGameMessage(String gameMessage) {
		this.gameMessage = gameMessage;
	}

	/**
	 * Getter for currentPlayerPos
	 * @return currentPlayerPos
	 */
	public int getCurrentPlayer() {
		return currentPlayerPos;
	}
	
	/**
	 * Setter for currentPlayerPos
	 * @param currentPlayer
	 */
	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayerPos = currentPlayer;
	}
	
	/**
	 * Getter for joinable
	 * @return joinable
	 */
	public boolean isJoinable() {
		return joinable;
	}
	
	/**
	 * Setter for joinable
	 * @param joinable
	 */
	public void setJoinable(boolean joinable) {
		this.joinable = joinable;
	}
	
	
	
	
	
	
	

	

}
