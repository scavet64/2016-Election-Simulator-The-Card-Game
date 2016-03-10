package cardGame_v1.AI;

import java.util.ArrayList;
import java.util.HashMap;

import cardGame_v1.Controller.Game;
import cardGame_v1.Controller.Player;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.UserProfile;

public class AI extends Player {
	Game game;

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
		
		String[][] bestTurn = findBestTurn(determinePossibleMoves());
		game.applyAction(bestTurn[0], bestTurn[1]);
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
	private ArrayList<Move> determinePossibleMoves(){
		ArrayList<Move> possibleMoves = new ArrayList<Move>();
		
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
	public Move findBestTurn(ArrayList<Move> possibleMoves){
//		//set up arrays for taking the turn
//		String[] label_position_sideSelectionOne = new String[2];
//		String[] label_position_sideSelectionTwo = new String[2];
//		String[][] bestMoveArray = {label_position_sideSelectionOne, label_position_sideSelectionTwo};
		
		double HighestValue = 0.0;
		Move bestMove = null;
		
		//currently only runs though the AI's first move
		for(Move move: possibleMoves){
			double value = determineMoveValue(move);
			if(value < HighestValue){
				HighestValue = value;
				bestMove = move;
			}
		}

		return bestMove;
		
	}

}
