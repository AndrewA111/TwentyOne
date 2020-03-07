import java.util.ArrayList;

public class Model {
	
	private Table table;
	private Deck deck;
	
	
	public Model() {
		
		this.table = new Table();
		this.newGame();
		
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


	public Table getTable() {
		return table;
	}
	
	public Deck getDeck() {
		return deck;
	}



	public static void main(String[] args) {
		Model model = new Model();
		

	}
}


