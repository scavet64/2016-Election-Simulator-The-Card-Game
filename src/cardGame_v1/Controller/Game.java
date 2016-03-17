package cardGame_v1.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import cardGame_v1.AI.AI;
import cardGame_v1.AI.ApplyActionOutcome;
import cardGame_v1.AI.Move;
import cardGame_v1.AI.Play;
import cardGame_v1.AI.PlayOutcome;
import cardGame_v1.Model.Card;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.Deck;
import cardGame_v1.Model.UserProfile;

public class Game implements Serializable{
	private Player playerOne;
	private Player playerTwo;
	private HashMap<Integer,HashMap<Integer,Creature>> field = new HashMap<Integer,HashMap<Integer,Creature>>();
	private int turn;
	private boolean isAI;
	
	private final String savedDeckPlayerOne = "playerOneDeck.ser";
	private final String savedDeckPlayerTwo = "playerTwoDeck.ser";
	public final static boolean AI_GAME = true;
	public final static boolean MULTIPLAYER_GAME = false;
	
	private final int CARDS_AT_START = 3;
	private final int WIN_CREDIT_AWARD = 5;
	private boolean gameOver = false;
	
	
	/**
	 * Constructor for Game
	 * @param playerOneProfile the active player to be put in the game
	 * @param playerTwoProfile the challenger player to be put in the game
	 * @param isAI true if there is an AI player
	 */
	public Game(UserProfile playerOneProfile, UserProfile playerTwoProfile, boolean isAI){
		//This is only for the presentation & demo
		//In the future, users can choose their own player image
		//In hopes to meet the deadline, this functionality can be added later
		playerOneProfile.setPlayerImagePath("PlayerImages//DonaldTrumpPlayer.png");
		playerTwoProfile.setPlayerImagePath("PlayerImages//HillaryClintonPlayer.png");
		
		this.isAI = isAI;
		
		field.put(1, new HashMap<Integer,Creature>());
		field.put(2, new HashMap<Integer,Creature>());
		try(ObjectOutputStream deckOutputOne = new ObjectOutputStream(new FileOutputStream(savedDeckPlayerOne));
			ObjectOutputStream deckOutputTwo = new ObjectOutputStream(new FileOutputStream(savedDeckPlayerTwo))) {
			deckOutputOne.writeObject(playerOneProfile.getDeck());
			deckOutputTwo.writeObject(playerTwoProfile.getDeck());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		playerOne = new Player(1, playerOneProfile, field);
		if(isAI){
			playerTwo = new AI(2, playerTwoProfile, field, this);
		} else {
			playerTwo = new Player(2, playerTwoProfile, field);
		}
		turn = 0;
		
		for(int i = 0; i < CARDS_AT_START; i++) {
			playerOne.draw();
			playerTwo.draw();
		}
		
		playerOne.incrementFatigue();
	}
	
	/**
	 * Return the Creature at the specified field location. If there is no Creature, return null.
	 * @param playerSide what player field is being looked at
	 * @param position Where on playerSide is being looked at
	 * @return Card on player's field, null if empty
	 */
	public Creature getCreatureAtPosition(int playerSide, int position) {
		try{
			return field.get(playerSide).get(position);
		}catch(Exception e) {
			return null;
		}
	}

	/**
	 * Return the current player's turn in an integer format. 
	 * @return 1 if player one's turn, 2 if player two's turn
	 */
	public int getCurrentPlayerTurn() {
		return (turn % 2) + 1;
	}
	
	/**
	 * Return the Player whose turn it currently is.
	 * @return Player one if it is Player one's turn, Player two if it is Player two's turn
	 */
	public Player getCurrentPlayer() {
		if(getCurrentPlayerTurn() == 1) {
			return playerOne;
		}else {
			return playerTwo;
		}
	}
	
	/**
	 * Return the Player whose turn it is currently not
	 * @return Player two if it is Player one's turn, Player one if it is Player two's turn
	 */
	public Player getOpposingPlayer() {
		if(getCurrentPlayerTurn() == 1) {
			return playerTwo;
		}else {
			return playerOne;
		}
	}
	
	/**
	 * Allow calling Player to draw a card, update the current turn, 
	 * and increment fatigue for the next Player.
	 * @return The Player's draw message
	 */
	public String endTurn() {
		String message = getCurrentPlayer().draw();
		turn++;
		Player currentPlayer = getCurrentPlayer();
		System.out.println("GAME: turn ended, new player = " + getCurrentPlayerTurn());
		currentPlayer.incrementFatigue();		
		return message;
	}
	
	public void fixTurnPls(){
		turn++;
	}

	/**
	 * Return the Card at the specified hand location. If there is no Card, return null.
	 * @param player The Player's hand to search
	 * @param position Where in the hand to search
	 * @return Card in player's hand at the position, null if empty
	 */
	public Card getCardInHandPostition(int player, int position) {
		try{
			if(player == 1) {
				return playerOne.getHandOfCards().get(position);
			}else {
				return playerTwo.getHandOfCards().get(position);
			}
		}catch(Exception e) {
			return null;
		}
	}

	/**
	 * Apply an action with the two given position selections. 
	 * Depending on the locations, the action could include attack, or play.
	 * @param label_position_sideSelectionOne The first position selection
	 * @param label_position_sideSelectionTwo The second position selection
	 * @return The message concatenated from all applied actions
	 */
	public ApplyActionOutcome applyAction(String[] label_position_sideSelectionOne, String[] label_position_sideSelectionTwo) {	
		int position = Integer.parseInt(label_position_sideSelectionTwo[1]);
		Card actionCard;
		switch(label_position_sideSelectionOne[0]) {
		case "field":
			 actionCard = getCreatureAtPosition(getCurrentPlayer().getPlayerSide(), 
					 							Integer.parseInt(label_position_sideSelectionOne[1]));
			if(label_position_sideSelectionTwo[0].equals("player")) {
				// Attack the Player
				ApplyActionOutcome message = (getCurrentPlayer().attack((Creature) actionCard, getOpposingPlayer(), PlayOutcome.NA));
				if(getOpposingPlayer().getHealthPoints() <= 0) {
					gameOver();
					return new ApplyActionOutcome("", PlayOutcome.H);
				}
				return message;
			}else {
				int opponentsSide = getOpposingPlayer().getPlayerSide();
				int attackingCardPosition = Integer.parseInt(label_position_sideSelectionOne[1]);
				Creature creatureToAttack = getCreatureAtPosition(opponentsSide, position);
				// Attack the Creature
				ApplyActionOutcome message = (getCurrentPlayer().attack((Creature) actionCard, opponentsSide, position, Player.WITH_FATIGUE, PlayOutcome.NA));
				if(message.getMessageString().equals(Player.FATIGUED)){
					// No attack
				}else {
					if(message.getOutcome() == PlayOutcome.H) {
						message.setMessageString(message.getMessageString() + ((Creature) actionCard).getType().modifierString(creatureToAttack.getType()));
					}
					// Creature attacks back
					ApplyActionOutcome attackBackMessage = (getOpposingPlayer().attack(creatureToAttack, getCurrentPlayer().getPlayerSide(), attackingCardPosition, Player.NO_FATIGUE, PlayOutcome.NA));
					if(attackBackMessage.getOutcome() == PlayOutcome.H) {
						attackBackMessage.setMessageString(attackBackMessage.getMessageString() + (creatureToAttack).getType().modifierString(((Creature) actionCard).getType()));
						if(message.getOutcome() == PlayOutcome.H) {
							message.setOutcome(PlayOutcome.HH);
						}else {
							message.setOutcome(PlayOutcome.MH);
						}
					}else {
						if(message.getOutcome() == PlayOutcome.H) {
							message.setOutcome(PlayOutcome.HM);
						}else {
							message.setOutcome(PlayOutcome.MM);
						}
					}
					message.setMessageString(message.getMessageString() + attackBackMessage.getMessageString());
				}
				return message;
			}
		case "hand":
			actionCard = getCurrentPlayer().getHandOfCards().get(Integer.parseInt(label_position_sideSelectionOne[1]));
			if((getCurrentPlayerTurn() == 1 && label_position_sideSelectionTwo[2].equals("north")) ||
				getCurrentPlayerTurn() == 2 && label_position_sideSelectionTwo[2].equals("south")) {
				//Enhance opposing player's card
				return new ApplyActionOutcome(getCurrentPlayer().playCard(actionCard, getOpposingPlayer().getPlayerSide(), position), PlayOutcome.NA);
			}else {
			// Play the Card or enhance own card
			return new ApplyActionOutcome(getCurrentPlayer().playCard(actionCard, getCurrentPlayer().getPlayerSide(), position), PlayOutcome.NA);
			}
		}
		return new ApplyActionOutcome("error", PlayOutcome.NA);
	}
	
	public void forceOutcome(PlayOutcome po, Move move){
		
		Player attackingPlayer = getCurrentPlayer();
		Player opposingPlayer;
		if(attackingPlayer.getPlayerSide() == 1){
			opposingPlayer = playerTwo;
		} else {
			opposingPlayer = playerOne;
		}
		
		int attackingPosition = Integer.parseInt(move.getFirstCardSelection()[1]);
		int attackingSide = attackingPlayer.getPlayerSide();
		Creature attackingCreature = getCreatureAtPosition(attackingSide, attackingPosition);
		
		if((po == PlayOutcome.HH) || (po == PlayOutcome.HM) || (po == PlayOutcome.MH) || (po == PlayOutcome.MM)){
			int defendingPosition = Integer.parseInt(move.getSecondCardSelection()[1]);
			int defendingSide = getOpposingPlayer().getPlayerSide();
			Creature defendingCreature = getCreatureAtPosition(defendingSide, defendingPosition);
			
			switch(po){
			case HH:
				attackingPlayer.attack(attackingCreature, defendingSide, defendingPosition, Player.WITH_FATIGUE, PlayOutcome.H );
				opposingPlayer.attack(defendingCreature, attackingSide, attackingPosition, Player.NO_FATIGUE, PlayOutcome.H);
				break;
			case HM:
				attackingPlayer.attack(attackingCreature, defendingSide, defendingPosition, Player.WITH_FATIGUE, PlayOutcome.H);
				opposingPlayer.attack(defendingCreature, attackingSide, attackingPosition, Player.NO_FATIGUE, PlayOutcome.M);
				break;
			case MH:
				attackingPlayer.attack(attackingCreature, defendingSide, defendingPosition, Player.WITH_FATIGUE, PlayOutcome.M);
				opposingPlayer.attack(defendingCreature, attackingSide, attackingPosition, Player.NO_FATIGUE, PlayOutcome.H);
				break;
			case MM:
				attackingPlayer.attack(attackingCreature, defendingSide, defendingPosition, Player.WITH_FATIGUE, PlayOutcome.M);
				opposingPlayer.attack(defendingCreature, attackingSide, attackingPosition, Player.NO_FATIGUE, PlayOutcome.M);
				break;
			default:
				break;
			}
		} else {
			//attacking player's face
			switch(po){
			case H:
				attackingPlayer.attack(attackingCreature, opposingPlayer, PlayOutcome.H);
				break;
			case M:
				attackingPlayer.attack(attackingCreature, opposingPlayer, PlayOutcome.M);
				break;
			default:
				break;
				
			}
		}
		
	}

	/**
	 * Update stats for the winning and losing player. 
	 * Add credits to winning Player, and reload the Players' Deck.
	 */
	private void gameOver() {
		// Update Stats
		UserProfile winningPlayer = getCurrentPlayer().getProfile();
		UserProfile losingPlayer = getOpposingPlayer().getProfile();
		winningPlayer.addWin();
		winningPlayer.addCredits(WIN_CREDIT_AWARD);
		losingPlayer.addLoss();
		// Reload Decks
		reloadDecks();
		gameOver = true;
		return;
	}

	/**
	 * Reload Player's Deck.
	 */
	public void quitGame() {
		reloadDecks();
	}
	
	/**
	 * Reload the Players' Deck to the state saved at the beginning of the game.
	 */
	private void reloadDecks() {
		try(ObjectInputStream getDeckOne = new ObjectInputStream(new FileInputStream(savedDeckPlayerOne));
				ObjectInputStream getDeckTwo = new ObjectInputStream(new FileInputStream(savedDeckPlayerTwo))) {
			playerOne.getProfile().setDeck((Deck) getDeckOne.readObject());
			playerTwo.getProfile().setDeck((Deck) getDeckTwo.readObject());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Player one
	 */
	public Player getPlayerOne() {
		return playerOne;
	}

	/**
	 * @return Player two
	 */
	public Player getPlayerTwo() {
		return playerTwo;
	}

	/**
	 * @return true if game is over, false if not
	 */
	public Boolean isGameOver() {
		return gameOver;
	}
	
	public boolean isAIGame() {
		return isAI;
	}
	
	public void playAITurn() {
		if(isAI) {
			((AI) playerTwo).playTurn(this);
		}
	}
}