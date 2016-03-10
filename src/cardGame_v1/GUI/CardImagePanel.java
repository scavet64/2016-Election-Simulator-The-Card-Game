package cardGame_v1.GUI;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cardGame_v1.ExceptionHandling.ImageNotFoundException;
import cardGame_v1.Model.Card;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.RareCreature;

public class CardImagePanel extends CardPanel {
	private static final long serialVersionUID = 1L;
	public static final int HAND_MODE = 0;
	public static final int DECK_EDIT = 1;

	/**
	 * Constructor for CardImagePanel
	 */
	public CardImagePanel(int displayMode) {
		if(displayMode == HAND_MODE){
			setGameFont(MainGUI.getGameFont().deriveFont(Font.PLAIN, 16));
			
			this.setDoubleDigitFix(5);
			this.setTripleDigitFix(10);
			
			this.setFATIGUE_Y(31);
			this.setFATIGUE_X(11);
			
			this.setReductionPercent(0.535);
			this.setRareGemReductionPercent(0.4);
			
				this.setHEALTH_Y(237);
				this.setHEALTH_X(145);
				
				this.setATTACK_Y(235);
				this.setATTACK_X(20);
				
				this.setCHANCE_TO_HIT_Y(24);
				this.setCHANCE_TO_HIT_X(154);
				
				this.setRARE_GEM_X(80);
				this.setRARE_GEM_Y(140);
			

			
		}else if(displayMode == DECK_EDIT){
			setGameFont(MainGUI.getGameFont().deriveFont(Font.PLAIN, 32));
			
			this.setFATIGUE_Y(58);
			this.setFATIGUE_X(20);
			this.setReductionPercent(1);
			this.setRareGemReductionPercent(0.85);
			
				this.setHEALTH_Y(444);
				this.setHEALTH_X(271);
				
				this.setATTACK_Y(442);
				this.setATTACK_X(36);
				
				this.setCHANCE_TO_HIT_Y(45);
				this.setCHANCE_TO_HIT_X(288);
				
				this.setRARE_GEM_X(145);
				this.setRARE_GEM_Y(260);
				
			this.setDoubleDigitFix(9);
			this.setTripleDigitFix(18);
		}

	}

	/**
	 * Draw the CardImagePanel's image.
	 */
	@Override
	protected void paintComponent(Graphics g1) {

		if(getCard() != null && isCardVisible()){
//			System.out.println("HANDPANEL: " + getCard());
			super.paintComponent(g1);
			
			//cast to Graphics2D to gain more methods and functionality
			Graphics2D g = (Graphics2D) g1;
			
			try {
				g1.drawImage(getCard().getReducedCardImage(getReductionPercent()), 0, 0, null);
			} catch (ImageNotFoundException e1) {
				System.err.println(e1.toString());
				drawNoImageBox(g);
			}
			
			
			if(getCard() instanceof RareCreature){
				try {
					Image rareGem = ImageIO.read(new File("images//Rare.png"));
					rareGem = Card.getReducedSizeImage(getRareGemReductionPercent(), rareGem);
					g1.drawImage(rareGem, getRARE_GEM_X(), getRARE_GEM_Y(), null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			//cast to Graphics2D to gain more methods and functionality
			
			if(isAntiAliasing()){
				incorperateAntiAliasing(g);
			}
			
			//set the stroke for draws
			g.setStroke(stroke);
			
			//draw dynamic fatigue
			drawDynamicValues(g, getCard().getPlayFatigueValue(), getFATIGUE_X(), getFATIGUE_Y());
			
			//if card is a creature, draw its dynamic health, attack, and chance to hit.
			if(getCard() instanceof Creature){
				Creature creature = (Creature) getCard();
				drawDynamicValues(g, creature.getHealthValue(), getHEALTH_X(), getHEALTH_Y());
				drawDynamicValues(g, creature.getAttackValue(), getATTACK_X(), getATTACK_Y());
				drawDynamicValues(g, creature.getChanceToHit(), getCHANCE_TO_HIT_X(), getCHANCE_TO_HIT_Y());
			}

		} else if(getCard() != null){
			super.paintComponent(g1);
			
			try {
				g1.drawImage(Card.getReducedSizeImage(getReductionPercent(), getCard().getCardBackImage()), 0, 0, null);
			} catch (ImageNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println(e.toString());
				drawNoImageBox((Graphics2D) g1);
			}
		} else {
			super.paintComponent(g1);
		}
	}

}
