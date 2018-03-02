import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class BackgroundPanel extends JPanel {
	
	private CrackABarrelGameFrame parentFrame;
	
	public BackgroundPanel(CrackABarrelGameFrame parent){
		parentFrame = parent;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Static coordinates for board
		Dimension d = getSize();
		int[] xPoints = {0, d.width/2, d.width};
		int[] yPoints = {d.height, 0, d.height};
		// Draw board
		g.setColor(Color.DARK_GRAY);
		g.fillPolygon(xPoints, yPoints, 3);
		parentFrame.updateDisplay();
	}
}
