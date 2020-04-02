/*
 * Thread to run main game logic
 */
public class GameLoopThread implements Runnable{
	
	/*
	 * Game objects
	 */
	private Model model;
	private Table table;
	
	/*
	 * Object to synchronize on and use to notify gameloop 
	 * when a player has selected draw or stand
	 */
	private Object drawStandNotifier;
	
	/*
	 * Constructor
	 */
	public GameLoopThread(Model model, Object drawStandNotifier) {
		this.model = model;
		this.table = model.getTable();
		this.drawStandNotifier = drawStandNotifier;
	}
	
	
	/*
	 * Main game logic
	 */
	public void run() {
		
		while(true) {

			/*
			 * ==============================================
			 * Wait until at least two players have arrived 
			 * then wait 10s to start game
			 * ==============================================
			 */
			
			System.out.println("Waiting for players to arrive");
			this.table.setGameMessage("Waiting for players to arrive");
			
			int numPlayers = 0;
			
			/*
			 * Allow players to join if space available
			 */
			if(this.table.getNoPlayers() < 5) {
				
				// flag for new players
				this.table.setJoinable(true);
				
				// set buttons for existing players
				this.model.allowJoining(true);
			}
			
			// send update to clients
			this.table.sendUpdate();
			
			/*
			 * Wait until 2 players have joined
			 */
			while(numPlayers < 2) {
				
				/**
				 * Wait for players. 
				 * 
				 * waitForPlayers() returns number of players when complete
				 */
				numPlayers = this.waitForPlayers();
				
			}

			
			/*
			 * =================================
			 * Select dealer by drawing for ace
			 * =================================
			 */
			
			System.out.println("Selecting dealer");
			this.table.setGameMessage("Selecting dealer");
			
			// send update to clients
			this.table.sendUpdate();
			
			// boolean to indicate if an ace has been drawn
			boolean aceFound = false;
			
			// show cards to all players for dealer selection
			this.table.cardsVisible(true);
			
			// set player to start from for drawing for ace
			this.table.setInitialCurrentPlayer();
			
			while(!aceFound) {
				
				/*
				 * Draw and check for ace. 
				 * 
				 * When ace drawn, dealer set to that player, 
				 * true returned and loop stops
				 * 
				 * Synchronized to prevent concurrent modification 
				 * exception with collection objects between this 
				 * and writer thread
				 */
				synchronized(this.table) {
					aceFound = this.table.drawForAce();
				}
				
				// send update to clients
				this.table.sendUpdate();
				
				// 0.5s pause for visual dealing effect	
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			/*
			 * Announce dealer
			 */
			System.out.println("The dealer is: " + this.table.dealer().getName());
			this.table.setGameMessage("The dealer is: " + this.table.dealer().getName());
			
			// send update to clients
			this.table.sendUpdate();
			
			// Pause before round
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/*
			 * Recall player hands
			 */
			synchronized(this.table) {
				this.table.recallPlayerHands();
			}
			
			/*
			 * Shuffle deck
			 */
			System.out.println("Unshuffled deck:\n" + this.table.getDeck());
			this.table.getDeck().shuffle();
			System.out.println("Shuffled deck:\n" + this.table.getDeck());


			/*
			 * =====================================
			 * Allow players to vary stake for 10s
			 * =====================================
			 */
			synchronized(this.table) {
				
				System.out.println("Players have 10s to place stake");
				this.table.setGameMessage("Players have 10s to place stake");
				
				// enable stake-placing
				this.table.allowStakes(true);
			}
			
			// send update to clients
			this.table.sendUpdate();
			
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/*
			 * =======================
			 * Round logic
			 * =======================
			 */
			while(this.table.getNoPlayers() >= 2) {
				
				// disable stake-placing
				this.table.allowStakes(false);
				
				// disable leaving
				this.table.allowLeaving(false);
				
				/*
				 *  cards dealt face-down 
				 *  (players can only view their own cards)
				 */
				this.table.cardsVisible(false);

				/*
				 * Announce dealer
				 */
				System.out.println("The dealer is: " + this.table.dealer().getName());
				this.table.setGameMessage("The dealer is: " + this.table.dealer().getName());
				
				// send update to clients
				this.table.sendUpdate();
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				/*
				 * Shuffle
				 */
				System.out.println("Unshuffled deck:\n" + this.table.getDeck());
				this.table.getDeck().shuffle();
				System.out.println("Shuffled deck:\n" + this.table.getDeck());
				
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				/*
				 * ======================================
				 * Initially deal 2 cards to each player
				 * ======================================
				 */
				
				/*
				 * Deal initial cards and sets current player 
				 * to player after dealer
				 */
				this.dealInitialCards();
				
				// boolean to check when everyone played
				boolean dealerHasPlayed = false;
				
				
				/*
				 * ======================================
				 * Check whether anyone has been dealt 
				 * a 'natural 21' 
				 * 
				 * (Score of 21 on first 2 cards)
				 * ======================================
				 */
				
				// boolean to store whether a 21 is found
				boolean twentyOne;
				
				/*
				 * Synchronize to prevent concurrent modification exception
				 */
				synchronized(this.table) {
					// check for 21
					twentyOne = this.table.checkTwentyOne();
				}
					
				if(twentyOne) {
					
					// show all players' cards
					this.table.cardsVisible(true);
					
					// update to show message of who has 21
					this.table.sendUpdate();
					
					try {
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					
				}


				/*
				 * ==============================================
				 * If no ''natural 21' proceed with normal round
				 * ==============================================
				 */
				
				if(!twentyOne) {
					
					// start from player after dealer
					this.table.resetToDealerPos();
					this.table.incrementCurrentPlayer();
					
					// loop from player after dealer until dealer has played
					while(!dealerHasPlayed) {
						
						// int to store players choice code
						int playerChoice;
						
						synchronized(this.table) {
							
							//notify who is playing
							System.out.println(this.table.currentPlayer().getName() + ". Draw or stand?");
							this.table.setGameMessage(this.table.currentPlayer().getName() + ". Draw or stand?");
		
							// set flags that player is able to select draw/stand
							this.table.currentPlayer().setAbleToDrawOrStand(true);
							
							// initialy set -1 to indicate no choice yet
							playerChoice = -1;
							
							// send update to clients
							this.table.sendUpdate();
						}

						
						/*
						 *  Keep checking player choice until player stands:
						 *  
						 * 	-1 undecided
						 * 	1 draw
						 * 	2 stand
						 */
						while(!(playerChoice == 2)) {
							
							/*
							 * playerChoice() waits for message from client 
							 * and returns choice received
							 */
							playerChoice = this.playerChoice();

						}
						
						/*
						 *  if this was dealers turn, round finished
						 */
						if(this.table.currentPlayer() == this.table.dealer()) {
							dealerHasPlayed = true;
						}
						

						
						// reset player to undecided (for next round)
						this.table.currentPlayer().setDrawOrStand(-1);
						
						// disable draw/stand buttons
						this.table.currentPlayer().setAbleToDrawOrStand(false);
						
						// increment player
						this.table.incrementCurrentPlayer();
						
						// send update to clients
						this.table.sendUpdate();

					}
					
					// show all players' cards at end of round
					this.table.cardsVisible(true);
					
					/**
					 * Exchange stakes based on final hands 
					 */
					synchronized(this.table) {
						this.table.checkingWinnerEndRound();
					}
					
					System.out.println("Round over.");
					this.table.setGameMessage("Round over.");
					
					// send update to clients
					this.table.sendUpdate();
					
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					/*
					 * Set new dealer
					 */
					this.table.incrementDealerPos();
	
				}
				
				/*
				 * =====================================
				 * Clear hands and allow joining/staking 
				 * for next round
				 * =====================================
				 */
				
				/*
				 * Recall hands
				 */
				synchronized(this.table) {
					this.table.recallPlayerHands();
				}
				
				/*
				 * Allow players to vary stake for 10s
				 */
				System.out.println("Players have 10s to place stake");
				this.table.setGameMessage("Players have 10s to place stake");
				
				// enable stake-placing
				this.table.allowStakes(true);
				
				// allow players to leave
				this.table.allowLeaving(true);
				
				/*
				 * Allow players to join
				 * ! needs to be synchronized?
				 */
				
				// flag for new players
				this.table.setJoinable(true);
				
				// set buttons for existing players
				this.model.allowJoining(true);
				
				// send update to clients
				this.table.sendUpdate();
				
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				/*
				 * Check if enough players for a new round
				 */
				numPlayers = this.table.getNoPlayers();

				// flag for new players
				this.table.setJoinable(false);
				
				// set buttons for existing players
				this.model.allowJoining(false);
			}

		}
		
		
	}
	
	/**
	 * Method to wait check whether enough players (>=2) are at 
	 * the table to start a game
	 * 
	 * If enough players have joined the table, 
	 * a 10s wait is initiated for further players to join.
	 * 
	 * At the end of the 10s wait, joining/leaving capability is locked. 
	 * Number of players at table is returned (as a check on number of
	 * players remaing after 10s waiting period)
	 * 
	 * @return
	 */
	public int waitForPlayers() {
		
		// if a single player remains, let them leave
		this.table.allowLeaving(true);
		
		// send update to clients
		this.table.sendUpdate();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// variable to store number of players
		int playerCount;
		
		/*
		 * Check number of players
		 */
		synchronized(this.table) {
			playerCount = this.table.getNoPlayers();
		}
	
		/*
		 * Once numPlayers >= 2, wait for 10s to allow other players to join
		 */
		if(playerCount >= 2) {
			
			/*
			 * Once 2 players have joined, wait further 10s to 
			 * start game, to allow more players to join
			 */
			System.out.println("10s for players to join");
			this.table.setGameMessage("10s for players to join");
			
			// send update to clients
			this.table.sendUpdate();
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/*
			 * Check no one left in interim
			 * 
			 * Synchronize to prevent last-minute leaving/joining
			 */
			synchronized(this.table) {

				/*
				 * Stop players from leaving
				 */
				this.table.allowLeaving(false);
				
				/*
				 * Stop players from joining
				 */
				
				// flag for new players
				this.table.setJoinable(false);
				
				// set buttons for existing players
				this.model.allowJoining(false);
				
				
				
				/*
				 *  check number players at table
				 */
				playerCount = this.table.getNoPlayers();
				
				// send update to clients
				this.table.sendUpdate();
			}
	
		}
		
		return playerCount;
	}
	
	/**
	 * Method to deal initial cards and set current 
	 * player to player after dealer
	 */
	public void dealInitialCards() {
		System.out.println("Dealing cards");
		
		// reset to dealer pos
		this.table.resetToDealerPos();
		
		// count how many rounds of cards given out
		int loops = 0;
		
		// deal two cards per player
		while(loops < 2) {
			
			synchronized(this.table) {
				this.table.incrementCurrentPlayer();
				this.table.dealToCurrentPlayer();
				
				// dealer gets card last, so increment loops
				if(this.table.currentPlayer() == this.table.dealer()) {
					loops++;
				}
				
				// send update to clients
				this.table.sendUpdate();
			}

			// 0.5s pause for visual dealing effect	
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		// Start from player after dealer
		this.table.incrementCurrentPlayer();
	}
	
	/**
	 * Method to wait for the client to submit a draw/stand choice.
	 * 
	 * Makes the appropriate model updates then returns choice
	 * 
	 * @return -1 (indicates to repeat), 2 (indicates stand/turn over) 
	 */
	public int playerChoice() {
		// wait until client makes a decision
		synchronized(this.drawStandNotifier) {
			try {
				this.drawStandNotifier.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// check whether player wants to draw or stand
		int choice = (this.table.currentPlayer().getDrawOrStand());
			
			try {
				/*
				 * 0.1s pause to limit loop rate
				 */
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("pong");
		
		/*
		 * If player selects to draw, deal card
		 */
		if(choice == 1) {
			System.out.println("Card drawn.");

			// deal card
			synchronized(this.table){
				
				this.table.getDeck().dealSingleCard((this.table.currentPlayer()));
				
				// check if bust (>= 21)
				if(this.table.currentPlayer().getHand().minLegalValue() == -1) {
					
					// sent to stand (end round)
					this.table.currentPlayer().setDrawOrStand(2);
				}
				// else set player to undecided for next loop
				else {

					this.table.currentPlayer().setDrawOrStand(-1);
				}
			}
			
			
			

		}
		
		// check player choice status
		choice = this.table.currentPlayer().getDrawOrStand();
		
		// send update to clients
		this.table.sendUpdate();
		
		return choice;
	}
}
