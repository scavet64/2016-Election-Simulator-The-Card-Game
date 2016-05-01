package cardGame_v1.AI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.UUID;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.rits.cloning.Cloner;

import cardGame_v1.Controller.Game;
import cardGame_v1.Controller.Player;
import cardGame_v1.Model.Card;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.Enhanceable;
import cardGame_v1.Model.Enhancement;
import cardGame_v1.Model.RareCreature;

public class PlayFinderUtility {

	private static int saveCount;		//TODO: When game exits we need to set this back to 0 some how
	private static String TEMP_GAME_FILE_NAME = "tempGame";
	private static Game game;
	public static int valueFlag;

	public static final int MAX = 0;
	public static final int MIN = 1;
	
	public static void kryoSerializeCurrentGameState(Game game){
		Kryo kryo = new Kryo();
		
		if(saveCount > 20){
			throw new RuntimeException("Exceeded 20 stacked save states");
		}
		try{
			Output output = new Output(new FileOutputStream(TEMP_GAME_FILE_NAME + saveCount + ".ser"));
		    kryo.writeObject(output, game);
		    output.close();
			//System.out.println("PLAYFINDERUTILITY: saved " + saveCount); //TODO
			saveCount++;
			output.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	//	public static void serializeCurrentGameState(Game game2, String filename){
	//		//boolean didSave;
	//		try(ObjectOutputStream gameOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
	//			gameOutputStream.writeObject(game2);
	//			//saveCount++;
	//			//didSave = true;
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			//didSave = false;
	//		}
	//		//return didSave;
	//	}
		
//		/**
//		 * Reload the saved game back into memory.
//		 */
//		public static Game loadTempGame(String filename){
//			try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
//				game = (Game) ois.readObject();
//				//saveCount--;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return game;
//		}

	public static Game kryoLoadGameState(){
			Kryo kryo = new Kryo();
			
			try {
				Input input = new Input(new FileInputStream(TEMP_GAME_FILE_NAME + (saveCount-1) + ".ser"));
				game = (Game) kryo.readObject(input, Game.class);
				//System.out.println("PLAYFINDERUTILITY: restored " + (saveCount-1)); //TODO
				saveCount--;
				input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return game;
		}

	/**
	 * returns a deep copy of the passed in game
	 * @param game
	 * @return
	 */
	public static Game getDeepCopyGame(Game game){
		Cloner cloner = new Cloner();
		Game gameCopy = cloner.deepClone(game);
		return gameCopy;
	}
	
	
	
//	public static void serializeCurrentGameState(Game game2, String filename){
//		//boolean didSave;
//		try(ObjectOutputStream gameOutputStream = new ObjectOutputStream(new FileOutputStream(filename))) {
//			gameOutputStream.writeObject(game2);
//			//saveCount++;
//			//didSave = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			//didSave = false;
//		}
//		//return didSave;
//	}
	
	/**
	 * Returns the chance of occurrence for the passed in move and playoutcome.
	 * 
	 * @param po The PlayOutcome you want the chance of occurring
	 * @param move The move to examine
	 * @return total chance of outcome
	 */
	public static double getChanceToOccur(PlayOutcome po, Move move, Game game){
		double chanceToOccur = 0.0;
		int attackingCTH = 1;
		int defendingCTH = 1;
		
		int attackingPosition = Integer.parseInt(move.getFirstCardSelection()[1]);
		int attackingSide = game.getCurrentPlayer().getPlayerSide();
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
			chanceToOccur = (attackingCTH/100D) * (defendingCTH/100D);
			break;
		case HM:
			chanceToOccur = (attackingCTH/100D) * ((100 - defendingCTH)/100D);
			break;
		case MH:
			chanceToOccur = ((100 - attackingCTH)/100D) * (defendingCTH/100D);
			break;
		case MM:
			chanceToOccur = ((100 - attackingCTH)/100D) * ((100 - defendingCTH)/100D);
			break;
		case H:
			chanceToOccur = (attackingCTH/100D);
			break;
		case M:
			chanceToOccur = ((100 - attackingCTH)/100D);
		default:
			break;			
		}
		
		return chanceToOccur;
	}

//	/**
//	 * Reload the saved game back into memory.
//	 */
//	public static Game loadTempGame(){
//		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEMP_GAME_FILE_NAME + (saveCount-1) + ".ser"))) {
//			game = (Game) ois.readObject();
//			System.out.println("PLAYFINDERUTILITY: restored " + (saveCount-1)); //TODO
//			saveCount--;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return game;
//	}
//	
//	public static Game getGame(){
//		return game;
//	}

	/**
	 * A utility method to parse through all possible plays at a given game state. Uses a static flag in this class to either
	 * compare and return the play with the highest value, or the play with the lowest value.
	 * @param game The current gamestate to search.
	 * @return Next play with either highest value(MAX) or lowest value(MIN). Null if no play is possible from the current game state.
	 */
	public static Object[] findPlay(Game game) {
		Play bestPlay = null;
		//find hand plays
		for(int i = 0; i < game.getCurrentPlayer().getHandOfCards().size(); i++) {
			Card card = game.getCurrentPlayer().getHandOfCards().get(i);
			Play tempPlay;
			if(card instanceof Creature) {
				if(!game.getCurrentPlayer().isFieldFull() && game.getCurrentPlayer().canPlay(card)) {
					tempPlay = new Play(new Move(("hand " + i + " " + game.getCurrentPlayer().getPlayerStringSide()).split(" "), 
							availableFieldPosition(game.getCurrentPlayer()), MoveCase.PlayCard), game);
					//System.out.println("PLAYFINDERUTILITY: Found a hand play"); //TODO
					//*****************************************************//
					Object[] result = valueCompare(tempPlay, bestPlay, game);
					bestPlay = (Play) result[0];
					game = (Game) result[1];
				}
			}else {
				//ENHANCEMENTS
				if(!game.getCurrentPlayer().isFieldEmpty() && game.getCurrentPlayer().canPlay(card)){
					//loop through current players field
					for(int j = 0; j < Player.MAX_FIELD_SIZE; j++) { //:^)
						Creature creature = game.getCurrentPlayer().getField().get(game.getCurrentPlayer().getPlayerSide()).get(j);
						if(creature != null && creature instanceof Enhanceable && (creature.getAttackFatigueValue() + ((Enhancement) card).getModValue()) > 0) {
							String[] secondCardPosition = ("field " + j + " " + 
									game.getCurrentPlayer().getPlayerStringSide()).split(" ");
						
							//System.out.println("PLAYFINDERUTILITY: Found an enhancement play, ai field"); //TODO
							tempPlay = new Play(new Move(("hand " + i + " " + game.getCurrentPlayer().getPlayerStringSide()).split(" "), 
									secondCardPosition, MoveCase.PlayCard ), game);
							
							//*****************************************************//
							Object[] result = valueCompare(tempPlay, bestPlay, game);
							bestPlay = (Play) result[0];
							game = (Game) result[1];
						}
					}					
					//loop through opposing player field
					for(int j = 0; j < Player.MAX_FIELD_SIZE; j++) { //:^)
						Creature creature = game.getOpposingPlayer().getField().get(game.getOpposingPlayer().getPlayerSide()).get(j);
						if(creature != null && creature instanceof Enhanceable && (creature.getAttackFatigueValue() + ((Enhancement) card).getModValue()) > 0) {
							String[] secondCardPosition = ("field " + j + " " + 
									game.getOpposingPlayer().getPlayerStringSide()).split(" ");
						
							tempPlay = new Play(new Move(("hand " + i + " " + game.getCurrentPlayer().getPlayerStringSide()).split(" "), 
									secondCardPosition, MoveCase.PlayCard ), game);
							//System.out.println("PLAYFINDERUTILITY: Found an enhancement play, player field"); //TODO
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
			Creature creature = game.getCurrentPlayer().getField().get(game.getCurrentPlayer().getPlayerSide()).get(i);
			if(creature != null) {
				if(game.getCurrentPlayer().canAttack(creature)) {
					Play tempPlay;
					if(game.getCurrentPlayer().getField().get(game.getOpposingPlayer().getPlayerSide()).size() == 0) {
						tempPlay = new Play(new Move(("field " + i + " " + game.getCurrentPlayer().getPlayerStringSide()).split(" "), 
								                     ("player " + 0 + " " + game.getOpposingPlayer().getPlayerStringSide()).split(" "), MoveCase.AttackPlayer), game);
						//System.out.println("PLAYFINDERUTILITY: Found a field play, attacking player"); //TODO
						//*****************************************************//
						Object[] result = valueCompare(tempPlay, bestPlay, game);
						bestPlay = (Play) result[0];
						game = (Game) result[1];
					}else {
						for(int n = 0; n < Player.MAX_FIELD_SIZE; n++) {
							Creature attackedCreature = game.getOpposingPlayer().getField().get(game.getOpposingPlayer().getPlayerSide()).get(n);
							if(attackedCreature != null) {
								tempPlay = new Play(new Move(("field " + i + " " + game.getCurrentPlayer().getPlayerStringSide()).split(" "), 
					                     ("field " + n + " " + game.getOpposingPlayer().getPlayerStringSide()).split(" "), MoveCase.AttackCard), game);
								//System.out.println("PLAYFINDERUTILITY: Found a field play, attacking card"); //TODO
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
		Object[] result = new Object[2];
		result[0] = bestPlay;
		result[1] = game;
		return result;
	}
	
	private static Object[] valueCompare(Play tempPlay, Play bestPlay, Game game) {
		if(bestPlay == null) {
			bestPlay = tempPlay;
			game = bestPlay.getValue(game).getUpdatedGame();
		}else {
			PlayReturn tempPlayReturn = tempPlay.getValue(game);
			double tempValue = tempPlayReturn.getValue();
			game = tempPlayReturn.getUpdatedGame();
			
			tempPlayReturn = bestPlay.getValue(game);
			double bestValue = tempPlayReturn.getValue();
			game = tempPlayReturn.getUpdatedGame();
			if(valueFlag == MAX){
				//System.out.println("PLAYFINDERUTILITY: MAX was set, comparing"); //TODO
				if(tempValue > bestValue) {
					bestPlay = tempPlay;
				}
			}else {
				//System.out.println("PLAYFINDERUTILITY: MIN was set, comparing"); //TODO
				if(tempValue < bestValue) {
					bestPlay = tempPlay;
				}
			}
			//System.out.println("PLAYFINDERUTILITY: Better play value = " + bestPlay.getValue(game).getValue()); //TODO
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
		double result = aiHealth - playerHealth;
		System.out.println("PLAYFINDERUTILITY: Gamestate value = " + result); //TODO
		return result;
		
	}
	
	public static void setGame(Game game2){
		game = game2;
	}
}
