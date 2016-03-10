package cardGame_v1.GUI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.TextLayout;
import cardGame_v1.Controller.Player;
import cardGame_v1.Model.Card;

public class PlayerPanel extends PositionPanel {
	private static final long serialVersionUID = 1L;
	private Player player;
	private final int nameX = 10;
	private final int nameY = 25;
	
	/**
	 * Constructor for PlayerPanel 
	 */
	public PlayerPanel() {
		setGameFont(MainGUI.getGameFont().deriveFont(Font.PLAIN, 26));
		
		//sets positions for drawing values & reduction percent
		this.setHEALTH_Y(225);
		this.setHEALTH_X(132);
		this.setFATIGUE_Y(227);
		this.setFATIGUE_X(29);
		this.setReductionPercent(0.535);
		this.setDoubleDigitFix(5);
		this.setTripleDigitFix(10);
	}
	
	/**
	 * Overridden method that will paint the players image and call the drawDynamicValues method for each of the dynamic values.
	 */
	@Override
	 protected void paintComponent(Graphics g1) {

		if(player != null){
			super.paintComponent(g1);
			
			//draw the player's image
			g1.drawImage(Card.getReducedSizeImage(getReductionPercent(), player.getImage()), 0, 0, null);				

			//cast to graphics2D
			Graphics2D g = (Graphics2D) g1;
			
			
			if(isAntiAliasing()){
				incorperateAntiAliasing(g);
			}
			
			//sets the stroke and color to prepare for drawing
			g.setStroke(stroke);
			g.setColor(Color.BLACK);
			
			//draw the players name
			String stringOfValue = player.getProfile().getName();
			TextLayout valueLayout = new TextLayout(stringOfValue,getGameFont(),g.getFontRenderContext());
			Shape valueShape = valueLayout.getOutline(null);
			g.translate(nameX, nameY);
			g.draw(valueShape);
			g.setColor(Color.white);
			g.fill(valueShape);
			g.translate(-nameX, -nameY);
			
			//draw the players dynamic health and fatigue points
			drawDynamicValues(g, player.getHealthPoints(), getHEALTH_X(), getHEALTH_Y());
			drawDynamicValues(g, player.getFatigue(), getFATIGUE_X(), getFATIGUE_Y());
		} else {
			super.paintComponent(g1);
		}
			
	}
	
	/**
	 * updates the display with the player
	 * @param player the updated player
	 */
	public void updateDisplay(Player player) {
		this.player = player;
	}

}
