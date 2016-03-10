package cardGame_v1.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public abstract class PositionPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private JButton button = new JButton();
	
	//The X and Y values for dynamic values
	private int FATIGUE_Y;
	private int FATIGUE_X;
	private int HEALTH_Y;
	private int HEALTH_X;
	
	//The double and triple digit fix to ensure the values are in proper place
	private int doubleDigitFix;
	private int tripleDigitFix;
	
	private double reductionPercent;
	private Font gameFont;
	
	//antiAliasing is default set to true assuming this is ran on a PC built within the last decade
	private boolean antiAliasing = true;
	
	protected BasicStroke stroke = new BasicStroke(5);
	
	/**
	 * Constructor for PositionPanel
	 */
	public PositionPanel() {
		setLayout(new GridLayout());
		setOpaque(false);
		button.setContentAreaFilled(false);
		//button.setBorderPainted(false);
		add(button);
	}
	
	/**
	 * This method will draw a dynamic value provided to it. This method requires the X and Y coordinates for the value to be drawn.
	 * The method takes these X and Y values and will translate the graphics pixel point to the desired location. Depending on how many
	 * digits the value has, the method will correct the pixel pointer using the doubleDigitFix and tripleDigitFix variables.
	 * After the graphics are drawn, the pixel pointer is reset back to the original location.
	 * 
	 * @param g graphics2D object from the panel to be drawn.
	 * @param valueToDraw number to be added
	 * @param x x-position on the field to have the graphic applied
	 * @param y y-position on the field to have the graphic applied
	 */
	protected void drawDynamicValues(Graphics2D g, int valueToDraw, int x, int y){
		String stringOfValue = Integer.toString(valueToDraw);
		TextLayout valueLayout = new TextLayout(stringOfValue,gameFont,g.getFontRenderContext());
		Shape valueShape = valueLayout.getOutline(null);
		g.setColor(Color.BLACK);
		int fixedXPosition;
		if(valueToDraw < 10){
			fixedXPosition = x;
		} else if(valueToDraw < 100){
			fixedXPosition = x - doubleDigitFix;
		} else {
			fixedXPosition = x - tripleDigitFix;
		}
		g.translate(fixedXPosition, y);
		g.draw(valueShape);
		g.setColor(Color.white);
		g.fill(valueShape);
		g.translate(-fixedXPosition, -y);
	}
	
	/**
	 * This method will set the antialiasing for the graphics2D object for the panel being drawn.
	 * 
	 * @param g graphics2D object from the panel to be drawn.
	 */
	protected void incorperateAntiAliasing(Graphics2D g){
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}
	
	/**
	 * This method will draw a pink colored rectangle and text stating there was an issue loading the image.
	 * It a provides a protection if the image is null to allow the game to continue playing
	 * @param g the graphics2D object from the panel
	 */
	protected void drawNoImageBox(Graphics2D g){
		g.drawRect(0, 0, 500, 500);
		g.setPaint(Color.pink);
		Rectangle r = new Rectangle(500,500);
		g.draw(r);
		g.fill(r);
		g.setPaint(Color.BLACK);
		g.drawString("Problem Loading Image :(", 25, 75);
	}
	
	/**
	 * @param state will the button be visible
	 */
	public void setActive(boolean state) {
		button.setEnabled(state);
	}
	
	/**
	 * @return button
	 */
	public JButton getButton() {
		return button;
	}
	
	/**
	 * @param icon ImageIcon to be displayed on the button
	 */
	public void setButtonIcon(ImageIcon icon) {
		button.setIcon(icon);
	}
	
	
	/**
	 * @param isActive set the adjustments in AntiAliasing to be active or off
	 */
	public void setAntiAliasing(Boolean isActive){
		this.antiAliasing = isActive;
	}

	/**
	 * @return HEALTH_Y
	 */
	public int getHEALTH_Y() {
		return HEALTH_Y;
	}

	/**
	 * @param hEALTH_Y int for the new health
	 */
	public void setHEALTH_Y(int hEALTH_Y) {
		HEALTH_Y = hEALTH_Y;
	}

	/**
	 * @return FATIGUE_Y
	 */
	public int getFATIGUE_Y() {
		return FATIGUE_Y;
	}

	/**
	 * @param fATIGUE_Y int for the new fatigue
	 */
	public void setFATIGUE_Y(int fATIGUE_Y) {
		FATIGUE_Y = fATIGUE_Y;
	}

	/**
	 * @return FATIGUE_X
	 */
	public int getFATIGUE_X() {
		return FATIGUE_X;
	}

	/**
	 * @param fATIGUE_X int for the new fatigue
	 */
	public void setFATIGUE_X(int fATIGUE_X) {
		FATIGUE_X = fATIGUE_X;
	}

	/**
	 * @return HEALTH_X
	 */
	public int getHEALTH_X() {
		return HEALTH_X;
	}

	/**
	 * @param hEALTH_X int for the new health
	 */
	public void setHEALTH_X(int hEALTH_X) {
		HEALTH_X = hEALTH_X;
	}

	/**
	 * @return reductionPercent
	 */
	public double getReductionPercent() {
		return reductionPercent;
	}

	/**
	 * @param reductionPercent the reductionPercent to set
	 */
	public void setReductionPercent(double reductionPercent) {
		this.reductionPercent = reductionPercent;
	}

	/**
	 * @return antiAliasing
	 */
	public boolean isAntiAliasing() {
		return antiAliasing;
	}

	/**
	 * @param antiAliasing boolean to toggle antialiasing
	 */
	public void setAntiAliasing(boolean antiAliasing) {
		this.antiAliasing = antiAliasing;
	}

	/**
	 * @return the doubleDigitFix
	 */
	public int getDoubleDigitFix() {
		return doubleDigitFix;
	}

	/**
	 * @param doubleDigitFix the doubleDigitFix to set
	 */
	public void setDoubleDigitFix(int doubleDigitFix) {
		this.doubleDigitFix = doubleDigitFix;
	}

	/**
	 * @return the tripleDigitFix
	 */
	public int getTripleDigitFix() {
		return tripleDigitFix;
	}

	/**
	 * @param tripleDigitFix the tripleDigitFix to set
	 */
	public void setTripleDigitFix(int tripleDigitFix) {
		this.tripleDigitFix = tripleDigitFix;
	}

	/**
	 * @return the gameFont
	 */
	public Font getGameFont() {
		return gameFont;
	}

	/**
	 * @param gameFont the gameFont to set
	 */
	public void setGameFont(Font gameFont) {
		this.gameFont = gameFont;
	}
}
