
public class GameLoopThread implements Runnable{
	
	private Model model;
	private Table table;
//	private Deck deck;
	
	public GameLoopThread(Model model) {
		this.model = model;
		this.table = model.getTable();
	}
	
	public void run() {
		
		
		/*
		 * 
		 * 
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
		
		System.out.println("10s for players to join");
		this.table.setGameMessage("10s for players to join");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Selecting dealer");
		this.table.setGameMessage("Selecting dealer");
		
		int selectCards = 1;
		
		while(!this.table.drawForAce()) {
			System.out.println(selectCards + " card drawn to select dealer");
			selectCards++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		while(true) {
			System.out.println("The dealer is: " + this.table.getDealer().getName());
			this.table.setGameMessage("The dealer is: " + this.table.getDealer().getName());
			
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
			
//			System.out.println("Dealing cards");
//			this.table.getDeck().dealInitialCards(this.table.getPlayers(), this.table.getDealer());
			
			System.out.println("Players have 10s to place stake");
			this.table.setGameMessage("Players have 10s to place stake");
			System.out.println("Test1");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Test2");
			for(Player player : this.table.getPlayers()) {
				if(player != null) {
					player.setAbleToChangeStake(false);
				}
			}
			System.out.println("Dealing cards");
			this.table.getDeck().dealInitialCards(this.table.getPlayers(), this.table.getDealer());
			System.out.println("Test3");
			this.table.incrementCurrentPlayer();
			System.out.println("Test4");
			boolean dealerHasPlayed = false;
			System.out.println("Test5");
			// check for 21
			boolean vingtUn = this.table.checkVingtUn();
			System.out.println("Test6");
			// if no 21
			if(!vingtUn) {
				System.out.println("Test7");
				
				// start from player after dealer
				this.table.setCurrentPlayer(this.table.getDealerPos());
				this.table.incrementCurrentPlayer();
				
				// loop from player after dealer until dealer has played
				while(!dealerHasPlayed) {
					System.out.println("Test8");
					
					
					//notify who is playing
					System.out.println(this.table.getPlayers()[this.table.getCurrentPlayer()].getName() + ". Draw or stand?");
					this.table.setGameMessage(this.table.getPlayers()[this.table.getCurrentPlayer()].getName() + ". Draw or stand? 10000s to choose");
					System.out.println("Test9");
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
					if(this.table.getPlayers()[this.table.getCurrentPlayer()] == this.table.getDealer()) {
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
				this.table.setCurrentPlayer(this.table.getDealerPos());
				this.table.incrementCurrentPlayer();
				this.table.setDealerPos(this.table.getCurrentPlayer());
				this.table.setDealer(this.table.getPlayers()[this.table.getDealerPos()]);
				
				/*
				 * 5s to new round
				 */
				System.out.println("Round over. New round in 5s.");
				this.table.setGameMessage("Round over. New round in 5s.");
			}
		}
		
		
		
		
		
		
		
		
		
		
	}
}
