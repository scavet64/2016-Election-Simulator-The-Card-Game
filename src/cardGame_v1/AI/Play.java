package cardGame_v1.AI;

import java.util.HashMap;

import cardGame_v1.Controller.Game;

public class Play {
	private Move move;
	private double value = Double.MIN_NORMAL;
	private HashMap<PlayOutcome,BranchingPlay> outcomeToBranchingPlayMap = new HashMap<PlayOutcome,BranchingPlay>();
	
	public Play(Move move, Game game) {
		this.move = move;
		findBranchingPlays(game);
	}
	
	private void findBranchingPlays(Game game) {
		MoveCase moveCase = move.getMoveCase();
		switch(moveCase){
		case PlayCard:
			outcomeToBranchingPlayMap.put(PlayOutcome.NA, new BranchingPlay(PlayOutcome.NA, 1, move));
			break;
		case AttackCard:
			outcomeToBranchingPlayMap.put(PlayOutcome.HH, new BranchingPlay(PlayOutcome.HH, PlayFinderUtility.getChanceToOccur(PlayOutcome.HH, move, game), move));
			outcomeToBranchingPlayMap.put(PlayOutcome.HM, new BranchingPlay(PlayOutcome.HM, PlayFinderUtility.getChanceToOccur(PlayOutcome.HM, move, game), move));
			outcomeToBranchingPlayMap.put(PlayOutcome.MH, new BranchingPlay(PlayOutcome.MH, PlayFinderUtility.getChanceToOccur(PlayOutcome.MH, move, game), move));
			outcomeToBranchingPlayMap.put(PlayOutcome.MM, new BranchingPlay(PlayOutcome.MM, PlayFinderUtility.getChanceToOccur(PlayOutcome.MM, move, game), move));
			break;
		case AttackPlayer:
			outcomeToBranchingPlayMap.put(PlayOutcome.H, new BranchingPlay(PlayOutcome.H, PlayFinderUtility.getChanceToOccur(PlayOutcome.H, move, game), move));
			outcomeToBranchingPlayMap.put(PlayOutcome.M, new BranchingPlay(PlayOutcome.M, PlayFinderUtility.getChanceToOccur(PlayOutcome.M, move, game), move));
			break;			
		}
	}
	
	public Move getMove(){
		return move;
	}
	
	public PlayReturn getValue(Game game){
		if(value == Double.MIN_NORMAL) {
			value = 0;
			for(BranchingPlay branch : outcomeToBranchingPlayMap.values()) {
				PlayReturn branchReturn = branch.getWeightedValue(game);
				value += branchReturn.getValue();
				game = branchReturn.getUpdatedGame();
			}
			System.out.println("PLAY: Play value = " + value); //TODO
		}
		return new PlayReturn(value, game);
	}
	
	public Play getNextPlay(PlayOutcome outcome){
		return outcomeToBranchingPlayMap.get(outcome).getNextPlay();
	}	
}