import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ImageAssets {
	
	/**
	 * HashMap to store blank card images for each suit

	 */
	private static HashMap<Character, BufferedImage> blankCardMap;
	
	
	private static BufferedImage chip;
	
	private static BufferedImage dealerChip;
	
	private static final int chipLength = 17;
	
	public ImageAssets() {
BufferedImage spriteSheet = null;
		
		try {
			spriteSheet = ImageIO.read(new File("Assets.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int cardWidth = spriteSheet.getWidth()/2;
		int cardHeight = spriteSheet.getHeight()/3;
		
		
		blankCardMap = new HashMap<Character, BufferedImage>();
		ImageAssets.blankCardMap.put('H', spriteSheet.getSubimage(0, 0, cardWidth, cardHeight));
		ImageAssets.blankCardMap.put('D', spriteSheet.getSubimage(cardWidth, 0, cardWidth, cardHeight));
		ImageAssets.blankCardMap.put('C', spriteSheet.getSubimage(0, cardHeight, cardWidth, cardHeight));
		ImageAssets.blankCardMap.put('S', spriteSheet.getSubimage(cardWidth, cardHeight, cardWidth, cardHeight));
		ImageAssets.blankCardMap.put('E', spriteSheet.getSubimage(0, 2 * cardHeight, cardWidth, cardHeight));
		
		
		ImageAssets.chip = spriteSheet.getSubimage(cardWidth, 2 * cardHeight, chipLength, chipLength);
		ImageAssets.dealerChip = spriteSheet.getSubimage(cardWidth + chipLength, 2 * cardHeight, chipLength, chipLength);
	}

	public static HashMap<Character, BufferedImage> getBlankCardMap() {
		return blankCardMap;
	}

	public static BufferedImage getChip() {
		return chip;
	}

	public static BufferedImage getDealerChip() {
		return dealerChip;
	}
	
	

}
