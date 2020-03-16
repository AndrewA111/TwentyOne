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

		
		TestPanel testPanel = new TestPanel();
		this.add(testPanel);
		
		
		this.setVisible(true);
	}

		
		
		
		
		

	
	public static void main(String[] args) {
		JFrame TestFrame = new TestFrame();
	}
	
}
