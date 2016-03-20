package cardGame_v1.Controller;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import javax.imageio.ImageIO;

import cardGame_v1.AI.ApplyActionOutcome;
import cardGame_v1.AI.PlayOutcome;
import cardGame_v1.Model.Card;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.Enhanceable;
import cardGame_v1.Model.Enhancement;
import cardGame_v1.Model.JackCard;
import cardGame_v1.Model.RareCreature;
import cardGame_v1.Model.Type;
import cardGame_v1.Model.UserProfile;

public class Player implements Serializable{
	private Integer playerSide;
	private UserProfile profile;
	private int fatigueForCurrentTurn; 							//actions the player has done: AKA action counter
	private int maxFatigueForCurrentTurn;						//Max number of actions the player can do this turn. increases every turn until it reaches 10
	private ArrayList <Card> handOfCards = new ArrayList<Card>();
	private HashMap <Integer,HashMap<Integer,Creature>> field;		//Integer represents field location
	private int healthPoints = 10;
	private String imgFilePath;
	
	//final variables
	private final Random RNG = new Random();
	public final static int MAX_HAND_SIZE = 5;
	public final static int MAX_FIELD_SIZE = 5;
	private final String NO_SPACE_ERROR = "There is no room to play that creature\n";
	private final String CARD_NOT_IN_HAND = "You do not have that card in your hand\n";
	private final String HAND_TOO_FULL = "Your hand is full!\n";
	private final String EMPTY_DECK = "You are out of cards!\n";
	private final String CARD_PLAYED = "Card was played successfully\n";
	static final String FATIGUED = "You are too fatigued to do that\n";
	private final String NO_CARD_AT_LOCATION_ERROR = "There is no card at the position\n";
	private final String NOT_RARE = "You can only apply a spell to a rare card\n";
	private final String CREATURES_ON_FIELD = "Opposing field is not empty\n";
	private final String FATIGUE_BOUNDS_ERROR = "You cannot modify a creature below 1 fatigue cost\n";
	static final String ATTACK_MISSED = "Attack missed!\n";
	static final String JACK_FTW = "All five limbs of Jack combine into the master Jack!\n";
	static final boolean WITH_FATIGUE = true;
	static final boolean NO_FATIGUE = false;
	
	/**
	 * Constructor for Player
	 * @param playerSide what side the player is on the field
	 * @param playerProfile what user is going to playerSide
	 * @param field the field where the game will take place
	 */
	public Player(Integer playerSide, UserProfile profile, HashMap<Integer,HashMap<Integer,Creature>> field){
		this.profile = profile;
		profile.getDeck().shuffle();
		this.field = field;
		this.playerSide = playerSide;
		this.setImgFilePath(profile.getPlayerImagePath());
	}
	
	///////Gameplay methods///////
	
	/**
	 * Draw a card from the Player's Deck into the hand
	 * @return message
	 */
	public String draw(){
		if(handOfCards.size() < MAX_HAND_SIZE && profile.getDeck().getSize() > 0){
			handOfCards.add(profile.getDeck().getTopCard());
			if(isJackHere()) {
				//System.out.println("PLAYER: entered if jackhere");
				combineJack();
				return JACK_FTW;
			}else {
				return "";
			}
		} else if (handOfCards.size() > MAX_HAND_SIZE){
			return HAND_TOO_FULL;
		} else {
			return EMPTY_DECK;
		}
	}

	/**
	 * Checks if all five pieces of Jack are present in the Player's hand.
	 * @return true if all five cards of Jack are present in Player's hand, false if not
	 */
	private boolean isJackHere(){
		//System.out.println("PLAYER: entered isJackHere");
		HashSet<Integer> jackSet = new HashSet<Integer>();
		for(Card card: handOfCards){
			if(card instanceof JackCard){
				JackCard jackCard = (JackCard) card;
				//System.out.println(jackCard.getPieceNumber());
				jackSet.add(jackCard.getPieceNumber());
			}
		}
		//System.out.println(jackSet.size());
		if(jackSet.size() == 5){
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Remove the five Jack cards from the hand and add the combined Jack Myers card to the hand.
	 */
	private void combineJack(){
		Iterator<Card> it = handOfCards.iterator();
		while(it.hasNext()){
			Card card = it.next();
			if(card instanceof JackCard){
				it.remove();
			}
		}
//		for(Card card: handOfCards){
//			if(card instanceof JackCard){
//				handOfCards.remove(card);
//			}
//		}
		handOfCards.add(new RareCreature("Jack Myers", 6, 100, 20, 20, Type.Forbidden, "CardImages//jackMyers.png", "FieldImages//jackMyersField.png"));
	}
	
	/**
	 * Play the given card at the given position. Can either put a creature from into the field, 
	 * or use an enhancement on a rare creature.
	 * @param card Card to play
	 * @param playerNumber Player's side of the field
	 * @param fieldLocation Location on the field
	 * @return message
	 */
	public String playCard(Card card, Integer playerNumber, Integer fieldLocation){
		if(!hasInHand(card)) {
			return CARD_NOT_IN_HAND;
		}else if (!canPlay(card)) {
			return FATIGUED;
		}else {
			if(card instanceof Creature) {
				if(hasSpaceOnField()){
					handOfCards.remove(card);
					field.get(playerNumber).put(fieldLocation, (Creature) card);
					fatigueForCurrentTurn += card.getPlayFatigueValue();
					return CARD_PLAYED;
				}else {
					return NO_SPACE_ERROR;
				}
			}else if(card instanceof Enhancement) {
				if(isCardAtLocation(playerNumber, fieldLocation)) {
					if(field.get(playerNumber).get(fieldLocation) instanceof Enhanceable) {
						String message = applySpell((Enhancement)card, field.get(playerNumber).get(fieldLocation));
						if(!message.equals(FATIGUE_BOUNDS_ERROR)) {
							handOfCards.remove(card);
							fatigueForCurrentTurn += card.getPlayFatigueValue();
						}
						return message;
					}else {
						return NOT_RARE;
					}
				}else {
					return NO_CARD_AT_LOCATION_ERROR;
				}
			}
			return "";
		}	
	}

//	/**
//	 * Use the given Creature to attack the given position. Apply fatigue if specified.
//	 * @param attackingCard The Creature attacking
//	 * @param playerSide The Player's side of the field to attack
//	 * @param fieldLocation The location on the field to attack
//	 * @param takesFatigue True to apply fatigue, false if not
//	 * @return message
//	 */
//	public ApplyActionOutcome attack(Creature attackingCard, Integer playerSide, Integer fieldLocation, boolean takesFatigue, boolean forceHit, boolean forceMiss) {
//		if(isCardAtLocation(playerSide, fieldLocation)) {
//			if (takesFatigue && !canAttack(attackingCard)) {
//				return new ApplyActionOutcome(FATIGUED, PlayOutcome.NA);
//			}
//			if(takesFatigue){
//				fatigueForCurrentTurn += attackingCard.getAttackFatigueValue();
//			}
//			if((!forceHit && attackMissed(attackingCard)) || forceMiss){
//				return new ApplyActionOutcome(ATTACK_MISSED, PlayOutcome.M);
//			}else {
//				Creature creatureToBeAttacked = field.get(playerSide).get(fieldLocation);
//				// Apply damage to attacked card
//				int attackValue = attackingCard.getAttackValue() + attackingCard.getType().applyModifier(creatureToBeAttacked.getType());
//				creatureToBeAttacked.setHealthValue(creatureToBeAttacked.getHealthValue() - attackValue);
//				// Check if creatures died
//				if(creatureToBeAttacked.getHealthValue() <= 0){
//					//The creature is dead therefore is removed from its location
//					field.get(playerSide).remove(fieldLocation);
//					return new ApplyActionOutcome((creatureToBeAttacked.getName() + " was killed\n"), PlayOutcome.H);
//				} else {
//					return new ApplyActionOutcome((creatureToBeAttacked.getName() + " was attacked for " + attackValue + "\n"), PlayOutcome.H);
//				}
//			}
//		}else {
//			return new ApplyActionOutcome(NO_CARD_AT_LOCATION_ERROR, PlayOutcome.NA);
//		}
//	}
	
	/**
	 * Use the given Creature to attack the given position. Apply fatigue if specified.
	 * @param attackingCard The Creature attacking
	 * @param playerSide The Player's side of the field to attack
	 * @param fieldLocation The location on the field to attack
	 * @param takesFatigue True to apply fatigue, false if not
	 * @return message
	 */
	public ApplyActionOutcome attack(Creature attackingCard, Integer playerSide, Integer fieldLocation, boolean takesFatigue, PlayOutcome po) {
		if(isCardAtLocation(playerSide, fieldLocation)) {
			if (takesFatigue && !canAttack(attackingCard)) {
				return new ApplyActionOutcome(FATIGUED, PlayOutcome.NA);
			}
			if(takesFatigue){
				fatigueForCurrentTurn += attackingCard.getAttackFatigueValue();
			}
				if(attackMissed(attackingCard) && po.equals(PlayOutcome.NA) || po.equals(PlayOutcome.M)){
					return new ApplyActionOutcome(ATTACK_MISSED, PlayOutcome.M);
				}else {
				Creature creatureToBeAttacked = field.get(playerSide).get(fieldLocation);
				// Apply damage to attacked card
				int attackValue = attackingCard.getAttackValue() + attackingCard.getType().applyModifier(creatureToBeAttacked.getType());
				creatureToBeAttacked.setHealthValue(creatureToBeAttacked.getHealthValue() - attackValue);
				// Check if creatures died
				if(creatureToBeAttacked.getHealthValue() <= 0){
					//The creature is dead therefore is removed from its location
					field.get(playerSide).remove(fieldLocation);
					return new ApplyActionOutcome((creatureToBeAttacked.getName() + " was killed\n"), PlayOutcome.H);
				} else {
					return new ApplyActionOutcome((creatureToBeAttacked.getName() + " was attacked for " + attackValue + "\n"), PlayOutcome.H);
				}
			}
		}else {
			return new ApplyActionOutcome(NO_CARD_AT_LOCATION_ERROR, PlayOutcome.NA);
		}
	}

	/**
	 * Use the given Creature to attack the given Player.
	 * @param attackingCard The Creature attacking
	 * @param playerToAttack The Player to attack
	 * @return message
	 */
	public ApplyActionOutcome attack(Creature attackingCard, Player playerToAttack, PlayOutcome po) {
		if (!canAttack(attackingCard)) {
			return new ApplyActionOutcome(FATIGUED, PlayOutcome.NA);
		}
		if(field.get(playerToAttack.getPlayerSide()).size() > 0) {
			return new ApplyActionOutcome(CREATURES_ON_FIELD, PlayOutcome.NA);
		}else {
			fatigueForCurrentTurn += attackingCard.getAttackFatigueValue();
			if(attackMissed(attackingCard) && po.equals(PlayOutcome.NA) || po.equals(PlayOutcome.M)){
				return new ApplyActionOutcome(ATTACK_MISSED, PlayOutcome.M);
			}else {
				playerToAttack.setHealthPoints(playerToAttack.getHealthPoints() - attackingCard.getAttackValue());
				return new ApplyActionOutcome((playerToAttack.getProfile().getName() + " attacked for " + attackingCard.getAttackValue() + "\n"), 
						                      PlayOutcome.H);
			}
		}
	}

	/**
	 * Determine if the Player has enough fatigue for the Creature to attack 
	 * @param attackingCard Creature to check fatigue
	 * @return true if Creature can attack, false if not
	 */
	public boolean canAttack(Creature attackingCard) {
		if((attackingCard.getAttackFatigueValue() + fatigueForCurrentTurn) <= maxFatigueForCurrentTurn) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Increment the Player's max fatigue by 1, stop if 10 is reached. Reset the
	 * fatigue for the current turn.
	 */
	protected void incrementFatigue(){
		if(maxFatigueForCurrentTurn < 10){
			maxFatigueForCurrentTurn++;
		}
		fatigueForCurrentTurn = 0;
	}

	/**
	 * Determine if the attacking Creature successfully attacked.
	 * @param attackingCard The Creature to determine successful attack
	 * @return true if attack hit, false if not
	 */
	private boolean attackMissed(Creature attackingCard){
		return attackingCard.getChanceToHit() < RNG.nextInt(100);
	}

	/**
	 * Determine if the Card is in the Player's hand
	 * @param card Card to search for
	 * @return true if Card is in hand, false if not
	 */
	private boolean hasInHand(Card card){
		if(handOfCards.contains(card)){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Determine if there is free space on the field
	 * @return true if there is space, false if not
	 */
	private boolean hasSpaceOnField(){
		if(field.get(playerSide).size() < MAX_FIELD_SIZE){
			return true;

		} else {
			return false;
		}
	}

	/**
	 * Determine if the Card can be played with the current fatigue
	 * @param card Card to determine if can be played
	 * @return true if CArd can be played, false if not
	 */
	public boolean canPlay(Card card) {
		if((card.getPlayFatigueValue() + fatigueForCurrentTurn) <= maxFatigueForCurrentTurn) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Apply the given Enhancement to the given Creature
	 * @param enhancement The Enhancement to use
	 * @param creatureToModify The Creature to get enhanced
	 * @return message
	 */
	private String applySpell(Enhancement enhancement, Creature creatureToModify){
		switch(enhancement.getStat()){
		case Enhancement.ATTACK:
			creatureToModify.setAttackValue(creatureToModify.getAttackValue() + enhancement.getModValue());
			break;
		case Enhancement.HEALTH:
			creatureToModify.setHealthValue(creatureToModify.getHealthValue() + enhancement.getModValue());
			break;
		case Enhancement.FATIGUE:
			if(creatureToModify.getAttackFatigueValue() + enhancement.getModValue() > 0)
				creatureToModify.setAttackFatigueValue(creatureToModify.getAttackFatigueValue() + enhancement.getModValue());
			else
				return FATIGUE_BOUNDS_ERROR;
			break;
		case Enhancement.CHANCE_TO_ATTACK:
			creatureToModify.setChanceToHit(creatureToModify.getChanceToHit() + enhancement.getModValue());
			break;
		}
		return creatureToModify.getName() + "'s " + enhancement.getStat() + " was modified by " + enhancement.getModValue() + "\n";
//		if(enhancement.getStat().equals("Attack")){
//			creatureToModify.setAttackValue(enhancement.getModValue());
//		} else {
//			creatureToModify.setHealthValue(enhancement.getModValue());
//		}
//		return creatureToModify.getName() + "'s " + enhancement.getStat() + " was modified by " + enhancement.getModValue();
	}
	
	/**
	 * Determine if a Card is at the given field location
	 * @param playerSide Player's side of field to search
	 * @param fieldLocation Position on field's side
	 * @return true if Card is at location, false if not
	 */
	private boolean isCardAtLocation(int playerSide, int fieldLocation){
			return field.get(playerSide).containsKey(fieldLocation);		
	}

	/**
	 * @return Player's side of the field
	 */
	public Integer getPlayerSide() {
		return playerSide;
	}
	
	/**
	 * @param healthPoints Number to set Player's health points
	 */
	public void setHealthPoints(int healthPoints){
		this.healthPoints = healthPoints;
	}

	/**
	 * @return Player's health points
	 */
	public int getHealthPoints() {
		return healthPoints;
	}

	/**
	 * @return Player's hand
	 */
	public ArrayList<Card> getHandOfCards() {
		return handOfCards;
	}
	
	/**
	 * Determine if a Creature is on the field
	 * @return true if there is a Creature on the field, false if not
	 */
	public boolean creatureOnField() {
		if(field.get(playerSide).isEmpty()) {
			return false;
		}else {
			return true;
		}
	}

	/**
	 * @return Maximum hand size
	 */
	public static int getMAX_HAND_SIZE() {
		return MAX_HAND_SIZE;
	}

	/**
	 * @return Maximum field size
	 */
	public static int getMAX_FIELD_SIZE() {
		return MAX_FIELD_SIZE;
	}

	/**
	 * @return Player's profile
	 */
	public UserProfile getProfile() {
		return profile;
	}

	/**
	 * @return Player's fatigue
	 */
	public int getFatigue() {
		return maxFatigueForCurrentTurn - fatigueForCurrentTurn;
	}

	/**
	 * @return Player's image file path
	 */
	public String getImgFilePath() {
		return imgFilePath;
	}

	/**
	 * @param imgFilePath the imgFilePath to set
	 */
	public void setImgFilePath(String imgFilePath) {
		this.imgFilePath = imgFilePath;
	}
	
	/**
	 * 
	 * @return The Image located at the imgFilePath
	 */
	public Image getImage(){
		try {
			return ImageIO.read(new File(imgFilePath));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public boolean isFieldFull() {
		return field.get(playerSide).size() == MAX_FIELD_SIZE;
	}
	
	public boolean isFieldEmpty() {
		return field.get(playerSide).size() == 0;
	}

	public String getPlayerStringSide() {
		if(playerSide == 1) return "south"; //:^)
		/******/else/*****/	return "north"; 
	}

	public HashMap<Integer, HashMap<Integer, Creature>> getField() {
		return field;
	}
		
//	//If drawing is going to be an option to the player
//	public String draw2(){
//		if(handOfCards.size() <= MAX_HAND_SIZE && deck.getSize() > 0 && fatigueForCurrentTurn > 0){
//			handOfCards.add(deck.getTopCard());
//			return "";
//		} else if (handOfCards.size() > MAX_HAND_SIZE){
//			return HAND_TOO_FULL;
//		} else {
//			return EMPTY_DECK;
//		}
//	}
}
