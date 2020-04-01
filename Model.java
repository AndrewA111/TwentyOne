import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Model for TwentyOne game
 * @author Andrew
 *
 */
public class Model {
	
	/**
	 * Table
	 */
	private Table table;
	
	/**
	 * Global players list
	 */
	private ArrayList<Player> globalPlayers;
	
	/*
	 * Lock
	 */
	private ReentrantLock modelLock = new ReentrantLock();
	
	
	public Model() {
		
		/*
		 * Create new table
		 */
		this.table = new Table();
		
		/*
		 * Create new global player list
		 */
		this.globalPlayers = new ArrayList<Player>();
		
	}
	
	/**
	 * Method to add a new player to global players list
	 * @param p
	 */
	public void addPlayer(Player p) {
		this.globalPlayers.add(p);
	}
	
	/**
	 * Method to set all players not currently at table able to join, 
	 * so long as table isn't full
	 * @param canJoin
	 */
	public void allowJoining(boolean allow) {
		
		for (Player p : this.globalPlayers) {
			// select only players not currently sitting at table
			if(p.getTablePos() == -1 && this.table.getNoPlayers() < 5) {
				p.setAbleToJoin(allow);
			}
		}
		
	}

	
	public Table getTable() {
		return table;
	}
	
	public ArrayList<Player> getGlobalPlayers() {
		return globalPlayers;
	}

	public void lock() {
		this.modelLock.lock();
	}
	
	public void unlock() {
		this.modelLock.unlock();
	}
	
}


