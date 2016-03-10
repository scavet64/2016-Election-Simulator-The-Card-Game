package cardGame_v1.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Pack implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<Card> cardsInPack;
	private final int MAX_CARDS_IN_PACK = 5;
	private Random rng;

	public Pack() {
		cardsInPack = new ArrayList<Card>(MAX_CARDS_IN_PACK);
		populatePack();
	}

	/**
	 * Adds the max number of cards to a pack. Always at least one rare card in each pack.
	 */
	private void populatePack() {
		ArrayList<Card> allCards = AllCards.getInstance().getAllCards();
		@SuppressWarnings("static-access")
		ArrayList<Card> rareCards = AllCards.getInstance().getRareCardsList();
		rng = new Random();
		int i = 0;
		while(i < (MAX_CARDS_IN_PACK - 1)){
			//first round of randomization
			Card cardToAdd = allCards.get(rng.nextInt(allCards.size()));
			if(cardToAdd instanceof RareCreature){
				if(rng.nextInt(100) >= 40){
					cardsInPack.add(cardToAdd);
					i++;
				}
			}else{
				cardsInPack.add(cardToAdd);
				i++;
			}
		}
//		for(int i = 0; i < (MAX_CARDS_IN_PACK-1); i++){
//			if(allCards.get(rng.nextInt(allCards.size())) instanceof RareCreature){
//				
//			}
//			cardsInPack.add(allCards.get(rng.nextInt(allCards.size())));
//		}
		//always have at least 1 rare card in pack
		cardsInPack.add(rareCards.get(rng.nextInt(rareCards.size())));
	}

	/**
	 * @return the cardsInPack
	 */
	public ArrayList<Card> getCardsInPack() {
		return cardsInPack;
	}

	/**
	 * @param cardsInPack the cardsInPack to set
	 */
	public void setCardsInPack(ArrayList<Card> cardsInPack) {
		this.cardsInPack = cardsInPack;
	}

	/**
	 * @return the cARDS_IN_PACK
	 */
	public int getMAX_CARDS_IN_PACK() {
		return MAX_CARDS_IN_PACK;
	}
	
}
