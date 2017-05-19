//import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class ImagePanel extends JPanel {

	private static final long serialVersionUID = -3315079919977494695L;
	
	// bufferedImage version
	BufferedImage image;
	
	public void showImage(int width, int height, int [][][] data) {
		
		// 如果發生沒有長寬資料，直接清除圖像 (防呆)
		if (width == 0 || height == 0) {
			image = null;
			this.repaint();
			return;
		}
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int [] rgb = data[j][i];
				image.setRGB(i, j, Utils.getRGB(rgb[0], rgb[1], rgb[2]));
			}
		}
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (image != null) 
			g.drawImage(image, 0, 0, this);
	}
}
