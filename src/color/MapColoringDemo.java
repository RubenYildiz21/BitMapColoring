package color;

import java.awt.Color;
import java.awt.image.BufferedImage;
import graphics.DrawingPanel;
import graphics.Image;

/**
 * MapColoring examples.
 * 
 * @author François Schumacker
 *
 */
public class MapColoringDemo {

	public static void main(String[] args) {
		for (String filename : new String[] { "img/maps/small_map.png" }) {
			// 1. Load image to color
			BufferedImage image;
			image = Image.loadImage(filename);
			System.out.println("\n>>> " + filename);
			System.out.println("Image size : " + image.getWidth() + "x" + image.getHeight() + "y");
			DrawingPanel p = Image.displayImage(image, filename);

			// 2. Color image
			long time = System.currentTimeMillis();
			MapColoring mc = new MapColoring(image, Color.BLACK);
			mc.colorMap();
			System.out.printf("Image coloring in %.2f seconds\n", (System.currentTimeMillis() - time) / 1000.0);

			// 3. Update display with colored image
			Image.updateImage(p, image, mc.getColoring().getNumberColors() + " colors");
		}
	}

}
