package cardGame_v1.AI;

import java.util.Collection;

public class BranchingPlay {
	private Move move;
	private PlayOutcome playOutcome;
	private double gameStateValue;
	private Collection<Play> listOfNextPlays;
	private double chanceToOccur;
	
	public BranchingPlay(){
	}
	
	

	/**
	 * @return the move
	 */
	public Move getMove() {
		return move;
	}

	/**
	 * @param move the move to set
	 */
	public void setMove(Move move) {
		this.move = move;
	}

	/**
	 * @return the gameStateValue
	 */
	public double getGameStateValue() {
		return gameStateValue;
	}

	/**
	 * @param gameStateValue the gameStateValue to set
	 */
	public void setGameStateValue(double gameStateValue) {
		this.gameStateValue = gameStateValue;
	}

	/**
	 * @return the listOfNextPlays
	 */
	public Collection<Play> getListOfNextPlays() {
		return listOfNextPlays;
	}

	/**
	 * @param listOfNextPlays the listOfNextPlays to set
	 */
	public void setListOfNextPlays(Collection<Play> listOfNextPlays) {
		this.listOfNextPlays = listOfNextPlays;
	}
	
	
}
