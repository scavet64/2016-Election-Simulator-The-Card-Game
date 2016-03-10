package cardGame_v1.Controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import cardGame_v1.AI.AI;
import cardGame_v1.Model.Card;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.Deck;
import cardGame_v1.Model.UserProfile;

public class Game {
	private Player playerOne;
	private Player playerTwo;
	private HashMap<Integer,HashMap<Integer,Creature>> field = new HashMap<Integer,HashMap<Integer,Creature>>();
	private int turn;
	
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
		getCurrentPlayer().incrementFatigue();
		return message;
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
	public String applyAction(String[] label_position_sideSelectionOne, String[] label_position_sideSelectionTwo) {	
		int position = Integer.parseInt(label_position_sideSelectionTwo[1]);
		Card actionCard;
		switch(label_position_sideSelectionOne[0]) {
		case "field":
			 actionCard = getCreatureAtPosition(getCurrentPlayer().getPlayerSide(), 
					 							Integer.parseInt(label_position_sideSelectionOne[1]));
			if(label_position_sideSelectionTwo[0].equals("player")) {
				// Attack the Player
				String message = (getCurrentPlayer().attack((Creature) actionCard, getOpposingPlayer()));
				if(getOpposingPlayer().getHealthPoints() <= 0) {
					gameOver();
					return "";
				}
				return message;
			}else {
				int opponentsSide = getOpposingPlayer().getPlayerSide();
				int attackingCardPosition = Integer.parseInt(label_position_sideSelectionOne[1]);
				Creature creatureToAttack = getCreatureAtPosition(opponentsSide, position);
				// Attack the Creature
				String message = (getCurrentPlayer().attack((Creature) actionCard, opponentsSide, position, Player.WITH_FATIGUE));
				if(message.equals(Player.FATIGUED)){
					// No attack
				}else {
					if(!message.equals(Player.ATTACK_MISSED)) {
						message += ((Creature) actionCard).getType().modifierString(creatureToAttack.getType());
					}
					// Creature attacks back
					String attackBackMessage = (getOpposingPlayer().attack(creatureToAttack, getCurrentPlayer().getPlayerSide(), attackingCardPosition, Player.NO_FATIGUE));
					if(!attackBackMessage.equals(Player.ATTACK_MISSED)) {
						attackBackMessage += (creatureToAttack).getType().modifierString(((Creature) actionCard).getType());
					}
					message += attackBackMessage;
				}
				return message;
			}
		case "hand":
			actionCard = getCurrentPlayer().getHandOfCards().get(Integer.parseInt(label_position_sideSelectionOne[1]));
			if((getCurrentPlayerTurn() == 1 && label_position_sideSelectionTwo[2].equals("north")) ||
				getCurrentPlayerTurn() == 2 && label_position_sideSelectionTwo[2].equals("south")) {
				//Enhance opposing player's card
				return (getCurrentPlayer().playCard(actionCard, getOpposingPlayer().getPlayerSide(), position));
			}else {
			// Play the Card or enhance own card
			return (getCurrentPlayer().playCard(actionCard, getCurrentPlayer().getPlayerSide(), position));
			}
		}
		return "error";
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
}