package pack;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class SplashPanel extends JPanel implements Runnable{
	BufferedImage img;
	float alpha = 0;
	boolean exit = false;
	public SplashPanel() {
		
		new Thread(this).start();

		setLayout(new GridLayout(1, 1));
		try {
			img = (BufferedImage) getScaledImage(ImageIO.read(getClass().getResourceAsStream("/icon.png")), 150, 150);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void exit() {
		exit = true;
	}

	public Image getScaledImage(Image srcImg, int w, int h){
	    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g2 = resizedImg.createGraphics();

	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	    g2.drawImage(srcImg, 0, 0, w, h, null);
	    g2.setColor(Color.RED);
	    g2.drawString("Carregando", 0, 200);
	    g2.dispose();

	    return resizedImg;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0));
		g2.setColor(new Color(0, 0, 0, 0));
		g2.fillRect(0, 0, 200, 200);
		
		g2.setFont(new Font("Dialog", Font.BOLD, 14));
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        g2.drawRenderedImage(img, AffineTransform.getTranslateInstance(0, 0));
        g2.setColor(Color.black);
        g2.drawString(
        		"Carregando...",
        		30,
        		150);
	}

	@Override
	public void run() {
		while(true) {
			if(!exit) {
				alpha += 0.01f;
				if(alpha > 1) alpha = 1;
			}
			else {
				alpha -= 0.01f;
				if(alpha < 0) {
					alpha = 0;
					getParent().setVisible(false);
				}
			}
			
			repaint();
			try {
				Thread.sleep((long) (1000/60));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
