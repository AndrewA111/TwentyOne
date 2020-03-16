import java.awt.Color;
import java.awt.Dimension;
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
	
	private int minWidthOrHeight;
	
	public TestPanel() {
		
		this.setBackground(new Color(0,102,0));
		

		try {
		      image = ImageIO.read(new File("Clubs.png"));
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
	}
	
	
	@Override
	  protected void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2d = (Graphics2D) g;
	    
	    this.minWidthOrHeight = Math.min(this.getWidth(), this.getHeight());
	    
	    // get to center
	    g2d.translate(this.getWidth()/2 - image.getWidth()/2, 
	    		this.getHeight()/2 - image.getHeight()/2);
	    
//	    g2d.translate(this.getWidth()/2, 
//	    		this.getHeight()/2);
	    
//	    g2d.drawImage(image, 0, 0, this);
////	    
//	    g2d.rotate(Math.toRadians(180), image.getWidth()/2, image.getHeight()/2);
//	    g2d.drawImage(image, 0, 0, this);
	    
	    for(int i = 0;  i < 5; i++) {
	    	g2d.translate(0, this.minWidthOrHeight/3);
		    g2d.drawImage(image, 0, 0, this);
		    g2d.translate(0, -this.minWidthOrHeight/3);
		    g2d.rotate(Math.toRadians(72), image.getWidth()/2, image.getHeight()/2);
	    }
	    
	    

	  }
	
	@Override
	  public Dimension getPreferredSize() {
	    return new Dimension(image.getWidth(), image.getHeight());
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