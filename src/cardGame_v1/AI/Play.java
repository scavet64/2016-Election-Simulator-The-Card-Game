package cardGame_v1.AI;

import java.util.HashMap;

public class Play {
	private Move move;
	private double value;
	private HashMap<PlayOutcome,BranchingPlay> outcomeToBranchingPlayMap;
	
	public Play(Move move) {
		
	}
	
	public Move getCurrentMove(){
		return move;
	}
	
	public double getValue(){
		return value;
	}
	
	public Move getNextMove(PlayOutcome outcome){
		return outcomeToBranchingPlayMap.get(outcome).getMove();
	}
	
	public Play getNextBestPlay(PlayOutcome outcome){
		
		return this;
	}
	
}
