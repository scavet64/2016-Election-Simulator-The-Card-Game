package cardGame_v1.GUI;

import cardGame_v1.Model.Card;

public abstract class CardPanel extends PositionPanel {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	//	protected Creature creature;
	private Card card;

	
	private int ATTACK_Y;
	private int ATTACK_X;
	
	private int CHANCE_TO_HIT_Y;
	private int CHANCE_TO_HIT_X;
	
	private int RARE_GEM_Y;
	private int RARE_GEM_X;
	
	private double rareGemReductionPercent;
	
	private boolean isCardVisible;
	
	public CardPanel() {
	}
	
	/**
	 * @param card card to be made visible in the method
	 * @param isVisible hide or show specified card
	 */
	public void updateDisplay(Card card, boolean isVisible) {
		if(card == null) {
			this.setCard(null);
		}else {
			this.setCard(card);
			this.setCardVisible(isVisible);
		}
	}

	public int getATTACK_Y() {
		return ATTACK_Y;
	}

	public void setATTACK_Y(int aTTACK_Y) {
		ATTACK_Y = aTTACK_Y;
	}

	public int getATTACK_X() {
		return ATTACK_X;
	}

	public void setATTACK_X(int aTTACK_X) {
		ATTACK_X = aTTACK_X;
	}

	public int getCHANCE_TO_HIT_Y() {
		return CHANCE_TO_HIT_Y;
	}

	public void setCHANCE_TO_HIT_Y(int cHANCE_TO_HIT_Y) {
		CHANCE_TO_HIT_Y = cHANCE_TO_HIT_Y;
	}

	public int getCHANCE_TO_HIT_X() {
		return CHANCE_TO_HIT_X;
	}

	public void setCHANCE_TO_HIT_X(int cHANCE_TO_HIT_X) {
		CHANCE_TO_HIT_X = cHANCE_TO_HIT_X;
	}

	public int getRARE_GEM_Y() {
		return RARE_GEM_Y;
	}

	public void setRARE_GEM_Y(int rARE_GEM_Y) {
		RARE_GEM_Y = rARE_GEM_Y;
	}

	public int getRARE_GEM_X() {
		return RARE_GEM_X;
	}

	public void setRARE_GEM_X(int rARE_GEM_X) {
		RARE_GEM_X = rARE_GEM_X;
	}

	public double getRareGemReductionPercent() {
		return rareGemReductionPercent;
	}

	public void setRareGemReductionPercent(double rareGemReductionPercent) {
		this.rareGemReductionPercent = rareGemReductionPercent;
	}

	public boolean isCardVisible() {
		return isCardVisible;
	}

	public void setCardVisible(boolean isCardVisible) {
		this.isCardVisible = isCardVisible;
	}

	/**
	 * @return the card
	 */
	public Card getCard() {
		return card;
	}

	/**
	 * @param card the card to set
	 */
	public void setCard(Card card) {
		this.card = card;
	}
}