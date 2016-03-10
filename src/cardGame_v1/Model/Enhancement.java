package cardGame_v1.Model;

public class Enhancement extends Card {
	
	public static final String HEALTH = "health";
	public static final String CHANCE_TO_ATTACK = "chance to hit";
	public static final String ATTACK = "attack";
	public static final String FATIGUE = "fatigue";
	
	private static final long serialVersionUID = 1L;
	private String statToModify;
	private int modValue;
	
	/**
	 * Default constructor for Enhancement
	 */
	public Enhancement(String name, int fatigueValue, String stat, int modValue, String imgFilePath){
		super(name, fatigueValue, imgFilePath);
		this.statToModify = stat;
		this.modValue = modValue;
	}

	/**
	 * Deep copy constructor for Enhancement
	 */
	public Enhancement(Enhancement c) {
		super(c.getName(), c.getPlayFatigueValue(), c.getImgFilePath());
		this.statToModify = c.getStat();
		this.modValue = c.getModValue();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return statToModify
	 */
	public String getStat() {
		return statToModify;
	}

	/**
	 * @return modValue
	 */
	public int getModValue() {
		return modValue;
	}
}