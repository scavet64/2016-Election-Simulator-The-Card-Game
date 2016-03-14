package cardGame_v1.AI;

/**
 * 
 * @author :^)
 *
 */
public class Move {
	private String[] firstCardSelection;
	private String[] secondCardSelection;
	private MoveCase moveCase;
	
	
	/**
	 * @param firstCardSelection
	 * @param secondCardSelection
	 * @param moveCase
	 */
	public Move(String[] firstCardSelection, String[] secondCardSelection, MoveCase moveCase) {
		super();
		this.firstCardSelection = firstCardSelection;
		this.secondCardSelection = secondCardSelection;
		this.moveCase = moveCase;
	}
	
	
	/**
	 * @return the firstCardSelection
	 */
	public String[] getFirstCardSelection() {
		return firstCardSelection;
	}
	/**
	 * @param firstCardSelection the firstCardSelection to set
	 */
	public void setFirstCardSelection(String[] firstCardSelection) {
		this.firstCardSelection = firstCardSelection;
	}
	/**
	 * @return the secondCardSelection
	 */
	public String[] getSecondCardSelection() {
		return secondCardSelection;
	}
	/**
	 * @param secondCardSelection the secondCardSelection to set
	 */
	public void setSecondCardSelection(String[] secondCardSelection) {
		this.secondCardSelection = secondCardSelection;
	}
	/**
	 * @return the moveCase
	 */
	public MoveCase getMoveCase() {
		return moveCase;
	}
	/**
	 * @param moveCase the moveCase to set
	 */
	public void setMoveCase(MoveCase moveCase) {
		this.moveCase = moveCase;
	}
	
	
	
}


