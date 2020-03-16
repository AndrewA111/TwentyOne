import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class TestFrame extends JFrame{
	
	public TestFrame() {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(900, 700);

		
		HandsPanel testPanel = new HandsPanel(createPlayersArray());
		this.add(testPanel);
		
		
		this.setVisible(true);
	}

		
		
	public Player[] createPlayersArray() {
		
		Player[] p = new Player[5];
		Player player1 = new Player(1, "Andrew", 1);
		Player player2 = new Player(2, "John", 2);
		Player player3 = new Player(3, "Aaron", 3);
		Player player4 = new Player(4, "Rebecca", 4);
		Player player5 = new Player(5, "Mike", 5);
		
		p[0] = player1;
		p[1] = player2;
		p[2] = player3;
		p[3] = player4;
		p[4] = player5;
		
		p[0].addCard(new Card("A", 'S'));
		p[0].addCard(new Card("K", 'S'));
		p[1].addCard(new Card("8", 'D'));
		p[1].addCard(new Card("9", 'C'));
		p[2].addCard(new Card("2", 'S'));
		p[2].addCard(new Card("5", 'H'));
		p[3].addCard(new Card("7", 'D'));
		p[3].addCard(new Card("3", 'D'));
		p[4].addCard(new Card("J", 'C'));
		p[4].addCard(new Card("Q", 'H'));
		
		return p;
	}
		
		

	
	public static void main(String[] args) {
		JFrame TestFrame = new TestFrame();
	}
	
}
