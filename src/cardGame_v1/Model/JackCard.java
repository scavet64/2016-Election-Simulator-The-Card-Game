package cardGame_v1.Model;

public class JackCard extends RareCreature {
	private static final long serialVersionUID = 1L;
	private int pieceNumber;

	/**
	 * Default constructor for the god card Jack
	 */
	public JackCard(String name, int fatigueValue, int chanceToHit, int attack, int health, Type type,
			String imgFilePath, String fieldImgPath, int pieceNumber) {
		super(name, fatigueValue, chanceToHit, attack, health, type, imgFilePath, fieldImgPath);
		this.pieceNumber = pieceNumber;
		// TODO Auto-generated constructor stub
	}
	
	public JackCard(){
		
	}

	/**
	 * Deep copy constructor for the god card Jack
	 */
	public JackCard(JackCard rc) {
		super(rc);
		this.pieceNumber = rc.getPieceNumber();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the pieceNumber
	 */
	public int getPieceNumber() {
		return pieceNumber;
	}

	/**
	 * @param pieceNumber the pieceNumber to set
	 */
	public void setPieceNumber(int pieceNumber) {
		this.pieceNumber = pieceNumber;
	}
}
