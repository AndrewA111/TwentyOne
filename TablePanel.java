import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

public class TablePanel extends JPanel {
	
	/**
	 *  variable to store card images
	 */
	BufferedImage image;
	
	/**
	 * Text size
	 */
	private final int TEXT_SIZE = 14;
	
	/**
	 * Boolean for setting a debug mode 
	 * where all players cards are revealed 
	 * at all times
	 */
	private final boolean DEBUG_MODE = false;
	
	/**
	 * Card-circle radius
	 */
	private int radius;
	
	/**
	 * Original panel width (for window scaling)
	 */
	private int O_WIDTH;
	
	/**
	 * Original panel height (for window scaling)
	 */
	private int O_HEIGHT;
	
	
	/**
	 * Int to store min value of width 
	 * and height of panel
	 * 
	 * Used to ensure card circle fits window
	 */
	private int minWidthOrHeight;
	
	/**
	 * Players array
	 */
	private Player[] players;
	
	/**
	 * Player associated with client calling this panel
	 */
	private Player player;
	
	/**
	 * Constructor
	 * @param p players array to be displayed
	 */
	public TablePanel(Player[] p, Player player) {
		
		/*
		 * call imageAssets to create hashmap for card images
		 */
		ImageAssets imageAssets = new ImageAssets();
		
		/*
		 * Get background green
		 */
		this.setBackground(new Color(0,102,0));
		
		/*
		 * Initialize players array
		 */
		this.players = p;
		
		/*
		 * Set player associated with client
		 */
		this.player = player;
				
		
	}
	
	/*
	 * Method to be called when loaded to store original 
	 * panel dimensions (for window scaling)
	 */
	public void initialDims() {
		
		// store width
		this.O_WIDTH = this.getWidth();
		
		// store height
		this.O_HEIGHT = this.getHeight();

	}
	
	
	@Override
	/**
	 * Method to paint graphics to this panel
	 */
	protected void paintComponent(Graphics g) {
		
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
	    
	    /*
	     *  ====================================================
	     *  Scaling
	     *  
	     *  Scale in proportion to change in size of this panel
	     *  ===================================================
	     */
	    double hScale = ((double) this.getWidth())/this.O_WIDTH;
	    double vScale = ((double) this.getHeight())/this.O_HEIGHT;
	    g2d.scale(hScale, vScale);
	    
	    
	    // use antialiasing to reduce effect of pixelation
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    	// set font
    	g2d.setFont(new Font("Serif", Font.BOLD, this.TEXT_SIZE));

  	    // arbitrary image to get card sizes from
  	    image = ImageAssets.getBlankCardMap().get('D');
  	    
  	    // set min from width and height
  	    this.minWidthOrHeight = Math.min(this.O_WIDTH, this.O_HEIGHT);
  	    
  	    
  	    /*
  	     * ===============================================
  	     * Draw table background markings
  	     * 
  	     * (Green table, legend and empty card markers)
  	     * ===============================================
  	     */
  	    
  	    this.drawTableMarkings(g2d);


  	    /*
  	     * ==============================================
  	     * Draw players in position
  	     * (Names, cards, chips and values)
  	     * ==============================================
  	     */
  	    
  	    // check players array has been received
	    if(players != null) {
	  	    
	  	    // card offset (within player's hand)
	      	int hCardOffset = image.getWidth()/3;
	      	int vCardOffset = image.getWidth()/4;
	  	    
	      	// loop through each table position
	  	    for(int i = 0; i < this.players.length; i++) {
	  	    	
	  	    	// for positions that contain a player
	  	    	if(this.players[i] != null) {
	  	    		
	  	    		/*
	  	    		 * Draw player info including:
	  	    		 * 	- Cards
	  	    		 * 	- Balance
	  	    		 * 	- Stake
	  	    		 * 	- Dealer chip (if dealer)
	  	    		 */
	  	    		this.drawPlayerInfo(g2d, i, hCardOffset, vCardOffset);
	  			    
	  	    	}
	  	    	
  			    // rotate to next player angle
  			    g2d.rotate(Math.toRadians(72 % 360), 
  			    		image.getWidth()/2, 
  			    		image.getHeight()/2); 
	  	    }	
	    }
	  }

	public void drawTableMarkings(Graphics2D g2d) {
		
		/*
		 * ===============
		 * Legend
		 * ===============
		 */
		
		// translate to position
  	    g2d.translate((this.O_WIDTH * 8)/10, this.O_HEIGHT/10);
  	    
  	    //Legend background colour (darker green)
  	    g2d.setColor(new Color(0,77,0));
  	    
  	    // Draw legend background
  	    g2d.fillRect(-ImageAssets.getChip().getWidth(), 
  	    		-2 * ImageAssets.getChip().getWidth(), 
  	    		(this.O_WIDTH* 2) / 10, 
  	    		(this.O_HEIGHT * 2) / 10);
  	    
  	    /*
  	     *  Draw border
  	     */
  	    g2d.setColor(new Color(102, 51, 0));
  	    
  	    g2d.setStroke(new BasicStroke(5));
  	    
  	    g2d.drawRect(-ImageAssets.getChip().getWidth(), 
  	    		-2 * ImageAssets.getChip().getWidth(), 
  	    		(this.O_WIDTH * 2) / 10, 
  	    		(this.O_HEIGHT * 2) / 10);
  	    
  	    /*
  	     * Stake
  	     */
  	    g2d.drawImage(ImageAssets.getChip(), 
				0, 
				 - TEXT_SIZE, 
				this);
  	    
  	    g2d.setColor(Color.WHITE);
  	    
  	    g2d.drawString("Stake", 
					3 * ImageAssets.getChip().getWidth(), 
					0);
  	    
  	    /*
  	     * Balance
  	     */
  	    for(int j = 0; j < 3; j++) {
			
			g2d.drawImage(ImageAssets.getChip(), 
					j * (ImageAssets.getChip().getWidth()/2), 
					2 * ImageAssets.getChip().getHeight() - TEXT_SIZE, 
					this);
		}
  	    
  	    g2d.drawString("Balance", 
				3 * ImageAssets.getChip().getWidth(), 
				2 * ImageAssets.getChip().getHeight());
  	  	
  	  	
  	    /*
  	     * Dealer chip
  	     */
	  	g2d.drawImage(ImageAssets.getDealerChip(), 
  				0, 
  				4 * ImageAssets.getChip().getHeight() - TEXT_SIZE, 
  				this);
	  	
	  	g2d.drawString("Dealer", 
				3 * ImageAssets.getChip().getWidth(), 
				4 * ImageAssets.getChip().getHeight());
  	    
  	    g2d.translate((-this.O_WIDTH * 8)/10, -this.O_HEIGHT/10);
  	  
  	  
  	    /*
  	     * =======================
  	     * Draw empty card markers
  	     * =======================
  	     */

  	    // set circle radius
  	    this.radius = (this.minWidthOrHeight * 5) / 20;
  	    
  	    // get to center
  	    g2d.translate(this.O_WIDTH/2 - image.getWidth()/2, 
  	    		this.O_HEIGHT/2 - image.getHeight()/2);
  	    
  	    // empty place markers
  	    for(int i = 0; i < 5; i++) {
  	    	
  	    	// translate to first card position
    		g2d.translate(0, this.radius);
    		
    		// get and draw card
	    	image = ImageAssets.getBlankCardMap().get('E');
	    	g2d.drawImage(image, 0,	0, this);
	    	
	    	// translate back to center
		    g2d.translate(0, -this.radius);  
		    
		    // rotate to next player angle
		    g2d.rotate(Math.toRadians(72), 
		    		image.getWidth()/2, 
		    		image.getHeight()/2); 
	    	
  	    	
  	    }
	}
	
	public void drawPlayerInfo(Graphics2D g2d, int i, int hCardOffset, int vCardOffset) {
		
		// track number of cards rendered
  		int cardNo = 0;
  		
  		// translate to first card position
  		g2d.translate(0, this.radius);
  		
  		/*
		 * Stake
		 */
  		
  		g2d.setColor(Color.WHITE);
  		
  		// if text upside down (indexes 2 and 3), flip
			if(i == 2 || i == 3) {
				
				// rotate
				g2d.rotate(Math.toRadians(180), 
			    		image.getWidth()/2, 
			    		image.getHeight()/2);
				
				
  			// if dealer show dealer chip
			if(this.players[i].isDealer()) {
	    		
	    		g2d.drawImage(ImageAssets.getDealerChip(), 
	    				image.getWidth()/4, 
	    				this.image.getHeight() + (image.getWidth()) / 3 - this.TEXT_SIZE, 
	    				this);
	    		}
			// stake
			else {
				g2d.drawImage(ImageAssets.getChip(), 
							image.getWidth() / 4, 
							this.image.getHeight() + (image.getWidth()) / 3 - this.TEXT_SIZE, 
							this);
				
	    			g2d.drawString("" + players[i].getStake(), 
	  	    					(image.getWidth() / 4)+ (image.getWidth())/ 3, 
	  	    					this.image.getHeight() + (image.getWidth()) / 3);
			}
  				

	    			
  			// balance
  			for(int j = 0; j < 3; j++) {
  				
  				g2d.drawImage(ImageAssets.getChip(), 
								j * (ImageAssets.getChip().getWidth()/2), 
								-(image.getWidth() * 4) / 7 - this.TEXT_SIZE, 
								this);
  			}
  			
  			g2d.drawString("" + players[i].getBalance() + " (" + (players[i].getBalance() + players[i].getStake()) + ")", 
  						(image.getWidth()* 2)/ 3, 
  						-(image.getWidth() * 4) / 7);
  			
  			g2d.drawString("" + players[i].getName(), 
  						0, 
  						-(image.getWidth()));
  			
  		
  			// revert
	    		g2d.rotate(Math.toRadians(180), 
			    		image.getWidth()/2, 
			    		image.getHeight()/2);
			}	
			
			else {
				// if dealer show dealer chip
				if(this.players[i].isDealer()) {
	  	    		
	  	    		g2d.drawImage(ImageAssets.getDealerChip(), 
	  	    				image.getWidth()/2, 
	  	    				-image.getWidth()/3 - this.TEXT_SIZE, 
	  	    				this);
	  	    		
	    			
	    		}
				// stake
				else {
					g2d.drawImage(ImageAssets.getChip(), 
							image.getWidth() / 4, 
							-image.getWidth()/4 - this.TEXT_SIZE, 
							this);
  	    			g2d.drawString("" + players[i].getStake(), 
  	    					(image.getWidth() / 4) + (image.getWidth())/ 3, 
  	    					-image.getWidth()/4);
				}
    			
    			
    			// balance
    			for(int j = 0; j < 3; j++) {
    				
    				g2d.drawImage(ImageAssets.getChip(), 
    						j * (ImageAssets.getChip().getWidth()/2), 
    						this.image.getHeight() + (image.getWidth() * 4) / 7 - this.TEXT_SIZE, 
    						this);
    			}
    			
    			g2d.drawString("" + players[i].getBalance() + " (" + (players[i].getBalance() + players[i].getStake()) + ")", 
    						(image.getWidth()* 2)/ 3, 
    						this.image.getHeight() + (image.getWidth() * 4) / 7);
	    		g2d.drawString("" + players[i].getName(), 
	    					0, 
	    					this.image.getHeight() + image.getWidth());
			}
			
  		
  		/*
  		 * Draw Cards
  		 * 
  		 * Loop through cards. Max row length is 5 cards
  		 */
			
			
  		for(Card card : this.players[i].getHand().getHand()) {

  			// get and draw card
  			
  				if(this.player != null && 
  						(this.players[i].isCardsVisible() 
							|| (this.players[i].getID() == this.player.getID()) 
							|| this.DEBUG_MODE)) {
  					
  					image = ImageAssets.getBlankCardMap().get(card.getType());
  				}
  				else {
  					image = ImageAssets.getBlankCardMap().get('B');
  				}
	    		
	    		
	    		g2d.drawImage(image, 
	    				(cardNo % 5) * hCardOffset, 
	    				(cardNo/5) * vCardOffset, this);
	    		
	    		
	    		if(this.player != null && 
  						(this.players[i].isCardsVisible() 
							|| (this.players[i].getID() == this.player.getID()) 
							|| this.DEBUG_MODE)) {
	  					
	    			//set color red if hearts or diamonds
		    		if(card.getType() == 'H' || card.getType() == 'D') {
		    			g2d.setColor(new Color(255,0,0));
		    		}
		    		// else set color black (spade or clubs)
		    		else {
		    			g2d.setColor(Color.BLACK);
		    		}
		    		
		    		
		    		/*
		    		 *  draw values on top left and bottom right corners
		    		 */
		    		
		    		// draw top-left
				    g2d.drawString(card.getValue(), 
				    		image.getWidth()/9 +((cardNo % 5) * hCardOffset), 
				    		image.getWidth()/4 + (cardNo/5) * vCardOffset);
				    
				    // rotate 180 deg
				    g2d.rotate(Math.toRadians(180), 
				    		image.getWidth()/2, 
				    		image.getHeight()/2);
				    
				    // draw bottom-right
				    g2d.drawString(card.getValue(), 
				    		image.getWidth()/9 - (cardNo % 5) * hCardOffset, 
				    		image.getWidth()/4 - (cardNo/5) * vCardOffset);
				    
				    // reverse rotation
				    g2d.rotate(-Math.toRadians(180), 
				    		image.getWidth()/2, 
				    		image.getHeight()/2);
	  			}
	    		
	    		
	    		
			    // increment card number
	    		cardNo++;
	    		
	    		/*
	    		 *  if more than 10 cards dealt revert to 
	    		 *  first row and allow overlap
	    		 */
	    		if(cardNo > 9) {
	    			cardNo = 0;
	    		}
  		}
  		// translate back to center
		    g2d.translate(0, -this.radius);    
	}

	/*
	 *  Getter for players array
	 */
	public Player[] getPlayers() {
		return players;
	}
	
	/*
	 * Setter for players array
	 */
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	/**
	 * Getter for player
	 * @return player
	 */
	public Player getPlayer() {
		return player;
	}
	
	/**
	 * Setter for player
	 * @param player
	 */
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	
	


}


