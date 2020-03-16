import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class TestPanel extends JPanel {
	
	BufferedImage image;
	
	Card testCard = new Card("A", 'S');
	
	private int minWidthOrHeight;
	
	private Player[] players;
	
	public TestPanel() {
		
		CardDetails cardDetails = new CardDetails();
		
		this.setBackground(new Color(0,102,0));
		
		this.players = createPlayersArray();
		
		
		for(Player p : players) {
			System.out.println(p);
		}
		
		

		try {
//		      image = ImageIO.read(new File("BlankCards.png"));
				image = CardDetails.getBlankCardMap().get('D');
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}
	
	
	@Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.setFont(new Font("Serif", Font.BOLD, 14));
	    
	    this.minWidthOrHeight = Math.min(this.getWidth(), this.getHeight());
	    
	    // get to center
	    g2d.translate(this.getWidth()/2 - image.getWidth()/2, 
	    		this.getHeight()/2 - image.getHeight()/2);
	    
	    // card offset
    	int cardOffset = image.getWidth()/3;
	    
	    for(int i = 0; i < this.players.length; i++) {
	    	if(this.players[i] != null) {
	    		
	    		
	    		int cardNo = 0;
	    		
	    		// translate to first card position
	    		g2d.translate(0, this.minWidthOrHeight/3);
	    		
	    		for(Card card : this.players[i].getHand().getHand()) {
	    			
		    		image = CardDetails.getBlankCardMap().get(card.getType());
		    		g2d.drawImage(image, cardNo * cardOffset, 0, this);
		    		
		    		// draw on top left and bottom right corners
				    g2d.drawString(card.getValue(), image.getWidth()/9 +(cardNo * cardOffset), image.getWidth()/4);
				    g2d.rotate(Math.toRadians(180), image.getWidth()/2, image.getHeight()/2);
				    g2d.drawString(card.getValue(), image.getWidth()/9 - cardNo * cardOffset, image.getWidth()/4);
				    
				    // reverse rotation
				    g2d.rotate(-Math.toRadians(180), image.getWidth()/2, image.getHeight()/2);
		    		
		    		cardNo++;
	    		}
	    		
	    		// translate back to center
			    g2d.translate(0, -this.minWidthOrHeight/3);
			    
	    	}
			    
			    // rotate to next player angle
			    g2d.rotate(Math.toRadians(72 % 360), image.getWidth()/2, image.getHeight()/2);
	    		
	    		
	    		
			    
	    	
	    }
	    
	    
//	    for(int i = 0;  i < 20; i++) {
//	    	
//	    	// card offset
////	    	int cardOffset = image.getWidth()/3;
//	    	
//	    	// translate to image position
//	    	g2d.translate(0, this.minWidthOrHeight/3);
//		    g2d.drawImage(image, (i/5) * cardOffset, 0, this);
//		  
//		    // draw on top left and bottom right corners
//		    g2d.drawString("A", image.getWidth()/9 +((i/5) * cardOffset), image.getWidth()/4);
//		    g2d.rotate(Math.toRadians(180), image.getWidth()/2, image.getHeight()/2);
//		    g2d.drawString("A", image.getWidth()/9 - (i/5) * cardOffset, image.getWidth()/4);
//		    
//		    
//		    
//		    // translate back to center
//		    g2d.translate(0, -this.minWidthOrHeight/3);
//		    
//		    // rotate to next player angle
//		    g2d.rotate(Math.toRadians(72 % 360), image.getWidth()/2, image.getHeight()/2);
//	    }
	    
	    

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
	

}



//Graphics2D g2d = (Graphics2D) g;
//
//g2d.translate(this.getWidth()/2, this.getHeight()/2);

//g2d.rotate(Math.toRadians(this.rotation));



//setBackground(Color.WHITE);
//g2d.drawImage(image, 0, 0, this);
//System.out.println("Test");
//g2d.drawRoundRect(0, 0, 75, 100, 10, 10);

//g2d.translate(-this.getWidth()/2, -this.getHeight()/2);