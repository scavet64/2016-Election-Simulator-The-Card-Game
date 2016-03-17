package cardGame_v1.AI;

import cardGame_v1.Controller.Game;

public class PlayReturn {

	private double value;
	private Game updatedGame;
	
	/**
	 * @param weightedValue
	 * @param updatedGame
	 */
	public PlayReturn(double weightedValue, Game updatedGame) {
		super();
		this.value = weightedValue;
		this.updatedGame = updatedGame;
	}
	
	/**
	 * @return the weightedValue
	 */
	public double getValue() {
		return value;
	}
	
	/**
	 * @param weightedValue the weightedValue to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
	/**
	 * @return the updatedGame
	 */
	public Game getUpdatedGame() {
		return updatedGame;
	}
	
	/**
	 * @param updatedGame the updatedGame to set
	 */
	public void setUpdatedGame(Game updatedGame) {
		this.updatedGame = updatedGame;
	}
	
}
