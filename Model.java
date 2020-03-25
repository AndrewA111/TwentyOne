import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Model {
	
	private Table table;
	private Deck deck;
	private ArrayList<Player> globalPlayers;
	
	/*
	 * Lock
	 */
	private ReentrantLock modelLock = new ReentrantLock();
	
	
	public Model() {
		
		this.table = new Table();
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
		
//		
//		this.deck.dealInitialCards(table.getPlayers());
//		System.out.println("\n" + this.table);	
//		System.out.println("\n" + this.deck);
		
	}
	
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


