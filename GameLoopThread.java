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
	 * Constructor
	 */
	public GameLoopThread(Model model) {
		this.model = model;
		this.table = model.getTable();
	}
	
	/*
	 * Main game logic
	 */
	public void run() {
		
		
		/*
		 * ==============================================
		 * Wait until at least two players have arrived 
		 * then wait 10s to start game
		 * ==============================================
		 */
		this.table.lock();
		System.out.println("Waiting for players to arrive");
		this.table.setGameMessage("Waiting for players to arrive");
		this.table.unlock();
		
		int numPlayers = 0;
		
		/*
		 * Wait until 2 players have joined
		 */
		while(numPlayers < 2) {
			System.out.println("Still waiting");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.table.lock();
			numPlayers = this.table.getNoPlayers();
			this.table.unlock();
		}
		
		/*
		 * Once 2 players have joined, wait further 10s to 
		 * start game, to allow more players to join
		 */
		System.out.println("10s for players to join");
		this.table.lock();
		this.table.setGameMessage("10s for players to join");
		this.table.unlock();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		/*
		 * =============================
		 * Select dealer by drawing lots
		 * =============================
		 */
		
		table.lock();
		System.out.println("Selecting dealer");
		this.table.setGameMessage("Selecting dealer");
		table.unlock();
		
		boolean aceFound = false;
		
		while(!aceFound) {
			
			/*
			 * Draw and check for ace. 
			 * 
			 * When ace drawn, dealer set to that player, 
			 * true returned and loop stops
			 */
			this.table.lock();
			aceFound = this.table.drawForAce();
			this.table.unlock();
				
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*
		 * =======================
		 * Round logic
		 * =======================
		 */
		while(true) {
			
			/*
			 * Announce dealer
			 */
			this.table.lock();
			System.out.println("The dealer is: " + this.table.dealer().getName());
			this.table.setGameMessage("The dealer is: " + this.table.dealer().getName());
			this.table.unlock();
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/*
			 * Recall player hands and shuffle
			 */
			this.table.lock();
			this.table.recallPlayerHands();
			System.out.println("Unshuffled deck:\n" + this.table.getDeck());
			this.table.getDeck().shuffle();
			System.out.println("Shuffled deck:\n" + this.table.getDeck());
			this.table.unlock();
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			/*
			 * Allow players to vary stake for 10s
			 */
			this.table.lock();
			System.out.println("Players have 10s to place stake");
			this.table.setGameMessage("Players have 10s to place stake");
			
			// enable stake-placing
			this.table.allowStakes(true);
			this.table.unlock();
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			
			/*
			 * ======================================
			 * Initially deal 2 cards to each player
			 * ======================================
			 */
			System.out.println("Dealing cards");
			
			this.table.lock();
			
			// disable stake-placing
			this.table.allowStakes(false);
			
			// reset to dealer pos
			this.table.resetToDealerPos();
			this.table.unlock();
			
			// count how many rounds of cards given out
			int loops = 0;
			
			// deal two cards per player
			while(loops < 2) {
				
				this.table.lock();
				this.table.incrementCurrentPlayer();
				this.table.dealToCurrentPlayer();
				
				// dealer gets card last, so increment loops
				if(this.table.currentPlayer() == this.table.dealer()) {
					loops++;
				}
				this.table.unlock();
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
			
			/*
			 * Start from player after dealer
			 */
			this.table.lock();
			this.table.incrementCurrentPlayer();
			
			// boolean to check when everyone played
			boolean dealerHasPlayed = false;

			// check for 21
			boolean vingtUn = this.table.checkVingtUn();
			
			this.table.unlock();
			
			// if no 21
			if(!vingtUn) {
				
				// start from player after dealer
				this.table.lock();
				this.table.resetToDealerPos();
				this.table.incrementCurrentPlayer();
				this.table.unlock();
				
				// loop from player after dealer until dealer has played
				while(!dealerHasPlayed) {
					
					this.table.lock();
					//notify who is playing
					System.out.println(this.table.currentPlayer().getName() + ". Draw or stand?");
					this.table.setGameMessage(this.table.currentPlayer().getName() + ". Draw or stand? 10s to choose");

					// set flags that player is able to select draw/stand
					this.table.currentPlayer().setAbleToDrawOrStand(true);
					
					
					// check whether player wants to draw or stand
					int playerChoice = (this.table.currentPlayer().getDrawOrStand());
					
					this.table.unlock();
					
					/*
					 *  Keep checking player choice until player stands:
					 *  
					 * 	-1 undecided
					 * 	1 draw
					 * 	2 stand
					 */
					while(!(playerChoice == 2)) {
							
							try {
								/*
								 * 0.1s pause to limit loop rate
								 */
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

						
						/*
						 * If player selects to draw, deal card
						 */
						if(playerChoice == 1) {
							System.out.println("Card drawn.");
							this.table.lock();
							this.table.getDeck().dealSingleCard((this.table.currentPlayer()));
							
							// reset to undecided
							this.table.currentPlayer().setDrawOrStand(-1);
							this.table.unlock();
						}
						
						this.table.lock();
						playerChoice = this.table.currentPlayer().getDrawOrStand();
						this.table.unlock();
					}
					
					// if this was dealers turn, round finished
					this.table.lock();
					if(this.table.currentPlayer() == this.table.dealer()) {
						dealerHasPlayed = true;
					}
					
					
					System.out.println("Stand.");
					
					// reset player to undecided (for next round)
					this.table.currentPlayer().setDrawOrStand(-1);
					
					// disable draw/stand buttons
					this.table.currentPlayer().setAbleToDrawOrStand(false);
					
					// increment player
					this.table.incrementCurrentPlayer();
					
					this.table.unlock();
				}
				
				
				this.table.lock();
				this.table.checkingWinnerEndRound();
				
				
				/*
				 * Set new dealer
				 */
				this.table.incrementDealerPos();
				
				/*
				 * 5s to new round
				 */
				System.out.println("Round over. New round in 5s.");
				this.table.setGameMessage("Round over. New round in 5s.");
				this.table.unlock();
				
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		
		
		
		
		
		
		
		
	}
}
