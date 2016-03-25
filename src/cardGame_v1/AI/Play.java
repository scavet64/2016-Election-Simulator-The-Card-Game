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
			//create our threads for each branching play
			PlayThread attackHHThread = new PlayThread(PlayOutcome.HH, move, game);
			PlayThread attackHMThread = new PlayThread(PlayOutcome.HM, move, game);
			PlayThread attackMHThread = new PlayThread(PlayOutcome.MH, move, game);
			
			//start the threads
			attackHHThread.start();
			attackHMThread.start();
			attackMHThread.start();
			outcomeToBranchingPlayMap.put(PlayOutcome.MM, new BranchingPlay(PlayOutcome.MM, PlayFinderUtility.getChanceToOccur(PlayOutcome.MM, move, game), move));
			
			//wait for these threads to finish and join back into the main thread
			try{
				attackHHThread.join();
				attackHMThread.join();
				attackMHThread.join();
			} catch (Exception e){
				e.printStackTrace();
			}
			break;
			
		case AttackPlayer:		
			PlayThread attackHThread = new PlayThread(PlayOutcome.H, move, game);
			
			//start the thread
			attackHThread.start();
			outcomeToBranchingPlayMap.put(PlayOutcome.M, new BranchingPlay(PlayOutcome.M, PlayFinderUtility.getChanceToOccur(PlayOutcome.M, move, game), move));
			
			//wait for the thread to finish and join back into the main thread
			try{
				attackHThread.join();
			} catch (Exception e){
				e.printStackTrace();
			}
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
	
	private class PlayThread extends Thread{
		
		private Game game;
		private Move move;
		private PlayOutcome po;
		
		public PlayThread(PlayOutcome po, Move move, Game game){
			System.out.println("New Thread");
			this.game = game;
			this.move = move;
			this.po = po;
			this.setPriority(Thread.MAX_PRIORITY);
		}
		 
		public void run(){
			outcomeToBranchingPlayMap.put(po, new BranchingPlay(po, PlayFinderUtility.getChanceToOccur(po, move, game), move));
		}
	}
}