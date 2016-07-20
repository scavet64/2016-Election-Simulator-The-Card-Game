package cardGame_v1.AI;

import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import cardGame_v1.Controller.Game;
import cardGame_v1.Controller.Player;
import cardGame_v1.GUI.GameGUI;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.Deck;
import cardGame_v1.Model.SoundEffectUtility;
import cardGame_v1.Model.UserProfile;

/**
 * 
 * @author :^)
 *
 */
public class AI extends Player {
	//private Game game;
	private final String TEMP_GAME_FILE_NAME = "tempGame.ser";

	//TODO May not need game passed
	public AI(Integer playerSide, UserProfile profile, HashMap<Integer, HashMap<Integer, Creature>> field, Game currentGame) {
		super(playerSide, profile, field);
		//this.game = currentGame;
	}
	
	public AI(){}
	
	/**
	 * Plays the AI's turn out determined by the minimax algorithm
	 * @TODO:
	 * 		Gets the best turn from minimax
	 * 		Call playAction with the values from minimax
	 * 		PlayAction will take care of the move from there
	 */
	public Game playTurn(Game game, GameGUI gameGUI){
		long startTime = System.nanoTime();
	    System.out.println("\n\n\n\n\nAI: Current Turn at start of AI move = " + game.getTurn()); //TODO
		Game gameDeepCopy = PlayFinderUtility.getDeepCopyGame(game);
		
		//Find best play sequence
		/******* This probs should have a class *****************/
		Object[] playResult = PlayFinderUtility.findPlay(game);
		Play currentPlay = (Play) playResult[0];
		game = (Game) playResult[1];
		/********************************************************/
		
		//Compare whether a pass is more favorable
		if(currentPlay != null) {
			//System.out.println("AI: Switching to MIN"); //TODO
			PlayFinderUtility.setValueFlag(PlayFinderUtility.MIN);
			game.endTurn();
			/******* This probs should have a class *****************/
			playResult = PlayFinderUtility.findPlay(game);
			Play tempPlay = (Play) playResult[0];
			game = (Game) playResult[1];
			/********************************************************/
			double passValue;
			if(tempPlay == null) {
				passValue = PlayFinderUtility.gameStateEvaluation(game);
			}else {
				PlayReturn playReturn = tempPlay.getValue(game);
				passValue = playReturn.getValue();
				game = playReturn.getUpdatedGame();
			}
			//System.out.println("BRANCHINGPLAY: Switching back to MAX"); //TODO
			PlayFinderUtility.setValueFlag(PlayFinderUtility.MAX);
			if(passValue > currentPlay.getValue(game).getValue()) {
				//A Pass is more favorable
				currentPlay = null;
			}
		}
		
		game = gameDeepCopy;
		boolean hasHit = false;
		boolean hasMiss = false;
		
		while(currentPlay != null){
			Move move = currentPlay.getMove();
			ApplyActionOutcome realOutcome = game.applyAction(move.getFirstCardSelection(), move.getSecondCardSelection());
			
			if(game.isGameOver()) {		
				SoundEffectUtility.playGameOverSound(true);
				gameGUI.displayWin(game.getCurrentPlayer().getProfile(), game.getOpposingPlayer().getProfile());
				return game;
			}
			gameGUI.addToActionLog(realOutcome.getMessageString());
			
			switch(realOutcome.getOutcome()) {
			case HH:
			case HM:
			case H:
				hasHit = true;
				break;
			case MH:
			case MM:
			case M:
				hasMiss = true;
				break;
			default:
				break;
			}
			
			//System.out.println("AI: RealOutcome = " + realOutcome.getOutcome()); //TODO
			currentPlay = currentPlay.getNextPlay(realOutcome.getOutcome());
			//delayPlay(3000);
		}
		
		if(hasHit) SoundEffectUtility.PlayTrumpAttackHitEffect();
		else if(hasMiss) SoundEffectUtility.PlayTrumpAttackMissEffect();
		
		double turnLength = (System.nanoTime() - startTime) * 0.000000001;
		//System.out.println("AI: LEAVING play Turn");
		System.out.println("AI: LENGTH OF TURN = " + turnLength);
		return game;
	}
	
	private void delayPlay(int delayTime) {
		long startTime = System.nanoTime();
		
		long endTime = startTime + delayTime;
		while(endTime > System.nanoTime());
	}
}