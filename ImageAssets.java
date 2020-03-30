import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Class to load and provide access to images
 * 
 * Individual assets are contained as subimages in one image file (Assets.png)
 * 
 * Subimages are selected from overall assets images for use elsewhere in program
 * 
 * @author Andrew
 *
 */
public class ImageAssets {
	
	/**
	 * HashMap to store blank card images for each suit
	 */
	private static HashMap<Character, BufferedImage> blankCardMap;
	
	/**
	 * Chip image
	 */
	private static BufferedImage chip;
	
	/**
	 * Dealer Chip image
	 */
	private static BufferedImage dealerChip;
	
	/**
	 * Length of chip images
	 * 
	 * (both dealer and standard chips are 17x17 pixels)
	 */
	private static final int chipLength = 17;
	
	/**
	 * Constructor
	 */
	public ImageAssets() {
		
		// Reference for overall asset sheet
		BufferedImage spriteSheet = null;
		
		try {
			// load overall asset sheet from file
			spriteSheet = ImageIO.read(new File("Assets.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// images are stored in grid. 2 col x 3 rows
		int cardWidth = spriteSheet.getWidth()/2;
		int cardHeight = spriteSheet.getHeight()/3;
		
		/*
		 *  Create HashMap to store/access card image by relevant type 
		 *  
		 *  (H - Hearts, D - Diamonds, C - Clubs, S - Spades, E - Empty)
		 */
		blankCardMap = new HashMap<Character, BufferedImage>();
		ImageAssets.blankCardMap.put('H', spriteSheet.getSubimage(0, 0, cardWidth, cardHeight));
		ImageAssets.blankCardMap.put('D', spriteSheet.getSubimage(cardWidth, 0, cardWidth, cardHeight));
		ImageAssets.blankCardMap.put('C', spriteSheet.getSubimage(0, cardHeight, cardWidth, cardHeight));
		ImageAssets.blankCardMap.put('S', spriteSheet.getSubimage(cardWidth, cardHeight, cardWidth, cardHeight));
		ImageAssets.blankCardMap.put('E', spriteSheet.getSubimage(0, 2 * cardHeight, cardWidth, cardHeight));
		
		/*
		 * Select chips from spriteSheet and assign to relevant reference 
		 */
		ImageAssets.chip = spriteSheet.getSubimage(cardWidth, 2 * cardHeight, chipLength, chipLength);
		ImageAssets.dealerChip = spriteSheet.getSubimage(cardWidth + chipLength, 2 * cardHeight, chipLength, chipLength);
	}
	
	/**
	 * Getter for card image hashMap
	 */
	public static HashMap<Character, BufferedImage> getBlankCardMap() {
		return blankCardMap;
	}
	
	/**
	 * Getter for chip image
	 */
	public static BufferedImage getChip() {
		return chip;
	}
	
	/**
	 * Getter for dealer chip image
	 */
	public static BufferedImage getDealerChip() {
		return dealerChip;
	}
	
	

}
