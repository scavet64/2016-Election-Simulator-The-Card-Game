package cardGame_v1.AI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

import cardGame_v1.Controller.Game;
import cardGame_v1.Controller.Player;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.Deck;
import cardGame_v1.Model.UserProfile;

/**
 * 
 * @author :^)
 *
 */
public class AI extends Player {
	//private Game game;
	private final String TEMP_GAME_FILE_NAME = "tempGame.ser";

	//TODO May not need game passed
	public AI(Integer playerSide, UserProfile profile, HashMap<Integer, HashMap<Integer, Creature>> field, Game currentGame) {
		super(playerSide, profile, field);
		//this.game = currentGame;
	}
	
	/**
	 * Plays the AI's turn out determined by the minimax algorithm
	 * @TODO:
	 * 		Gets the best turn from minimax
	 * 		Call playAction with the values from minimax
	 * 		PlayAction will take care of the move from there
	 */
	public Game playTurn(Game game){
	    System.out.println("\n\n\n\n\nAI: Current Turn at start of AI move = " + game.getCurrentPlayerTurn()); //TODO
		PlayFinderUtility.serializeCurrentGameState(game, "ORIGGAME.ser");
		
		//Find best play sequence
		/******* This probs should have a class *****************/
		Object[] playResult = PlayFinderUtility.findPlay(game);
		Play currentPlay = (Play) playResult[0];
		game = (Game) playResult[1];
		/********************************************************/
		
		//Compare whether a pass is more favorable
		if(currentPlay != null) {
			System.out.println("AI: Switching to MIN"); //TODO
			PlayFinderUtility.setValueFlag(PlayFinderUtility.MIN);
			game.endTurn();
			/******* This probs should have a class *****************/
			playResult = PlayFinderUtility.findPlay(game);
			Play tempPlay = (Play) playResult[0];
			game = (Game) playResult[1];
			/********************************************************/
			double passValue;
			if(tempPlay == null) {
				passValue = PlayFinderUtility.gameStateEvaluation(game);
			}else {
				PlayReturn playReturn = tempPlay.getValue(game);
				passValue = playReturn.getValue();
				game = playReturn.getUpdatedGame();
			}
			System.out.println("BRANCHINGPLAY: Switching back to MAX"); //TODO
			PlayFinderUtility.setValueFlag(PlayFinderUtility.MAX);
			if(passValue > currentPlay.getValue(game).getValue()) {
				//A Pass is more favorable
				currentPlay = null;
			}
		}
		
		game = PlayFinderUtility.loadTempGame("ORIGGAME.ser");
		while(currentPlay != null){
			Move move = currentPlay.getMove();
			PlayOutcome realOutcome = game.applyAction(move.getFirstCardSelection(), move.getSecondCardSelection()).getOutcome();
			System.out.println("AI: RealOutcome = " + realOutcome); //TODO
			currentPlay = currentPlay.getNextPlay(realOutcome);
		}
		System.out.println("AI: LEAVING play Turn");
		return game;
	}
	
//	/**
//	 * Save a copy of the game before the AI experiments with the game state
//	 */
//	private boolean serializeCurrentGameState(){
//		boolean didSave;
//		try(ObjectOutputStream gameOutputStream = new ObjectOutputStream(new FileOutputStream(TEMP_GAME_FILE_NAME))) {
//			gameOutputStream.writeObject(game);
//			didSave = true;
//			} catch (Exception e) {
//				e.printStackTrace();
//				didSave = false;
//			}
//		return didSave;
//	}
//	
//	/**
//	 * Reload the saved game back into memory.
//	 */
//	private boolean loadTempGame(){
//		boolean didSave;
//		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEMP_GAME_FILE_NAME))) {
//			game = (Game) ois.readObject();
//			didSave = true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			didSave = false;
//		}
//		return didSave;
//	}
}