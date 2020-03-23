/*
 * Thread to run main game logic
 */
public class GameLoopThread implements Runnable{
	
	/*
	 * Game objects
	 */
	private Model model;
	private Table table;
//	private Deck deck;
	
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
		System.out.println("Waiting for players to arrive");
		this.table.setGameMessage("Waiting for players to arrive");
		
		while(this.table.getNoPlayers() < 2) {
			System.out.println("Still waiting");
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		/*
		 * Once 2 players have joined, wait further 10s to start game
		 */
		System.out.println("10s for players to join");
		this.table.setGameMessage("10s for players to join");
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
		System.out.println("Selecting dealer");
		this.table.setGameMessage("Selecting dealer");
		
		int selectCards = 1;
		
		while(!this.table.drawForAce()) {
			System.out.println(selectCards + " card drawn to select dealer");
			selectCards++;
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
			System.out.println("The dealer is: " + this.table.dealer().getName());
			this.table.setGameMessage("The dealer is: " + this.table.dealer().getName());
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			this.table.recallPlayerHands();
			System.out.println(this.table.getDeck());
			this.table.getDeck().shuffle();
			System.out.println(this.table.getDeck());
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

			
			System.out.println("Players have 10s to place stake");
			this.table.setGameMessage("Players have 10s to place stake");
			
			// enable stake-placing
			this.table.allowStakes(true);
			
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// disable stake-placing
			this.table.allowStakes(false);
			

			System.out.println("Dealing cards");
			/*
			 * ! probably shouldn't have to pass these arguments
			 */
			this.table.getDeck().dealInitialCards(this.table.getPlayers(), this.table.dealer());
			
			
			/*
			 * ! is delear set before this increment somewhere?
			 */
			this.table.incrementCurrentPlayer();
			
			// boolean to check when everyone played
			boolean dealerHasPlayed = false;

			// check for 21
			/*
			 * ! need to add some message if 21
			 */
			boolean vingtUn = this.table.checkVingtUn();

			
			// if no 21
			if(!vingtUn) {
				
				// start from player after dealer
				this.table.resetToDealerPos();
				this.table.incrementCurrentPlayer();
				
				// loop from player after dealer until dealer has played
				while(!dealerHasPlayed) {
					System.out.println("Test8");
					
					
					//notify who is playing
					System.out.println(this.table.currentPlayer().getName() + ". Draw or stand?");
					this.table.setGameMessage(this.table.currentPlayer().getName() + ". Draw or stand? 10s to choose");

					// set flags that player is to select
					this.table.getPlayers()[this.table.getCurrentPlayer()].setAbleToDrawOrStand(true);
					
					/*
					 * Keep looping until player stands
					 */
					while(!(this.table.getPlayers()[this.table.getCurrentPlayer()].getDrawOrStand() == 2)) {
							
							System.out.println("-1");
							try {
								/*
								 * 0.1s pause to limit no of loops
								 */
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						
						/*
						 * If player selects to draw
						 */
						if(this.table.getPlayers()[this.table.getCurrentPlayer()].getDrawOrStand() == 1) {
							System.out.println("1");
							this.table.getDeck().dealSingleCard((this.table.getPlayers()[this.table.getCurrentPlayer()]));
							this.table.getPlayers()[this.table.getCurrentPlayer()].setDrawOrStand(-1);
						}
					}
					
					// if this was dealers turn, round finished
					if(this.table.getPlayers()[this.table.getCurrentPlayer()] == this.table.dealer()) {
						dealerHasPlayed = true;
					}
					
					System.out.println("2");
					this.table.getPlayers()[this.table.getCurrentPlayer()].setDrawOrStand(-1);
					this.table.getPlayers()[this.table.getCurrentPlayer()].setAbleToDrawOrStand(false);
					this.table.incrementCurrentPlayer();
				}
				
				System.out.println("Test last");
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
			}
		}
		
		
		
		
		
		
		
		
		
		
	}
}
