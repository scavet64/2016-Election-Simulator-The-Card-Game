package cardGame_v1.AI;

import java.util.HashMap;

import cardGame_v1.Controller.Game;

public class Play {
	private Move move;
	private Double value;
	private HashMap<PlayOutcome,BranchingPlay> outcomeToBranchingPlayMap;
	
	public Play(Move move) {
		this.move = move;
		findBranchingPlays();
	}
	
	/**
	 * TODO Figure out how to get and pass the chance of occurrence for each branch
	 */
	private void findBranchingPlays() {
		MoveCase moveCase = move.getMoveCase();
		switch(moveCase){
		case PlayCard:
			outcomeToBranchingPlayMap.put(PlayOutcome.NA, new BranchingPlay(PlayOutcome.NA, 100, move));
			break;
		case AttackCard:
			outcomeToBranchingPlayMap.put(PlayOutcome.HH, new BranchingPlay(PlayOutcome.HH, ));
			outcomeToBranchingPlayMap.put(PlayOutcome.HM, new BranchingPlay(PlayOutcome.HM, ));
			outcomeToBranchingPlayMap.put(PlayOutcome.MH, new BranchingPlay(PlayOutcome.MH, ));
			outcomeToBranchingPlayMap.put(PlayOutcome.MM, new BranchingPlay(PlayOutcome.MM, ));
			break;
		case AttackPlayer:
			outcomeToBranchingPlayMap.put(PlayOutcome.H, new BranchingPlay(PlayOutcome.H, ));
			outcomeToBranchingPlayMap.put(PlayOutcome.M, new BranchingPlay(PlayOutcome.M, ));
			break;			
		}
	}
	
	public Move getMove(){
		return move;
	}
	
	public double getValue(Game game){
		double result = 0;
		if(value != null) {
			result = value;
		}else {
			for(BranchingPlay branch : outcomeToBranchingPlayMap.values()) {
				result += branch.getWeightedValue(game);
			}
			value = result;
		}
		return result;
	}
	
	public Play getNextPlay(PlayOutcome outcome){
		return outcomeToBranchingPlayMap.get(outcome).getNextPlay();
	}	
}