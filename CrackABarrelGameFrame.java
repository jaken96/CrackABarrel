import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EventListener;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

public class CrackABarrelGameFrame extends JFrame implements ActionListener {
	// Window height = (1/2)(width)(sqrt(3))
	static int WINDOW_WIDTH = 500;
	static int WINDOW_HEIGHT = (int) (WINDOW_WIDTH*0.5*Math.sqrt(3.0));
	
	private boolean isRunning;
	private int fps;
	private BackgroundPanel backgroundPanel;
	private Graphics g;
	private Insets insets;
	private int firstSelection;
	private int secondSelection;
	ArrayList<JButton> butts;
	private static GameBoard gb;
	private boolean startingEmptyLocation;
	private static Image barrel;
	private static Image crackedBarrel;
	private static Border defaultBorder = BorderFactory.createEmptyBorder(3, 5, 3, 5);
	private LayoutManager [] layouts;
	
	public static void main(String[] args){
		CrackABarrelGameFrame game = new CrackABarrelGameFrame();
		game.run();
		System.exit(0);
	}
	/**
	 * This method starts the game and runs in a loop
	 */
	public void run(){
		initialize();
		while(this.isRunning){
			
			
		}
		setVisible(false);
	}
	/**
	 * This method will set up everything needed to play the game.
	 */
	void initialize(){
		this.isRunning = true;
		firstSelection = -1;
		secondSelection = -1;
		gb = new GameBoard(5);
		startingEmptyLocation = false;
		
		// Create Frame
		setTitle("Crack-A-Barrel");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setResizable(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(500, 500));
		setVisible(true);
		
		// Get insets
		this.insets = getInsets();
		setSize(this.insets.left + WINDOW_WIDTH + this.insets.right, this.insets.top + WINDOW_HEIGHT + this.insets.bottom);
		
		// Create JPanel - This Panel should hold the background board image
		this.backgroundPanel = new BackgroundPanel(this);
		// REMOVE : this.backgroundPanel.setDimensions(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.add(backgroundPanel);
		
		// Create BoxLayout
		BoxLayout bl = new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS);
		this.backgroundPanel.setLayout(bl);
		barrel = null;
		crackedBarrel = null;
		try{
			barrel = ImageIO.read(getClass().getResource("image/46x50_PixelBarrel_NoBackground.png"));
			crackedBarrel = ImageIO.read(getClass().getResource("image/Split_46x50_PixelBarrel_NoBackground.png"));
		} catch (Exception e) {
			System.out.println("There was an error initializing the buttons");
		}
		
		// Create list of buttons
		this.butts = new ArrayList<>();
		// Create reset button
		JButton resetButton = new JButton("Reset");
		resetButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				firstSelection = -1;
				secondSelection = -1;
				gb = new GameBoard(5);
				startingEmptyLocation = false;
				updateDisplay();
			}
			
		});
		butts.add(resetButton);
		JPanel resetPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		resetPanel.setOpaque(false);
		resetPanel.add(resetButton);
		// Add reset button to board
		this.backgroundPanel.add(resetPanel);
		// Create buttons
		for(int i=1; i<16; i++) {
				JButton jabutt = new JButton(); // + (i+1)); NOTE: this helps with debugging
				jabutt.setIcon(new ImageIcon(barrel));
				jabutt.setBackground(Color.CYAN);
				jabutt.setFocusable(false);
				jabutt.setBorder(defaultBorder);
				jabutt.addActionListener(this);
				butts.add(jabutt);
			}
		
		// Add buttons to board
		int buttCount = 1;
		this.layouts = new LayoutManager[5];
		for(int i=1; i<=5; i++) {
			JPanel jp = new JPanel();
			FlowLayout layout = new FlowLayout(FlowLayout.CENTER);
			layouts[i-1] = layout;
			layout.setHgap((WINDOW_WIDTH-320)/6);
			jp.setLayout(layout);
			jp.setOpaque(false);
			for(int k=1; k<=i; k++) {
				jp.add(butts.get(buttCount));
				buttCount++;
			}
			this.backgroundPanel.add(jp);
		}
		
		WINDOW_WIDTH = this.getWidth();
		WINDOW_HEIGHT = (int) (WINDOW_WIDTH*0.5*Math.sqrt(3.0));
		pack();
	}
	/**
	 * This method attempts to update which peg has been selected.
	 * If the first peg is selected, return 1.
	 * If the second peg is selected, return 2.
	 * @param buttonNum
	 * @return
	 */
	public int buttonPress(int buttonNum) {
		if(this.firstSelection == -1) {
			this.firstSelection = buttonNum;
			return 1;
		}
		this.secondSelection = buttonNum;
		return 2;
	}
	/**
	 * This method is responsible for actually moving the pieces on the board.
	 * @return true if pieces were moved.
	 */
	public boolean movePieces() {
		if(!gb.movePiece(gb.getNode(this.firstSelection), gb.getNode(this.secondSelection))){
			gb.movePiece(gb.getNode(this.secondSelection), gb.getNode(this.firstSelection));
		}
		updateDisplay();
		this.firstSelection = -1;
		this.secondSelection = -1;
		return false;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Make sure a button has been clicked
		if(e.getSource().getClass() == new JButton().getClass()) {
			// check if this is the starting empty location selection
			if(startingEmptyLocation == false){
				gb.getNode(this.butts.indexOf(e.getSource())).setEmpty(true);
				startingEmptyLocation = true;
				updateDisplay();
				return;
			}
			// Check if second button is pressed
			int buttonNum = buttonPress(this.butts.indexOf(e.getSource()));
			System.out.println("First Selection: " + this.firstSelection);
			System.out.println("Second Selection: " + this.secondSelection);
			if(buttonNum == 2){
				movePieces();
			}
			if(buttonNum == 1){
				((JComponent) e.getSource()).setBackground(Color.YELLOW);
				for(Integer pegNum : gb.getNode(this.butts.indexOf(e.getSource())).getValidMoves()){
					// Check that exactly one button (either the selection or the valid move) is empty
					if(gb.getNode(this.butts.indexOf(e.getSource())).isEmpty() != gb.getNode(pegNum).isEmpty()){
						this.butts.get(pegNum).setBackground(Color.GREEN);
					}
					
				}
			}
		}
	}
	
	public void updateDisplay(){
		try {
			for(int i=1; i<=15; i++){

				if(gb.getNode(i).isEmpty()){
					// Update color to match empty
					butts.get(i).setBackground(Color.GRAY);
					butts.get(i).setIcon(new ImageIcon(crackedBarrel));

				} else {
					butts.get(i).setBackground(Color.CYAN);
					butts.get(i).setIcon(new ImageIcon(barrel));

				}

			}
			for(LayoutManager lm : this.layouts){
				((FlowLayout) lm).setHgap((this.getWidth()-320)/6);
			}
		} catch(Exception e){
			System.err.println("Exception during updateDisplay()");
		}
	}
}

