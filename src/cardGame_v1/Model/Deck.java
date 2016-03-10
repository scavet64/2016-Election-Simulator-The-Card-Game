package cardGame_v1.Model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

import cardGame_v1.ExceptionHandling.DeckFullException;
//import java.util.Stack;

public class Deck implements Serializable{
	private static final long serialVersionUID = 1L;
	private static final int DECK_LIMIT = 30;
	private final int MAX_NUMBER_OF_CARD_IN_DECK = 2;
	private LinkedList<Card> cardList = new LinkedList<Card>();
	private String name;
//	private Stack<Card> deck = new Stack<Card>();
	
	/**
	 * default constructor for the Deck class
	 */
	public Deck(){
	}
	
	/**
	 * constructor that takes the deck's name
	 * @param name
	 */
	public Deck(String name){
	}
	
	
	///////Deck Modifying Methods///////
	
	/**
	 * Adds a card to the deck only if the deck has not reached its limit and there arent two of the same card in the deck.
	 * @param cardToAdd the card to add
	 * @return String containing message
	 * @throws DeckFullException
	 */
	public String addCard(Card cardToAdd) throws DeckFullException{
		if (getSize() >= DECK_LIMIT) {
			throw new DeckFullException();
			//return "The deck is full. Remove a card, then try to add a new card.";
		}else if(cardList.size() == 0){
			cardList.add(cardToAdd);
			return cardToAdd.getName() + " was added to the deck.";
		}else {
			if(getNumberOfCardInDeck(cardToAdd) < MAX_NUMBER_OF_CARD_IN_DECK){
				cardList.add(cardToAdd);
				return cardToAdd.getName() + " was added to the deck.";
			} else {
				return "You cannot have more than 2 of the same cards";
			}
		}
	}
	
	/**
	 * @param cardToCheck the card that you want to check
	 * @return the number of that card in the deck
	 */
	public int getNumberOfCardInDeck(Card cardToCheck){
		int counter = 0;
		for(Card card: cardList){
			if(card.equals(cardToCheck)){
				counter++;
			}
		}
		return counter;
	}
	
	/**
	 * Remove a specified card from the deck
	 * @param card Card to remove
	 * @return Removal message
	 */
	public String removeCard(Card card){
		if(cardList.contains(card)) {
			cardList.remove(card);
			return card.getName() + " was removed from the deck.";
		}else {
			return card.getName() + " is not in the deck.";
		}
	}
	
	/**
	 * Swap a specified card for another card in the deck
	 * @param card Card to add to deck
	 * @param cardToRemove Card to get removed (swapped)
	 * @return Swap message
	 */
	public String swapCard(Card card, Card cardToRemove) {
		if(cardList.contains(card))
			return ("You can't swap the same card.");
		if(cardList.contains(cardToRemove)) {
		cardList.set(cardList.indexOf(cardToRemove), card);
		return card.getName() + " swapped with " + cardToRemove.getName();
		}else{
			return cardToRemove.getName() + " does not exist in the deck.";
		}
	}
	
	/**
	 * @return How many cards are left in the deck
	 */
	public String getCardsLeftString(){
		return cardList.size() + "/" + DECK_LIMIT + " Cards Left in Deck";
	}

	/**
	 * @return the size of the deck
	 */
	public int getSize() {
		return cardList.size();
	}
	
	/**
	 * Returns the top card of the deck and removes from the list
	 * @return the top card
	 */
	public Card getTopCard() {
		return cardList.removeFirst();		
	}
	
	/**
	 * Randomizes the order of Cards in the deck
	 */
	public void shuffle() {
		Collections.shuffle(cardList);
	}

	/**
	 * @return The maximum number of decks available
	 */
	public static int getLimit() {
		return DECK_LIMIT;
	}
	
	/**
	 * @return All cards owned
	 */
	public ArrayList<String> getStringsOfCards(){
		ArrayList<String> stringsOfCardsOwned = new ArrayList<String>();
		for(Card card: cardList){
			stringsOfCardsOwned.add(card.getName());
		}
		return stringsOfCardsOwned;
	}
	
	/**
	 * @return Max number of Cards in a Deck
	 */
	public int getMAX_NUMBER_OF_CARD_IN_DECK(){
		return MAX_NUMBER_OF_CARD_IN_DECK;
	}

	/**
	 * @return the name of the Deck
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name The name to set to the Deck
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return true if the deck is full, false if not
	 */
	public boolean isFull() {
		if(cardList.size() == DECK_LIMIT) return true;
		else return false;
	}
	
	/**
	 * @param cardList List of cards to set to the deck
	 */
	public void setCardList(LinkedList<Card> cardList) {
		this.cardList = cardList;
	}
}
