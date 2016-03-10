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

public class FieldPanel extends CardPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for field panel
	 */
	public FieldPanel() {
		//set the font for the panel
		setGameFont(MainGUI.getGameFont().deriveFont(Font.PLAIN, 26));
		
		//set dynamic value X and Y values
		this.setHEALTH_Y(195);
		this.setHEALTH_X(177);
		this.setATTACK_Y(191);
		this.setATTACK_X(27);
		this.setFATIGUE_Y(49);
		this.setFATIGUE_X(25);
		this.setCHANCE_TO_HIT_Y(40);
		this.setCHANCE_TO_HIT_X(182);
		this.setRARE_GEM_Y(175);
		this.setRARE_GEM_X(100);
		
		//sets the double and triple digit fix numbers for this panel
		this.setDoubleDigitFix(8);
		this.setTripleDigitFix(14);
		
		//sets the reduction percent for this panel
		this.setReductionPercent(0.60);
		this.setRareGemReductionPercent(0.40);
	}
	
	/**
	 * Overridden version of the paintComponent method located within the JComponent Class. This method will draw the card's field image
	 * inside the panel if the creature is not null. If the creature is null, there is nothing in this position, therefore it just calls
	 * the super classes paintComponent method. If the creature is a rare creature, the method will draw the gem denoting its rare status.
	 */
	@Override
	protected void paintComponent(Graphics g1) {
		Creature creature = (Creature) getCard();

		if(creature != null){
//			System.out.println("FIELDPANEL: " + creature);
			super.paintComponent(g1);
			Graphics2D g = (Graphics2D) g1;
			
			try {
				g1.drawImage(creature.getReducedFieldImageSize(getReductionPercent()), 5, 5, null);
			} catch (ImageNotFoundException e1) {
				System.err.println(e1.toString());
				drawNoImageBox(g);
			}
			
			if(creature instanceof RareCreature){
				try {
					Image rareGem = ImageIO.read(new File("images//Rare.png"));
					rareGem = Card.getReducedSizeImage(getRareGemReductionPercent(), rareGem);
					g1.drawImage(rareGem, getRARE_GEM_X(), getRARE_GEM_Y(), null);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if(isAntiAliasing()){
				incorperateAntiAliasing(g);
			}
			
			//set the stroke for draws
			g.setStroke(stroke);
				
			//draw dynamic values
			drawDynamicValues(g, creature.getHealthValue(), getHEALTH_X(), getHEALTH_Y());
			drawDynamicValues(g, creature.getAttackValue(), getATTACK_X(), getATTACK_Y());
			drawDynamicValues(g, creature.getAttackFatigueValue(), getFATIGUE_X(), getFATIGUE_Y());
			drawDynamicValues(g, creature.getChanceToHit(), getCHANCE_TO_HIT_X(), getCHANCE_TO_HIT_Y());
		} else {
			super.paintComponent(g1);
		}
	}
}
