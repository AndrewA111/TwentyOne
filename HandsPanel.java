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
	
	// variable to store card images
	BufferedImage image;
	
	BufferedImage chip;
	
	private final int TEXT_SIZE = 14;
	
	private int radius;
	
//	Card testCard = new Card("A", 'S');
	
	/*
	 * Int to store min value of width 
	 * and height of panel
	 * 
	 * Used to ensure card circle fits window
	 */
	private int minWidthOrHeight;
	
	private Player[] players;
	
	public HandsPanel(Player[] p) {
		
		CardDetails cardDetails = new CardDetails();
		
		this.setBackground(new Color(0,102,0));
		
		this.players = p;
		
		try {
			this.chip = ImageIO.read(new File("Chip.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	@Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
	    
	    
	    
	    // check players array has been received
	    if(players != null) {
	    	
	    	// set font
	    	g2d.setFont(new Font("Serif", Font.BOLD, this.TEXT_SIZE));
	  	    
	  	    // arbitrary image to get card sizes from
	  	    image = CardDetails.getBlankCardMap().get('D');
	  	    
	  	    // set min from width and height
	  	    this.minWidthOrHeight = Math.min(this.getWidth(), this.getHeight());
	  	    
	  	    // set circle radius
	  	    this.radius = (this.minWidthOrHeight * 3) / 10;
	  	    
	  	    // get to center
	  	    g2d.translate(this.getWidth()/2 - image.getWidth()/2, 
	  	    		this.getHeight()/2 - image.getHeight()/2);
	  	    
	  	    // empty place markers
	  	    for(int i = 0; i < 5; i++) {
	  	    	
	  	    	// translate to first card position
  	    		g2d.translate(0, this.radius);
  	    		
  	    		// get and draw card
		    	image = CardDetails.getBlankCardMap().get('E');
		    	g2d.drawImage(image, 0,	0, this);
		    	
		    	// translate back to center
  			    g2d.translate(0, -this.radius);  
  			    
  			    // rotate to next player angle
  			    g2d.rotate(Math.toRadians(72), 
  			    		image.getWidth()/2, 
  			    		image.getHeight()/2); 
		    	
	  	    	
	  	    }
	  	    
	  	    
	  	    // card offset (within player's hand)
	      	int cardOffset = image.getWidth()/3;
	  	    
	      	// loop through each player
	  	    for(int i = 0; i < this.players.length; i++) {
	  	    	if(this.players[i] != null) {
	  	    		
	  	    		// track number of cards a player has
	  	    		int cardNo = 0;
	  	    		
	  	    		// translate to first card position
	  	    		g2d.translate(0, this.radius);
	  	    		
	  	    		/*
  	    			 * S stake
  	    			 */
	  	    		
	  	    		g2d.setColor(Color.WHITE);
	  	    		
	  	    		// if upside down, flip
  	    			if(i == 2 || i == 3) {
  	    				
  	    				// rotate
  	    				g2d.rotate(Math.toRadians(180), 
	  				    		image.getWidth()/2, 
	  				    		image.getHeight()/2);
  	    				
  	    				// stake
  	  	    			g2d.drawImage(this.chip, 0, this.image.getHeight() + (image.getWidth()) / 3 - this.TEXT_SIZE, this);
  	  	    			g2d.drawString("" + players[i].getStake(), (image.getWidth())/ 3, this.image.getHeight() + (image.getWidth()) / 3);
  	  	    			
//  	  	    		g2d.drawImage(this.chip, 0, -image.getWidth()/3 - this.TEXT_SIZE, this);
//	  	    			g2d.drawString("" + players[i].getStake(), (image.getWidth())/ 3, -image.getWidth()/3);
  	  	    			
  	  	    			// balance
  	  	    			for(int j = 0; j < 3; j++) {
  	  	    				
  	  	    				g2d.drawImage(this.chip, 
  	  	    						j * (this.chip.getWidth()/2), 
  	  	    					-(image.getWidth() * 2) / 3 - this.TEXT_SIZE, 
  	  	    						this);
  	  	    			}
  	  	    			
  	  	    			g2d.drawString("" + players[i].getBalance(), 
  	  	    					(image.getWidth()* 2)/ 3, 
  	  	    				-(image.getWidth() * 2) / 3);
  	  	    			
//  	  	    		// balance
//  	  	    			for(int j = 0; j < 3; j++) {
//  	  	    				
//  	  	    				g2d.drawImage(this.chip, 
//  	  	    						j * (this.chip.getWidth()/2), 
//  	  	    						this.image.getHeight() + (image.getWidth() * 2) / 3 - this.TEXT_SIZE, 
//  	  	    						this);
//  	  	    			}
//  	  	    			
//  	  	    			g2d.drawString("" + players[i].getBalance(), 
//  	  	    					(image.getWidth()* 2)/ 3, 
//  	  	    					this.image.getHeight() + (image.getWidth() * 2) / 3);
  	  	    		
  	  	    			// revert
	  	  	    		g2d.rotate(Math.toRadians(180), 
	  				    		image.getWidth()/2, 
	  				    		image.getHeight()/2);
  	    			}	
  	    			
  	    			else {
  	    				// stake
  	  	    			g2d.drawImage(this.chip, 0, -image.getWidth()/3 - this.TEXT_SIZE, this);
  	  	    			g2d.drawString("" + players[i].getStake(), (image.getWidth())/ 3, -image.getWidth()/3);
  	  	    			
  	  	    			// balance
  	  	    			for(int j = 0; j < 3; j++) {
  	  	    				
  	  	    				g2d.drawImage(this.chip, 
  	  	    						j * (this.chip.getWidth()/2), 
  	  	    						this.image.getHeight() + (image.getWidth() * 2) / 3 - this.TEXT_SIZE, 
  	  	    						this);
  	  	    			}
  	  	    			
  	  	    			g2d.drawString("" + players[i].getBalance(), 
  	  	    					(image.getWidth()* 2)/ 3, 
  	  	    					this.image.getHeight() + (image.getWidth() * 2) / 3);
  	    			}
  	    			
	  	    		
	  	    		/*
	  	    		 * Loop through cards
	  	    		 * 
	  	    		 * Max row length is 5 cards
	  	    		 */
	  	    		for(Card card : this.players[i].getHand().getHand()) {
	  	    			
	  	    			
	  	    			
	  	    			
	  	    			
	  	    			// get and draw card
	  		    		image = CardDetails.getBlankCardMap().get(card.getType());
	  		    		
	  		    		g2d.drawImage(image, 
	  		    				(cardNo % 5) * cardOffset, 
	  		    				(cardNo/5) * cardOffset, this);
	  		    		
	  		    		//set color
	  		    		if(card.getType() == 'H' || card.getType() == 'D') {
	  		    			g2d.setColor(new Color(255,0,0));
	  		    		}
	  		    		else {
	  		    			g2d.setColor(Color.BLACK);
	  		    		}
	  		    		
	  		    		
	  		    		// draw values on top left and bottom right corners
	  				    g2d.drawString(card.getValue(), 
	  				    		image.getWidth()/9 +((cardNo % 5) * cardOffset), 
	  				    		image.getWidth()/4 + (cardNo/5) * cardOffset);
	  				    
	  				    g2d.rotate(Math.toRadians(180), 
	  				    		image.getWidth()/2, 
	  				    		image.getHeight()/2);
	  				    
	  				    g2d.drawString(card.getValue(), 
	  				    		image.getWidth()/9 - (cardNo % 5) * cardOffset, 
	  				    		image.getWidth()/4 - (cardNo/5) * cardOffset);
	  				    
	  				    // reverse rotation
	  				    g2d.rotate(-Math.toRadians(180), 
	  				    		image.getWidth()/2, 
	  				    		image.getHeight()/2);
	  		    		
	  		    		cardNo++;
	  		    		
	  		    		/*
	  		    		 *  if more than 10 cards dealt allow overlap
	  		    		 */
	  		    		if(cardNo > 9) {
	  		    			cardNo = 0;
	  		    		}
	  	    		}
	  	    		// translate back to center
	  			    g2d.translate(0, -this.radius);    
	  			    
	  	    	}	    
  			    // rotate to next player angle
  			    g2d.rotate(Math.toRadians(72 % 360), 
  			    		image.getWidth()/2, 
  			    		image.getHeight()/2); 
	  	    }
	     
	    	
	    }

	  }


	/*
	 *  Getters and setters
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	
	public void setPlayers(Player[] players) {
		this.players = players;
	}



}


