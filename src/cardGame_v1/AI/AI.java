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

public class AI extends Player {
	private Game game;
	private final String TEMP_GAME_FILE_NAME = "tempGame.ser";

	public AI(Integer playerSide, UserProfile profile, HashMap<Integer, HashMap<Integer, Creature>> field, Game currentGame) {
		super(playerSide, profile, field);
		this.game = currentGame;
	}
	
	/**
	 * Plays the AI's turn out determined by the minimax algorithm
	 * @TODO:
	 * 		Gets the best turn from minimax
	 * 		Call playAction with the values from minimax
	 * 		PlayAction will take care of the move from there
	 */
	public void playTurn(){
		
		Play bestPlay = findBestTurn(determinePossibleMoves());
		while(bestPlay != null){
			Move move = bestPlay.getCurrentMove();
			
			//applyAction needs to return the outcome and be completely reworked unless there is an easier way
			PlayOutcome realOutcome = game.applyAction(move.getFirstCardSelection(), move.getSecondCardSelection());
			bestPlay = bestPlay.getNextBestPlay(realOutcome);
		}
	}
	
	/**
	 * Save a copy of the game before the AI experiments with the game state
	 */
	private boolean serializeCurrentGameState(){
		boolean didSave;
		try(ObjectOutputStream gameOutputStream = new ObjectOutputStream(new FileOutputStream(TEMP_GAME_FILE_NAME))) {
			gameOutputStream.writeObject(game);
			didSave = true;
			} catch (Exception e) {
				e.printStackTrace();
				didSave = false;
			}
		return didSave;
	}
	
	/**
	 * Reload the saved game back into memory.
	 */
	private boolean loadTempGame(){
		boolean didSave;
		try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(TEMP_GAME_FILE_NAME))) {
			game = (Game) ois.readObject();
			didSave = true;
		} catch (Exception e) {
			e.printStackTrace();
			didSave = false;
		}
		return didSave;
	}
	
	/**
	 * Method will determine the value of a given move
	 * @return The value of the move
	 */
	private double determineMoveValue(Move moveToDetermine){
		double gameStateValue = 0.0;
		
		return gameStateValue;
	}
	
	/**
	 * Determines the number of possible moves by placing each move(Array of StringArrays) into an ArrayList
	 * @return all possible moves
	 */
	private ArrayList<Play> determinePossibleMoves(){
		ArrayList<Play> possibleMoves = new ArrayList<Play>();
		
		return possibleMoves;
		
	}
	
	/**
	 * Meat and potatoes of minimax
	 * TODO: Run though possibleMoves for current turn
	 * 		run though players possible moves
	 * 		run though possibleMoves after player makes move

	 * 			
	 * @return an array containing the string arrays for best turn
	 */
	public Play findBestTurn(ArrayList<Play> possibleMoves){
//		//set up arrays for taking the turn
//		String[] label_position_sideSelectionOne = new String[2];
//		String[] label_position_sideSelectionTwo = new String[2];
//		String[][] bestMoveArray = {label_position_sideSelectionOne, label_position_sideSelectionTwo};
		
		double HighestValue = 0.0;
		Play bestMove = null;
		
		//currently only runs though the AI's first move
		for(Play play: possibleMoves){
			double value = play.getValue();
			if(value < HighestValue){
				HighestValue = value;
				bestMove = play;
			}
		}

		return bestMove;
		
	}

}
