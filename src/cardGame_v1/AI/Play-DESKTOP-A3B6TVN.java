package cardGame_v1.AI;

import java.util.HashMap;

public class Play {
	Move move;
	double value;
	HashMap<PlayOutcome,BranchingPlay> outcomeToBranchingPlayMap = new HashMap<PlayOutcome,BranchingPlay>();
	
	public Play(Move move) {
		this.move = move;
		value = 0.0;
		generateBranchingPlays();
	}

	private void generateBranchingPlays() {
		
		
	}
	
	
}
