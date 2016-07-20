package cardGame_v1.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class UserProfile implements Serializable { 
	private static final long serialVersionUID = 1L;
	private String name;
	private int wins;
	private int losses;
	private Integer credits;	//credits the user can use to spend on new card packs
	private Deck currentDeck;
	private boolean isFirstLoad;
	private final int MAX_NUMBER_OF_CARDS_IN_COLLECTION = 2;
	private ArrayList <Pack> packs;
	private ArrayList <Card> collectedCards;
	private String playerImagePath;
	
//	maybe one day
//	private ArrayList <Deck> decks = new ArrayList<Deck>();
//	private final int MAX_NUMBER_OF_DECKS = 9;

	
	/**
	 * Constructor for UserProfile
	 * @Param name the name for the profile to be recognized by
	 */
	public UserProfile(String name) {
		this.name = name;
		currentDeck = new Deck();
		collectedCards = new ArrayList<Card>();
		packs = new ArrayList<Pack>();
		credits = 100;
		playerImagePath = "images//defaultPlayer.png";
		setFirstLoad(true);
	}
	
	public UserProfile(){
		
	}

	/**
	 * @return stringOfCardsOwned collection of cards player has opened
	 */
	public ArrayList<String> getStringsOfCardsOwned(){
		ArrayList<String> stringsOfCardsOwned = new ArrayList<String>();
		for(Card card: collectedCards){
			stringsOfCardsOwned.add(card.getName());
		}
		return stringsOfCardsOwned;
	}
	
	/**
	 * @Return array of cards owned
	 */
	public String[] getCardOwnedNameArray(){
		return (String[]) getStringsOfCardsOwned().toArray();
	}
	
	/**
	 * @param numberOfPacks how many packs player is trying to purchase
	 * @param totalCost cost of the selected amount of packs
	 * @return true if player can afford, false if player failed to afford
	 */
	public boolean purchasePacks(int numberOfPacks, int totalCost){
		if(totalCost <= credits){
			for(int i = 0; i < numberOfPacks; i++){
				packs.add(new Pack());
			}
			subtractCredits(totalCost);
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @Param cardToAdd card to check with player's current collection to see 
	 * if they can hold another in their collection
	 * @Return true if success, false if fail
	 */
	public boolean addCard(Card cardToAdd){
		int counter = 0;
		for(Card card: collectedCards){
			if(card.equals(cardToAdd)){
				counter++;
			}
		}
		if(counter < MAX_NUMBER_OF_CARDS_IN_COLLECTION){
			collectedCards.add(cardToAdd);
			return true;
		} else{
			return false;
		}
	}
	
	/**
	 * @Return name
	 */
	public String toString(){
		return name;
	}
	
	/**
	 * @Param creditsToAdd Credits to be added to the user's stats.
	 */
	public void addCredits(int creditsToAdd){
		this.credits += creditsToAdd;
	}
	
	/**
	 * @Param creditsToSubtract Credits to be removed from the user's stats.
	 */
	private void subtractCredits(int creditsToSubtract){
		this.credits -= creditsToSubtract;
	}

	///////Getters & Setters///////
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the wins
	 */
	public int getWins() {
		return wins;
	}

	/**
	 * @param wins the wins to set
	 */
	public void setWins(int wins) {
		this.wins = wins;
	}

	/**
	 * @return the losses
	 */
	public int getLosses() {
		return losses;
	}

	/**
	 * @param losses the losses to set
	 */
	public void setLosses(int losses) {
		this.losses = losses;
	}

	/**
	 * @return the deck
	 */
	public Deck getDeck() {
		return currentDeck;
	}

	/**
	 * @param deck the deck to set
	 */
	public void setDeck(Deck deck) {
		this.currentDeck = deck;
	}

	/**
	 * @return the collectedCards
	 */
	public ArrayList<Card> getCollectedCards() {
		return collectedCards;
	}

	/**
	 * @param collectedCards the collectedCards to set
	 */
	public void setCollectedCards(ArrayList<Card> collectedCards) {
		this.collectedCards = collectedCards;
	}

	/**
	 * @return the credits
	 */
	public Integer getCredits() {
		return credits;
	}

	/**
	 * @param credits the credits to set
	 */
	public void setCredits(Integer credits) {
		this.credits = credits;
	}

	/**
	 * @return the packs
	 */
	public ArrayList<Pack> getPacks() {
		return packs;
	}

	/**
	 * @param packs the packs to set
	 */
	public void setPacks(ArrayList<Pack> packs) {
		this.packs = packs;
	}
	
	/**
	 * Increment wins
	 */
	public void addWin() {
		wins++;
	}
	
	/**
	 * increment losses
	 */
	public void addLoss() {
		losses++;
	}

	/**
	 * @return the playerImagePath
	 */
	public String getPlayerImagePath() {
		return playerImagePath;
	}

	/**
	 * @param playerImagePath the playerImagePath to set
	 */
	public void setPlayerImagePath(String playerImagePath) {
		this.playerImagePath = playerImagePath;
	}

	/**
	 * @return the isFirstLoad
	 */
	public boolean isFirstLoad() {
		return isFirstLoad;
	}

	/**
	 * @param isFirstLoad the isFirstLoad to set
	 */
	public void setFirstLoad(boolean isFirstLoad) {
		this.isFirstLoad = isFirstLoad;
	}
}
