package cardGame_v1.AI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import cardGame_v1.Controller.Game;
import cardGame_v1.Controller.Player;
import cardGame_v1.Model.Card;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.RareCreature;

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
		if(saveCount > 5){
			throw new RuntimeException("STOP NIGGA");
		}
		try(ObjectOutputStream gameOutputStream = new ObjectOutputStream(new FileOutputStream(TEMP_GAME_FILE_NAME + saveCount + ".ser"))) {
			gameOutputStream.writeObject(game);
			System.out.println("PLAYFINDERUTILITY: saved " + saveCount); //TODO
			saveCount++;
			didSave = true;
		} catch (Exception e) {
			e.printStackTrace();
			didSave = false;
		}
		return didSave;
	}
	
	public static void serializeCurrentGameState(Game game2, String filename){
		//boolean didSave;
		try(ObjectOutputStream gameOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
			gameOutputStream.writeObject(game2);
			//System.out.println("PLAYFINDERUTILITY: saved " + saveCount); //TODO
			//saveCount++;
			//didSave = true;
		} catch (Exception e) {
			e.printStackTrace();
			//didSave = false;
		}
		//return didSave;
	}
	
	/**
	 * Reload the saved game back into memory.
	 */
	public static Game loadTempGame(String filename){
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
			game = (Game) ois.readObject();
			//System.out.println("PLAYFINDERUTILITY: restored " + (saveCount-1)); //TODO
			//saveCount--;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return game;
	}
	
	/**
	 * Returns the chance of occurrence for the passed in move and playoutcome.
	 * 
	 * @param po The PlayOutcome you want the chance of occurring
	 * @param move The move to examine
	 * @return total chance of outcome
	 */
	public static int getChanceToOccur(PlayOutcome po, Move move, Game game){
		int chanceToOccur = 0;
		int attackingCTH = 1;
		int defendingCTH = 1;
		
		int attackingPosition = Integer.parseInt(move.getFirstCardSelection()[1]);
		int attackingSide = game.getCurrentPlayer().getPlayerSide();			//TODO check into side number
		//int attackingSide = game.getOpposingPlayer().getPlayerSide();
		Creature attackingCreature = game.getCreatureAtPosition(attackingSide, attackingPosition);
		attackingCTH = attackingCreature.getChanceToHit();
		
		if((po == PlayOutcome.HH) || (po == PlayOutcome.HM) || (po == PlayOutcome.MH) || (po == PlayOutcome.MM)){
			int defendingPosition = Integer.parseInt(move.getSecondCardSelection()[1]);
			int defendingSide = game.getOpposingPlayer().getPlayerSide();
			//int defendingSide = game.getCurrentPlayer().getPlayerSide();
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
	public static Game loadTempGame(){
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEMP_GAME_FILE_NAME + (saveCount-1) + ".ser"))) {
			game = (Game) ois.readObject();
			System.out.println("PLAYFINDERUTILITY: restored " + (saveCount-1)); //TODO
			saveCount--;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return game;
	}
	
	public static Game getGame(){
		return game;
	}

	/**
	 * A utility method to parse through all possible plays at a given game state. Uses a static flag in this class to either
	 * compare and return the play with the highest value, or the play with the lowest value.
	 * @param game The current gamestate to search.
	 * @return Next play with either highest value(MAX) or lowest value(MIN). Null if no play is possible from the current game state.
	 */
	public static Play findPlay(Game game) {
		System.out.println("PLAYFINDERUTILITY: Entered findPlay"); //TODO
		System.out.println("PLAYFINDERUTILITY: Current fatigue = " + game.getCurrentPlayer().getFatigue());
		System.out.println("PLAYFINDERUTILITY: opposing fatigue = " + game.getOpposingPlayer().getFatigue());
		Player currentPlayer = game.getCurrentPlayer();
		Player opposingPlayer = game.getOpposingPlayer();
		Play bestPlay = null;
		//find hand plays
		
		int handSize = currentPlayer.getHandOfCards().size();
		System.out.println("PLAYFINDERUTILIY: " + handSize);	//TODO
		for(int i = 0; i < currentPlayer.getHandOfCards().size(); i++) {
			Card card = currentPlayer.getHandOfCards().get(i);
			Play tempPlay;
			if(card instanceof Creature) {
				if(!currentPlayer.isFieldFull() && currentPlayer.canPlay(card)) {
					tempPlay = new Play(new Move(("hand " + i + " " + currentPlayer.getPlayerStringSide()).split(" "), 
							availableFieldPosition(currentPlayer), MoveCase.PlayCard), game);
					System.out.println("PLAYFINDERUTILITY: Found a hand play"); //TODO
					//*****************************************************//
					Object[] result = valueCompare(tempPlay, bestPlay, game);
					bestPlay = (Play) result[0];
					game = (Game) result[1];
				}
			}else {
				//ENHANCEMENTS
				if(!currentPlayer.isFieldEmpty() && currentPlayer.canPlay(card)){
					//loop through current players field
					for(int j = 0; j < Player.MAX_FIELD_SIZE; j++) { //:^)
						Creature creature = currentPlayer.getField().get(currentPlayer.getPlayerSide()).get(j);
						if(creature != null && creature instanceof RareCreature) {
							String[] cardPosition = ("field " + j + " " + 
									currentPlayer.getPlayerStringSide()).split(" ");
						
							tempPlay = new Play(new Move(("hand " + j + " " + 
									currentPlayer.getPlayerStringSide()).split(" "), cardPosition, MoveCase.PlayCard ), game);
							System.out.println("PLAYFINDERUTILITY: Found an enhancement play, ai field"); //TODO
							//*****************************************************//
							Object[] result = valueCompare(tempPlay, bestPlay, game);
							bestPlay = (Play) result[0];
							game = (Game) result[1];
						}
					}					
					//loop through opposing player field
					for(int j = 0; j < Player.MAX_FIELD_SIZE; j++) { //:^)
						Creature creature = opposingPlayer.getField().get(opposingPlayer.getPlayerSide()).get(j);
						if(creature != null && creature instanceof RareCreature) {
							String[] cardPosition = ("field " + j + " " + 
									currentPlayer.getPlayerStringSide()).split(" ");
						
							tempPlay = new Play(new Move(("hand " + j + " " + 
									currentPlayer.getPlayerStringSide()).split(" "), cardPosition, MoveCase.PlayCard ), game);
							System.out.println("PLAYFINDERUTILITY: Found an enhancement play, player field"); //TODO
							//*****************************************************//
							Object[] result = valueCompare(tempPlay, bestPlay, game);
							bestPlay = (Play) result[0];
							game = (Game) result[1];
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
								                     ("player " + 0 + " " + opposingPlayer.getPlayerStringSide()).split(" "), MoveCase.AttackPlayer), game);
						System.out.println("PLAYFINDERUTILITY: Found a field play, attacking player"); //TODO
						//*****************************************************//
						Object[] result = valueCompare(tempPlay, bestPlay, game);
						bestPlay = (Play) result[0];
						game = (Game) result[1];
					}else {
						for(int n = 0; n < Player.MAX_FIELD_SIZE; n++) {
							Creature attackedCreature = opposingPlayer.getField().get(opposingPlayer.getPlayerSide()).get(n);
							if(attackedCreature != null) {
								tempPlay = new Play(new Move(("field " + i + " " + currentPlayer.getPlayerStringSide()).split(" "), 
					                     ("field " + n + " " + opposingPlayer.getPlayerStringSide()).split(" "), MoveCase.AttackCard), game);
								System.out.println("PLAYFINDERUTILITY: Found a field play, attacking card"); //TODO
								//*****************************************************//
								Object[] result = valueCompare(tempPlay, bestPlay, game);
								bestPlay = (Play) result[0];
								game = (Game) result[1];
							}
						}
					}
				}
			}
		}
		System.out.println("PLAYFINDERUTILITY: Leaving findPlay with " + bestPlay); //TODO
		return bestPlay;
	}
	
	private static Object[] valueCompare(Play tempPlay, Play bestPlay, Game game) {
		System.out.println("PLAYFINDERUTILITY: Entered valueCompare"); //TODO
		if(bestPlay == null) {
			System.out.println("PLAYFINDERUTILITY: Initialized bestPlay"); //TODO
			bestPlay = tempPlay;
		}else {
			PlayReturn tempPlayReturn = tempPlay.getValue(game);
			double tempValue = tempPlayReturn.getValue();
			game = tempPlayReturn.getUpdatedGame();
			
			tempPlayReturn = bestPlay.getValue(game);
			double bestValue = tempPlayReturn.getValue();
			game = tempPlayReturn.getUpdatedGame();
			if(valueFlag == MAX){
				System.out.println("PLAYFINDERUTILITY: MAX was set, comparing"); //TODO
				if(tempValue > bestValue) {
					bestPlay = tempPlay;
				}
			}else {
				System.out.println("PLAYFINDERUTILITY: MIN was set, comparing"); //TODO
				if(tempValue < bestValue) {
					bestPlay = tempPlay;
				}
			}
		}
		Object[] result = new Object[2];
		result[0] = bestPlay;
		result[1] = game;
		return result;
	}

	/**
	 * Finds the first available (no card on position) field position for the given player. Assumes field is not full when
	 * this method is called (no error check).
	 * @param player Player who's field will be searched
	 * @return A string array containing the position information for the first available field position
	 */
	private static String[] availableFieldPosition(Player player) {
		String[] result = new String[3];
		boolean positionFound = false;
		for(int i = 0; !positionFound; i++) {
			Card position = player.getField().get(player.getPlayerSide()).get(i);
			if(position == null) {
				result = ("field " + i + " " + player.getPlayerStringSide()).split(" ");
				positionFound = true;
			}
		}
		return result;
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
	
	public static void setGame(Game game2){
		game = game2;
	}
}
