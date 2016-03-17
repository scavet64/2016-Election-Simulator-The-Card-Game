package cardGame_v1.AI;

import cardGame_v1.Controller.Game;

public class BranchingPlay {
	private Move move;
	private PlayOutcome playOutcome;
	private double weightedValue = Double.MIN_NORMAL;
	private Play nextPlay;
	private int chanceToOccur;

	/**
	 * 
	 * @param playOutcome
	 * @param chanceToOccur
	 * @param move
	 */
	public BranchingPlay(PlayOutcome playOutcome, int chanceToOccur, Move move){
		this.playOutcome = playOutcome;
		this.chanceToOccur = chanceToOccur;
		this.move = move;
	}

	/**
	 * @return the gameStateValue
	 */
	public PlayReturn getWeightedValue(Game game) {
		if(weightedValue == Double.MIN_NORMAL) {
			double result = 0;
			if(nextPlay == null) {
				PlayFinderUtility.serializeCurrentGameState(game);
				if(playOutcome == PlayOutcome.NA) {
					game.applyAction(move.getFirstCardSelection(), move.getSecondCardSelection());
				}else {
					game.forceOutcome(playOutcome, move);
				}
				nextPlay = PlayFinderUtility.findPlay(game);
				if(PlayFinderUtility.valueFlag == PlayFinderUtility.MAX) {
					System.out.println("BRANCHINGPLAY: Switching to MIN"); //TODO
					PlayFinderUtility.setValueFlag(PlayFinderUtility.MIN);
					game.endTurn();
					Play tempPlay = PlayFinderUtility.findPlay(game);
					if(tempPlay == null) {
						result = PlayFinderUtility.gameStateEvaluation(game);
						System.out.println("BRANCHINGPLAY: Didn't find a play for MIN, evaluated gamestate to: " + result); //TODO
					}else {
						PlayReturn playReturn = tempPlay.getValue(game);
						result = playReturn.getValue();
						game = playReturn.getUpdatedGame();
						System.out.println("BRANCHINGPLAY: If turn stopped here, result = " + result); //TODO
					}
					System.out.println("BRANCHINGPLAY: Switching back to MAX"); //TODO
					PlayFinderUtility.setValueFlag(PlayFinderUtility.MAX);
					if(nextPlay != null && result > nextPlay.getValue(game).getValue()) {
						nextPlay = null;
					}
				}else {
					result = PlayFinderUtility.gameStateEvaluation(game);
					if(nextPlay != null && result < nextPlay.getValue(game).getValue()) {
						nextPlay = null;
					}
				}
				game = PlayFinderUtility.loadTempGame();
			}
			if(nextPlay != null) {
				System.out.println("BRANCHINGPLAY: Setting result to nextPlay value"); //TODO
				result = nextPlay.getValue(game).getValue();
			}
			result *= chanceToOccur;
			weightedValue = result / 100;
		}
		return new PlayReturn(weightedValue, game);
	}

	public Play getNextPlay() {
		System.out.println("BRANCHINGPLAY: Entered getNextPlay. Next play: " + nextPlay);
		return nextPlay;
	}
}