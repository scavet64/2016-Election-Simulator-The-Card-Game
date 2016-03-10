package cardGame_v1.Model;

public class RareCreature extends Creature implements Enhanceable {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor for RareCreature
	 */
	public RareCreature(String name, int fatigueValue, int chanceToHit, int attack, int health, Type type, String imgFilePath, String fieldImgPath){
		super(name, fatigueValue, chanceToHit, attack, health, type, imgFilePath, fieldImgPath);
	}
	
	/**
	 * Deep copy constructor for RareCreature
	 */
	public RareCreature(RareCreature rc){
		super(rc.getName(), rc.getPlayFatigueValue(), rc.getChanceToHit(), rc.getAttackValue(), rc.getHealthValue(), 
				rc.getType(), rc.getImgFilePath(), rc.getFieldImgPath());
	}
}
