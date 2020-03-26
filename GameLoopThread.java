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
		
		while(true) {
		
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
				System.out.println("Waiting for players to join.");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				this.table.lock();
				numPlayers = this.table.getNoPlayers();
				this.table.unlock();
				
				
				/*
				 * Once numPlayers >= 2, pause for 10s to allow other players to join
				 * 
				 * Carried out in while loop in case player leaves before game starts
				 * 
				 */
				if(numPlayers >= 2) {
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
					 * ! check whether table locked when removing players
					 */
					// check no one left in the interim
					this.table.lock();
					numPlayers = this.table.getNoPlayers();
					
					
					for(Player p : this.table.getPlayers()) {
						if(p != null) {
							p.setAbleToLeave(false);
						}
						
					}
					
					this.table.unlock();
					
					
				}
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
			
			// set player to start from for drawing for ace
			this.table.setInitialCurrentPlayer();
			
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
			 * =======================
			 * Round logic
			 * =======================
			 */
			while(this.table.getNoPlayers() >= 2) {
				

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
//				this.table.recallPlayerHands();
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
				 * ======================================
				 * Initially deal 2 cards to each player
				 * ======================================
				 */
				System.out.println("Dealing cards");
				
				this.table.lock();
				
				
				
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
				
				if(vingtUn) {
					this.table.recallPlayerHands();
					
					/*
					 * ! Add a pause and some indication of 21
					 */
				}
				
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
					 * Recall hands
					 */
					
					this.table.recallPlayerHands();
					
					
					/*
					 * Allow players to vary stake for 10s
					 */
					System.out.println("Round over. Players have 10s to place stake");
					this.table.setGameMessage("Round over. Players have 10s to place stake");
					
					// enable stake-placing
					this.table.allowStakes(true);
					
					for(Player p : this.table.getPlayers()) {
						if(p != null) {
							p.setAbleToLeave(true);
						}
						
					}
					
					
					
					
					
					this.table.unlock();
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					/*
					 * Check if enough players for a new round
					 */
					this.table.lock();
					
					numPlayers = this.table.getNoPlayers();
					
					// disable stake-placing
					this.table.allowStakes(false);
					
					
					for(Player p : this.table.getPlayers()) {
						if(p != null) {
							p.setAbleToLeave(false);
						}
						
					}
					
					this.table.unlock();
				}
			}
		
		
		
		
		
		
		
		}
		
		
	}
}
