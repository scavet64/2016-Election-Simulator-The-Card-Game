package cardGame_v1.AI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cardGame_v1.Controller.Game;
import cardGame_v1.Controller.Player;
import cardGame_v1.Model.Card;
import cardGame_v1.Model.Creature;

public class PlayFinderUtility {

	private static int saveCount;
	private static String TEMP_GAME_FILE_NAME = "tempGame";
	private static Game game;
	public static int valueFlag;

	public static final int MAX = 0;
	public static final int MIN = 1;

	/**
	 * Save a copy of the game before the AI experiments with the game state
	 */
	public static boolean serializeCurrentGameState(Game game){
		boolean didSave;
		try(ObjectOutputStream gameOutputStream = new ObjectOutputStream(new FileOutputStream(TEMP_GAME_FILE_NAME + saveCount + ".ser"))) {
			gameOutputStream.writeObject(game);
			saveCount++;
			didSave = true;
		} catch (Exception e) {
			e.printStackTrace();
			didSave = false;
		}
		return didSave;
	}
	
	/**
	 * Returns the chance of occurrence for the passed in move and playoutcome.
	 * 
	 * @param po The PlayOutcome you want the chance of occurring
	 * @param move The move to examine
	 * @return total chance of outcome
	 */
	public int getChanceToOccur(PlayOutcome po, Move move){
		int chanceToOccur = 0;
		int attackingCTH = 1;
		int defendingCTH = 1;
		
		int attackingPosition = Integer.parseInt(move.getFirstCardSelection()[1]);
		int attackingSide = game.getCurrentPlayer().getPlayerSide();
		Creature attackingCreature = game.getCreatureAtPosition(attackingSide, attackingPosition);
		attackingCTH = attackingCreature.getChanceToHit();
		
		if(!po.equals(PlayOutcome.H) || !po.equals(PlayOutcome.M)){
			int defendingPosition = Integer.parseInt(move.getSecondCardSelection()[1]);
			int defendingSide = game.getOpposingPlayer().getPlayerSide();
			Creature defendingCreature = game.getCreatureAtPosition(defendingSide, defendingPosition);
			defendingCTH = defendingCreature.getChanceToHit();
		}
		
		switch(po){
		case HH:
			chanceToOccur = attackingCTH * defendingCTH;
			break;
		case HM:
			chanceToOccur = attackingCTH * (100 - defendingCTH);
			break;
		case MH:
			chanceToOccur = (100 - attackingCTH) * defendingCTH;
			break;
		case MM:
			chanceToOccur = (100 - attackingCTH) * (100 - defendingCTH);
			break;
		case H:
			chanceToOccur = attackingCTH;
			break;
		case M:
			chanceToOccur = 100 - attackingCTH;
		default:
			break;			
		}
		
		return chanceToOccur;
	}

	/**
	 * Reload the saved game back into memory.
	 */
	public static boolean loadTempGame(){
		boolean didSave;
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEMP_GAME_FILE_NAME + saveCount + ".ser"))) {
			game = (Game) ois.readObject();
			didSave = true;
			saveCount--;
		} catch (Exception e) {
			e.printStackTrace();
			didSave = false;
		}
		return didSave;
	}
	
	public static Game getGame(){
		return game;
	}

	/**
	 * A utility method to parse through all possible plays at a given game state. Takes a parameter to either
	 * compare and return the play with the highest value or the lowest value.
	 * @param currentPlayer The player who is currently in turn.
	 * @param opposingPlayer The opponent player who is not in turn.
	 * @param valueFlag An integer flag for whether the highest value or lowest value play should be returned. *Use public flags in class*.
	 * @return Next play with either highest value(MAX) or lowest value(MIN). Null if no play is possible from the current game state.
	 */
	public static Play findPlay(Game game) {
		Player currentPlayer = game.getCurrentPlayer();
		Player opposingPlayer = game.getOpposingPlayer();
		Play bestPlay = null;
		//find hand plays
		int handSize = currentPlayer.getHandOfCards().size();
		for(int i = 0; i < handSize; i++) {
			Card card = currentPlayer.getHandOfCards().get(i);
			Play tempPlay;
			if(card instanceof Creature) {
				if(!currentPlayer.isFieldFull() && currentPlayer.canPlay(card)) {
					tempPlay = new Play(new Move(("hand " + i + " " + currentPlayer.getPlayerStringSide()).split(" "), 
							availableFieldPosition(), MoveCase.PlayCard));
				}
			}else {
				//ENHANCEMENTS
				if(!currentPlayer.isFieldEmpty() && currentPlayer.canPlay(card)){
					//loop through current players field
					for(int j = 0; j < Player.MAX_FIELD_SIZE; j++) { //:^)
						Creature creature = currentPlayer.getField().get(currentPlayer.getPlayerSide()).get(j);
						if(creature != null) {
							String[] cardPosition = ("field " + j + " " + 
									currentPlayer.getPlayerStringSide()).split(" ");
						
							tempPlay = new Play(new Move(("hand " + j + " " + 
									currentPlayer.getPlayerStringSide()).split(" "), cardPosition, MoveCase.PlayCard ));
						
							//THIS IS WHAT VARIES
							if(valueFlag == MAX){
								if(tempPlay.getValue(game) > bestPlay.getValue(game)) {
									bestPlay = tempPlay;
								}
							}else {
								if(tempPlay.getValue(game) < bestPlay.getValue(game)) {
									bestPlay = tempPlay;
								}
							}
						}
					}					
					//loop through opposing player field
					for(int j = 0; j < Player.MAX_FIELD_SIZE; j++) { //:^)
						Creature creature = opposingPlayer.getField().get(opposingPlayer.getPlayerSide()).get(j);
						if(creature != null) {
							String[] cardPosition = ("field " + j + " " + 
									currentPlayer.getPlayerStringSide()).split(" ");
						
							tempPlay = new Play(new Move(("hand " + j + " " + 
									currentPlayer.getPlayerStringSide()).split(" "), cardPosition, MoveCase.PlayCard ));
						
							//THIS IS WHAT VARIES
							if(valueFlag == MAX){
								if(tempPlay.getValue(game) > bestPlay.getValue(game)) {
									bestPlay = tempPlay;
								}
							}else {
								if(tempPlay.getValue(game) < bestPlay.getValue(game)) {
									bestPlay = tempPlay;
								}
							}
						}
					}
				}
			}
		}

		//find field plays
		for(int i = 0; i < Player.MAX_FIELD_SIZE; i++) {
			Creature creature = currentPlayer.getField().get(currentPlayer.getPlayerSide()).get(i);
			if(creature != null) {
				if(currentPlayer.canAttack(creature)) {
					Play tempPlay;
					if(currentPlayer.getField().get(opposingPlayer.getPlayerSide()).size() == 0) {
						tempPlay = new Play(new Move(("field " + i + " " + currentPlayer.getPlayerStringSide()).split(" "), 
								                     ("player " + 0 + " " + opposingPlayer.getPlayerStringSide()).split(" "), MoveCase.AttackPlayer));
						//THIS IS WHAT VARIES
						if(valueFlag == MAX){
							if(tempPlay.getValue(game) > bestPlay.getValue(game)) {
								bestPlay = tempPlay;
							}
						}else {
							if(tempPlay.getValue(game) < bestPlay.getValue(game)) {
								bestPlay = tempPlay;
							}
						}
					}else {
						for(int n = 0; n < Player.MAX_FIELD_SIZE; n++) {
							Creature attackedCreature = opposingPlayer.getField().get(opposingPlayer.getPlayerSide()).get(n);
							if(attackedCreature != null) {
								tempPlay = new Play(new Move(("field " + i + " " + currentPlayer.getPlayerStringSide()).split(" "), 
					                     ("field " + n + " " + opposingPlayer.getPlayerStringSide()).split(" "), MoveCase.AttackCard));
								//THIS IS WHAT VARIES
								if(valueFlag == MAX){
									if(tempPlay.getValue(game) > bestPlay.getValue(game)) {
										bestPlay = tempPlay;
									}
								}else {
									if(tempPlay.getValue(game) < bestPlay.getValue(game)) {
										bestPlay = tempPlay;
									}
								}
							}
						}
					}
				}
			}
		}
		return bestPlay;
	}

	private static String[] availableFieldPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	public static void setValueFlag(int flag) {
		valueFlag = flag;
	}

	/**
	 * Evaluates the current Gamestate using the passed in game object.
	 * Compares player and AI health to determine how the move fairs.
	 * @param gameToEvaluate
	 * @return
	 */
	public static double gameStateEvaluation(Game gameToEvaluate) {
		
		Player player = gameToEvaluate.getCurrentPlayer();
		Player ai = gameToEvaluate.getOpposingPlayer();
		
		//gets total ai health using board health as extension of own health
		int aiHealth = ai.getHealthPoints();
		for(int i = 0; i < Player.MAX_FIELD_SIZE; i++) {
			Creature creature = ai.getField().get(ai.getPlayerSide()).get(i);
			if(creature != null) {
				aiHealth += creature.getHealthValue();
			}
		}
		
		//gets total player health using board health as extension of own health
		int playerHealth = player.getHealthPoints();
		for(int i = 0; i < Player.MAX_FIELD_SIZE; i++) {
			Creature creature = player.getField().get(player.getPlayerSide()).get(i);
			if(creature != null) {
				playerHealth += creature.getHealthValue();
			}
		}
		
		return aiHealth - playerHealth;
		
	}
}
