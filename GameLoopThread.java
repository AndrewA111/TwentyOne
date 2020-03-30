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
			 * Allow players to join
			 * ! needs to be synchronized?
			 */
			for(Player p : this.model.getGlobalPlayers()) {
				
				// select only players not currently sitting at table
				if(p.getTablePos() == -1) {
					p.setAbleToJoin(true);
				}
			}
			
			// send update to clients
			this.table.sendUpdate();
			
			/*
			 * Wait until 2 players have joined
			 */
			while(numPlayers < 2) {
				
				// if a player remains, let them leave
				for(Player p : this.table.getPlayers()) {
					if(p != null) {
						p.setAbleToLeave(true);
					}
					
				}
				
				// send update to clients
				this.table.sendUpdate();
				
				System.out.println("Waiting for players to join.");
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/*
				 * ! Synchronize?
				 */
				synchronized(this.table) {
					numPlayers = this.table.getNoPlayers();
				}
			
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
					this.table.setGameMessage("10s for players to join");
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
						numPlayers = this.table.getNoPlayers();
						
						/*
						 * Stop players from leaving
						 */
						for(Player p : this.table.getPlayers()) {
							if(p != null) {
								p.setAbleToLeave(false);
							}
							
						}
						
						/*
						 * Stop players from joining
						 */
						for(Player p : this.model.getGlobalPlayers()) {
							
							// select only players not currently sitting at table
							if(p.getTablePos() == -1) {
								p.setAbleToJoin(false);
							}
						}
						
						// send update to clients
						this.table.sendUpdate();
					}
					
					
					
				}
			}
			
			
			
			/*
			 * =============================
			 * Select dealer by drawing lots
			 * =============================
			 */
			
			System.out.println("Selecting dealer");
			this.table.setGameMessage("Selecting dealer");
			
			// send update to clients
			this.table.sendUpdate();
			
			boolean aceFound = false;
			
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
			 * Recall player hands
			 */
			synchronized(this.table) {
				this.table.recallPlayerHands();
			}
			
			// send update to clients
			this.table.sendUpdate();
			
			/*
			 * Shuffle deck
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
			 * Allow players to vary stake for 10s
			 */
			synchronized(this.table) {
				System.out.println("Players have 10s to place stake");
				this.table.setGameMessage("Players have 10s to place stake");
				// disable stake-placing
				this.table.allowStakes(true);
				
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
				
				
				for(Player p : this.table.getPlayers()) {
					if(p != null) {
						p.setAbleToLeave(false);
					}
					
				}

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

					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				
				
				/*
				 * Start from player after dealer
				 */

				this.table.incrementCurrentPlayer();
				
				// boolean to check when everyone played
				boolean dealerHasPlayed = false;
				
				// boolean to store whether a 21 is found
				boolean twentyOne;
				
				synchronized(this.table) {
					// check for 21
					twentyOne = this.table.checkTwentyOne();
					
					if(twentyOne) {
						this.table.recallPlayerHands();
						
						// send update to clients
						this.table.sendUpdate();
						
						/*
						 * ! Add a pause and some indication of 21
						 */
						
						
					}
				}

				
				// if no 21
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
							this.table.setGameMessage(this.table.currentPlayer().getName() + ". Draw or stand? 10s to choose");
		
							// set flags that player is able to select draw/stand
							this.table.currentPlayer().setAbleToDrawOrStand(true);
							
							
//							// check whether player wants to draw or stand
//							playerChoice = (this.table.currentPlayer().getDrawOrStand());
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
							
							// wait until client makes a decision
							synchronized(this.drawStandNotifier) {
								try {
									this.drawStandNotifier.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}
							
							// check whether player wants to draw or stand
							playerChoice = (this.table.currentPlayer().getDrawOrStand());
								
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
							if(playerChoice == 1) {
								System.out.println("Card drawn.");


								synchronized(this.table){
									this.table.getDeck().dealSingleCard((this.table.currentPlayer()));
								}
								
								// reset to undecided
								this.table.currentPlayer().setDrawOrStand(-1);
								

							}

							playerChoice = this.table.currentPlayer().getDrawOrStand();
							
							// send update to clients
							this.table.sendUpdate();

						}
						
						// if this was dealers turn, round finished
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
						
						// send update to clients
						this.table.sendUpdate();
						
						
						

					}
					
					
					synchronized(this.table) {
						this.table.checkingWinnerEndRound();
					}
					
					
					
					/*
					 * Set new dealer
					 */
					this.table.incrementDealerPos();
					
					/*
					 * Recall hands
					 */
					synchronized(this.table) {
						this.table.recallPlayerHands();
					}
					

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
					
					/*
					 * Allow players to join
					 * ! needs to be synchronized?
					 */
					for(Player p : this.model.getGlobalPlayers()) {
						
						// select only players not currently sitting at table
						if(p.getTablePos() == -1 && this.table.getNoPlayers() < 5) {
							p.setAbleToJoin(true);
						}
					}
					
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

				}
			}
		
		
		
		
		
		
		
		}
		
		
	}
}
