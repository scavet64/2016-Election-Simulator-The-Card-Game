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
		System.out.println("\n\n\n\n\nAI: Current Turn at start of AI move = " + game.getCurrentPlayerTurn());
		PlayFinderUtility.serializeCurrentGameState(game, "ORIGGAME.ser");
		//System.out.println("AI: Save Complete");
		PlayFinderUtility.setGame(game);
		Play currentPlay = PlayFinderUtility.findPlay(game);
		//System.out.println("AI: Found Play");
		System.out.println("\n AI: CurrentPlay = " + currentPlay);
		game = PlayFinderUtility.loadTempGame("ORIGGAME.ser");
		System.out.println("AI: current turn after loading game = " + game.getCurrentPlayerTurn()); //TODO
		game.fixTurnPls();
		System.out.println("AI: current turn after fixing turn plz = " + game.getCurrentPlayerTurn()); //TODO
		game.fixTurnPls();
		System.out.println("AI: current turn after fixing turn plz again = " + game.getCurrentPlayerTurn()); //TODO
		while(currentPlay != null){
			System.out.println("Enter Loop:");
			Move move = currentPlay.getMove();
			
			//applyAction needs to return the outcome and be completely reworked unless there is an easier way
			PlayOutcome realOutcome = game.applyAction(move.getFirstCardSelection(), move.getSecondCardSelection()).getOutcome();
			System.out.println("AI: RealOutcome = " + realOutcome);
			currentPlay = currentPlay.getNextPlay(realOutcome);
			//currentPlay = null;
		}
		System.out.println("AI: LEAVING play Turn");
		//game.fixTurnPls();
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