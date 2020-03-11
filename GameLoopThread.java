
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
		
		System.out.println("Dealing cards");
		this.table.getDeck().dealInitialCards(this.table.getPlayers(), this.table.getDealer());
		
		System.out.println("Players have 10s to place stake");
		this.table.setGameMessage("Players have 10s to place stake");
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Player player : this.table.getPlayers()) {
			if(player != null) {
				player.setAbleToChangeStake(false);
			}
		}
		
		this.table.incrementCurrentPlayer();
		
		boolean dealerHasPlayed = false;
		
		while(!dealerHasPlayed) {
			
			//notify who is playing
			System.out.println(this.table.getPlayers()[this.table.getCurrentPlayer()].getName() + ". Draw or stand?");
			this.table.setGameMessage(this.table.getPlayers()[this.table.getCurrentPlayer()].getName() + ". Draw or stand? 10000s to choose");
			
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
		
		
		
		
		
		
		
		
	}
}
