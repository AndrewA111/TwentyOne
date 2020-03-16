import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class HandsPanel extends JPanel {
	
	
	BufferedImage image;
	
	Card testCard = new Card("A", 'S');
	
	private int minWidthOrHeight;
	
	private Player[] players;
	
	public HandsPanel(Player[] p) {
		
		CardDetails cardDetails = new CardDetails();
		
		this.setBackground(new Color(0,102,0));
		
		this.players = p;
		
		
		
	}
	
	
	@Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
	    
	    System.out.println("In repaint method");
	    System.out.println(players);
	    if(players != null) {
	    	
	    	System.out.println("In  the if statement");
	    	
	    	g2d.setFont(new Font("Serif", Font.BOLD, 14));
	  	    
	  	    // arbitrary image to get card sizes from
	  	    image = CardDetails.getBlankCardMap().get('D');
	  	    
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
	  		    		
	  		    		//set color
	  		    		if(card.getType() == 'H' || card.getType() == 'D') {
	  		    			g2d.setColor(new Color(255,0,0));
	  		    		}
	  		    		else {
	  		    			g2d.setColor(Color.BLACK);
	  		    		}
	  		    		
	  		    		
	  		    		// draw values on top left and bottom right corners
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
	     
	    	
	    }

	  }
	
public class UpdateWorker extends SwingWorker<Void, Player[]>{


		@Override
		protected Void doInBackground() throws Exception {
			while(true) {
				publish(HandsPanel.this.players);
				Thread.sleep(100);
			}
			
		}
		
		protected void process(List<Player[]> messages) {

			HandsPanel.this.players = messages.get(messages.size() - 1);
		}

}

public Player[] getPlayers() {
	return players;
}


public void setPlayers(Player[] players) {
	this.players = players;
}



}


