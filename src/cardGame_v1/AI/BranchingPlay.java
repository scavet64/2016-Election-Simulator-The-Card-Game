package cardGame_v1.AI;

import cardGame_v1.Controller.Game;

public class BranchingPlay {
	private Move move;
	private PlayOutcome playOutcome;
	private double weightedValue = Double.MIN_NORMAL;
	private Play nextPlay;
	private double chanceToOccur;

	/**
	 * 
	 * @param playOutcome
	 * @param chanceToOccur
	 * @param move
	 */
	public BranchingPlay(PlayOutcome playOutcome, double chanceToOccur, Move move){
		this.playOutcome = playOutcome;
		this.chanceToOccur = chanceToOccur;
		this.move = move;
	}

	/**
	 * @return the gameStateValue
	 */
	public PlayReturn getWeightedValue(Game game) {
		if(weightedValue == Double.MIN_NORMAL) {
			double result = 0.0;
			//System.out.println("BRANCHINGPLAY: Checking chance: " + playOutcome); //TODO
			//PlayFinderUtility.serializeCurrentGameState(game);
			PlayFinderUtility.kryoSerializeCurrentGameState(game);	//TODO testing
			if(playOutcome == PlayOutcome.NA) {
				game.applyAction(move.getFirstCardSelection(), move.getSecondCardSelection());
			}else {
				game.forceOutcome(playOutcome, move);
			}
			if((playOutcome == PlayOutcome.MM || playOutcome == PlayOutcome.M) 
					&& game.getCurrentPlayer().canAttack(game.getCreatureAtPosition(game.getCurrentPlayerTurn(), 
							Integer.parseInt(move.getFirstCardSelection()[1])))) {
				nextPlay = new Play(move, game);
			}else {
				/******* This probs should have a class *****************/
				Object[] playResult = PlayFinderUtility.findPlay(game);
				nextPlay = (Play) playResult[0];
				game = (Game) playResult[1];
				/********************************************************/
				if(PlayFinderUtility.valueFlag == PlayFinderUtility.MAX) {
					//System.out.println("BRANCHINGPLAY: Switching to MIN"); //TODO
					PlayFinderUtility.setValueFlag(PlayFinderUtility.MIN);
					game.endTurn();
					/******* This probs should have a class *****************/
					playResult = PlayFinderUtility.findPlay(game);
					Play tempPlay = (Play) playResult[0];
					game = (Game) playResult[1];
					/********************************************************/
					if(tempPlay == null) {
						result = PlayFinderUtility.gameStateEvaluation(game);
					}else {
						PlayReturn playReturn = tempPlay.getValue(game);
						result = playReturn.getValue();
						game = playReturn.getUpdatedGame();
					}
					//System.out.println("BRANCHINGPLAY: Switching back to MAX"); //TODO
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
				if(nextPlay != null) {
					result = nextPlay.getValue(game).getValue();
				}
			}
			//game = PlayFinderUtility.loadTempGame();	TODO testing
			game = PlayFinderUtility.kryoLoadGameState();
	
			result *= chanceToOccur;
			weightedValue = result;
			//System.out.println("BRANCHINGPLAY: Branch weighted value = " + weightedValue); //TODO
		}
		return new PlayReturn(weightedValue, game);
	}

	public Play getNextPlay() {
		System.out.println("BRANCHINGPLAY: Next play: " + nextPlay);
		return nextPlay;
	}
}
