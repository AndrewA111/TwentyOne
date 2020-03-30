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
	private Deck deck;
	
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
	
	/*
	 * This needs updated to that dealer selection is first thing that happens
	 */
	public void newGame() {
		this.deck = new Deck();
		System.out.println(this.deck);
		this.deck.shuffle();
		System.out.println(this.deck);

		
	}
	
	/**
	 * Method to add a new player to global players list
	 * @param p
	 */
	public void addPlayer(Player p) {
		this.globalPlayers.add(p);
	}

	
	public Table getTable() {
		return table;
	}
	
	public Deck getDeck() {
		return deck;
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


	public static void main(String[] args) {
		Model model = new Model();
		

	}
}


