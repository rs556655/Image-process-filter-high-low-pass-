import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class HLPFrame extends JFrame {

	private static final long serialVersionUID = 4390493953307669741L;
	JPanel cotrolPanel = new JPanel();
	ImagePanel leftImagePanel = new ImagePanel();
	ImagePanel rightImagePanel = new ImagePanel();
	JButton btnShow = new JButton("顯示"), 
			btnLowPass = new JButton("Low Pass"), 
			btnHighPass = new JButton("High Pass");

	final int[][][] data;
	int height, width;
	BufferedImage img = null;
	
	ActionListener buttonActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (e.getSource() == btnLowPass) rightImagePanel.showImage(width, height, processLowPass());
			else if (e.getSource() == btnHighPass) rightImagePanel.showImage(width, height, processHighPass());
			else leftImagePanel.showImage(width, height, data);
			
			refreshImagePanelBounds();
		}
	};
	
	private void refreshImagePanelBounds() {
		int space = HLPFrame.this.getWidth()- 2 * img.getWidth();
		space = space > 0 ? space / 3 : 0;
		
		leftImagePanel.setBounds(
				space,
				((HLPFrame.this.getHeight() - 100 - img.getHeight()) / 2),
				img.getWidth(), img.getHeight());
		
		rightImagePanel.setBounds(
				(space>0)?(2*space+img.getWidth()):(HLPFrame.this.getWidth()-img.getWidth()),
				((HLPFrame.this.getHeight() - 100 - img.getHeight()) / 2),
				img.getWidth(), img.getHeight());
	}
	
	protected HLPFrame(){
		setTitle("影像處理 Low/High Pass by 410275024 陳品豪");
		
		try {
			img = ImageIO.read(new File("file/Munich.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		
		height = img.getHeight();
		width = img.getWidth();
		data = new int[height][width][3];
		
		this.setSize(width + 15, height + 77);
		
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = Utils.getR(rgb);
				data[y][x][1] = Utils.getG(rgb);
				data[y][x][2] = Utils.getB(rgb);
			}
		
		// 事件監聽
		btnShow.addActionListener(buttonActionListener);
		btnLowPass.addActionListener(buttonActionListener);
		btnHighPass.addActionListener(buttonActionListener);
		this.addComponentListener(new ComponentListener() {
			public void componentShown(ComponentEvent e) {}
			public void componentHidden(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				refreshImagePanelBounds();
			}
		});
		
		// 控制面板
		cotrolPanel.add(btnShow);
		cotrolPanel.add(new JPanel());
		cotrolPanel.add(btnLowPass);
		cotrolPanel.add(new JPanel());
		cotrolPanel.add(btnHighPass);
		cotrolPanel.add(new JPanel());
		
		JPanel temp = new JPanel();
		temp.setLayout(null);
		temp.add(leftImagePanel);
		temp.add(rightImagePanel);
		
		// 主畫面
		setLayout(new BorderLayout());	 
	    add(cotrolPanel, BorderLayout.PAGE_START);
	    add(temp, BorderLayout.CENTER);
	}
	
	private int [][][] processLowPass() {
		
		int [][][] ndata = new int [data.length][data[0].length][3];

		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[0].length; x++) {
				ndata[y][x] = getLowPassColor(x, y);
			}
		}
		return ndata;
	}
	
	private int [] getLowPassColor(int x, int y) {
		int count = 0;
		int [] ncolor = new int [3];
		for (int i = x - 1; i <= x + 1; i++) {
			if (i < 0 || i >= data[0].length) continue;
			for (int j = y - 1; j <= y + 1; j++) {
				if (j < 0 || j >= data.length) continue;
				for (int k = 0; k < 3; k++)
					ncolor[k] += data[j][i][k];
				count++;
			}
		}
		for (int k = 0; k < 3; k++) 
			ncolor[k] = Utils.checkPixelBound(ncolor[k] / count);
		return ncolor;
	}
	
	private int [][][] processHighPass() {
		
		int [][][] ndata = new int [data.length][data[0].length][3];
		
		for (int y = 0; y < data.length; y++) {
			for (int x = 0; x < data[0].length; x++) {
				ndata[y][x] = getHighPassColor(x, y);
			}
		}
		return ndata;
	}
	
	private int [] getHighPassColor(int x, int y) {

		int [] ncolor = new int [3];
		for (int i = x - 1; i <= x + 1; i++) {
			if (i < 0 || i >= data[0].length) return data[y][x];
			for (int j = y - 1; j <= y + 1; j++) {
				if (j < 0 || j >= data.length) return data[y][x];

				for (int k = 0; k < 3; k++)
					if (x == i && y == j)
						ncolor[k] += ((17 * data[j][i][k]));
					else
						ncolor[k] -= (data[j][i][k]);
			}
		}
		for (int k = 0; k < 3; k++) 
			ncolor[k] = Utils.checkPixelBound(ncolor[k] / 9);
			
		return ncolor;
	}
}
