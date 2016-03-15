package cardGame_v1.AI;

import cardGame_v1.Controller.Game;

public class BranchingPlay {
	private Move move;
	private PlayOutcome playOutcome;
	private Double weightedValue;
	private Play nextPlay;
	private int chanceToOccur;

	public BranchingPlay(PlayOutcome playOutcome, int chanceToOccur, Move move){
		this.playOutcome = playOutcome;
		this.chanceToOccur = chanceToOccur;
		this.move = move;
	}

	/**
	 * @return the gameStateValue
	 */
	public double getWeightedValue(Game game) {
		double result = 0;
		if(weightedValue == null) {
			if(nextPlay == null) {
				PlayFinderUtility.serializeCurrentGameState(game);
				//TODO apply this branch to the gamestate

				nextPlay = PlayFinderUtility.findPlay(game);
				if(PlayFinderUtility.valueFlag == PlayFinderUtility.MAX) {
					PlayFinderUtility.setValueFlag(PlayFinderUtility.MIN);
					game.endTurn();
					result = PlayFinderUtility.findPlay(game).getValue(game);
					PlayFinderUtility.setValueFlag(PlayFinderUtility.MAX);
					if(nextPlay != null && result > nextPlay.getValue(game)) {
						nextPlay = null;
					}
				}else {
					result = PlayFinderUtility.gameStateEvaluation(game);
					if(nextPlay != null && result < nextPlay.getValue(game)) {
						nextPlay = null;
					}
				}
				PlayFinderUtility.loadTempGame();
			}
			if(nextPlay != null) {
				result = nextPlay.getValue(game);
			}
			result *= chanceToOccur;
			weightedValue = result;
		}else {
			result = weightedValue;
		}
		return result;
	}

	public Play getNextPlay() {
		return nextPlay;
	}
}