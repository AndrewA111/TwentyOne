import java.util.Iterator;

public class Server {
	public static void main(String[] args) {

		Deck deck = new Deck();
		
		Player player1 = new Player(1, "Andrew");
		Player player2 = new Player(2, "John");
		Player player3 = new Player(3, "Aaron");
		Player player4 = new Player(4, "Rebecca");
		Player player5 = new Player(5, "Mike");
		
		Table table = new Table();
		
		table.addPlayer(player1);
		table.addPlayer(player2);
		table.addPlayer(player3);
		table.addPlayer(player4);
		table.addPlayer(player5);
		
		table.dealAll(deck);
		
		System.out.println(table);
		
		System.out.println("Original deck:\n" + deck);
		
		deck.shuffle();
		
		System.out.println("\nShuffled deck:\n" + deck);
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		
//		Iterator<Card> it = deck.getDeck().iterator();
//		
//		for(int i = 0; i <100; i++) {
//			if(it.hasNext()) {
//				System.out.println(it.next());
//			}
//			else {
//				it = deck.getDeck().iterator();
//				System.out.println(it.next());
//			}
//			
//		}
	}
}
