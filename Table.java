import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.locks.ReentrantLock;

public class Table {
	
	private Player[] players;
	private Player dealer;
	private Deck deck;
	private String gameMessage;
	
	private static final int numPlayers = 5;
	
	private int currentPlayer;
	private int dealerPos;
	
	private int noPlayers;
	
	/*
	 * Lock
	 */
	private ReentrantLock tableLock = new ReentrantLock();
	
	public Table() {
		
		// set number of places at table
		this.players = new Player[numPlayers];
		this.currentPlayer = 0;
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
		this.currentPlayer = this.dealerPos;
		this.incrementCurrentPlayer();
		
		
		/*
		 * if dealer has 21
		 */
		if(this.players[dealerPos].getHand().maxLegalValue() == 21) {
			
			System.out.println("Dealer has 21!");
			this.gameMessage = "Dealer has 21!";
			while(currentPlayer !=  dealerPos) {
				
				// if player doesn't have 21 pay dealer
				if(!(this.players[currentPlayer].getHand().maxLegalValue() == 21)) {
					this.players[currentPlayer].payStake(this.players[dealerPos]);
				}
				
				this.incrementCurrentPlayer();
			}
			
			return true;
		}
		
		/*
		 * Else (dealer does not have 21)
		 */
		else {
			while(currentPlayer !=  dealerPos) {
				/* 
				 * If a player has 21 all other players who 
				 * don't also have 21 pay this player
				 * 
				 * In the case another player has 21, they do not pay this 
				 * player. However, all other players still player this player
				 * as they have positional advantage (they come first after dealer)
				 * 
				 */
				if((this.players[currentPlayer].getHand().maxLegalValue() == 21)) {
					
					// store winner pos
					int winnerPos = currentPlayer;
					this.incrementCurrentPlayer();
					
					
					System.out.println("Player " + winnerPos + " has 21!");
					this.gameMessage = "Player " + winnerPos + " has 21!";
					
					while(currentPlayer != winnerPos) {
						// if player doesn't have 21 pay winning player
						if(!(this.players[currentPlayer].getHand().maxLegalValue() == 21)) {
							this.players[currentPlayer].payStake(this.players[winnerPos]);
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
		
		this.currentPlayer = this.dealerPos;
		this.incrementCurrentPlayer();
		
		while(currentPlayer != dealerPos) {
			
			// if player bust
			if(this.players[currentPlayer].getHand().maxLegalValue() == -1) {
				this.players[currentPlayer].payStake(this.players[dealerPos]);
			}
			// if player has greater value than dealer
			else if(this.players[currentPlayer].getHand().maxLegalValue() >
					this.players[this.dealerPos].getHand().maxLegalValue()) {
				this.players[dealerPos].payStake(this.players[currentPlayer]);
			}
			// if player has lower value than dealer
			else if(this.players[currentPlayer].getHand().maxLegalValue() >
					this.players[this.dealerPos].getHand().maxLegalValue()) {
				this.players[currentPlayer].payStake(this.players[dealerPos]);
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
	
	public boolean drawForAce(){
		
		/*
		 * Deal a single card to the current player
		 */
		System.out.println("Current player: " + this.players[this.currentPlayer].getName());
		Player playerToDealTo = this.players[this.currentPlayer];
		this.deck.dealSingleCard(playerToDealTo);
		
		/*
		 * Get current player hand
		 */
		ArrayList<Card> playerHand = players[currentPlayer].getHand().getHand();
		
		/*
		 * If most recently drawn card is Ace, set current player to dealer
		 * and return true to indicate dealer has been selected
		 */
		if(playerHand.get(playerHand.size() - 1).getValue() == "A") {
			/*
			 * !! need to decide how to handle this
			 */
			dealer = players[currentPlayer];
			dealerPos = currentPlayer;
			return true;
		}
		
		/*
		 * Increment current player
		 * 
		 * !! can probably replace this with helper method below
		 */
		currentPlayer++;
		if(currentPlayer >= numPlayers) {
			currentPlayer = 0;
		}
		
		/*
		 * Check if next position filled, loop until filled position found
		 */
		while(players[currentPlayer] == null) {
			currentPlayer++;
			if(currentPlayer >= numPlayers) {
				currentPlayer = 0;
			}
		}
		
		return false;
	}
	
	public void incrementCurrentPlayer() {
		/*
		 * Increment current player
		 */
		currentPlayer++;
		if(currentPlayer >= numPlayers) {
			currentPlayer = 0;
		}
		
		/*
		 * Check if next position filled, loop until filled position found
		 */
		while(players[currentPlayer] == null) {
			currentPlayer++;
			if(currentPlayer >= numPlayers) {
				currentPlayer = 0;
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
		return currentPlayer;
	}

	public void setCurrentPlayer(int currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public void setDealerPos(int dealerPos) {
		this.dealerPos = dealerPos;
	}

	public void setDealer(Player dealer) {
		this.dealer = dealer;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

}
