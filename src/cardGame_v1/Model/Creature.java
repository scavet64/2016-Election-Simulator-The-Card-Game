package cardGame_v1.Model;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

import javax.imageio.ImageIO;

import cardGame_v1.ExceptionHandling.ImageNotFoundException;

public class Creature extends Card {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int attackValue;
	private int healthValue;
	private int attackFatigueValue;
	private int chanceToHit;
	private Type type;
	private String fieldImgPath;
	
	/**
	 * default original creature constructor
	 * @param name name of creature
	 * @param fatigueValue fatigue of creature
	 * @param chanceToHit chance to hit
	 * @param attack attack value
	 * @param health health value
	 * @param type type of creature
	 * @param imgFilePath filepath pointing to the card image
	 * @param fieldImgPath filepath pointing to the field image
	 */
	public Creature(String name, int fatigueValue, int chanceToHit, int attack, int health, Type type, String imgFilePath, String fieldImgPath){
		super(name, fatigueValue, imgFilePath);
		int attackFatigueValue = fatigueValue / 2;
		if(attackFatigueValue <= 0) attackFatigueValue = 1;
		this.attackFatigueValue = attackFatigueValue;
		this.attackValue = attack;
		this.healthValue = health;
		this.type = type;
		this.chanceToHit = chanceToHit;
		this.fieldImgPath = fieldImgPath;
	}
	
	public Creature(){
		super();
	}
	
	/**
	 * Constructor that takes a creature. This is used to create copies of the card
	 * @param c creature to be copied
	 */
	public Creature(Creature c){
		super(c.getName(), c.getPlayFatigueValue(), c.getImgFilePath());
		this.attackValue = c.getAttackValue();
		this.attackFatigueValue = c.getAttackFatigueValue();
		this.healthValue = c.getHealthValue();
		this.type = c.getType();
		this.chanceToHit = c.getChanceToHit();
		this.fieldImgPath = c.getFieldImgPath();
	}

	public String getFieldImgPath() {
		return fieldImgPath;
	}

	/**
	 * reduces the field image by the reduction percent provided. Uses the getScaledInstance within the Image class.
	 * @param reducePercent how reduced
	 * @return the reduced image
	 */
	public Image getReducedFieldImageSize(double reducePercent) throws ImageNotFoundException{
		Image fieldImage = getFieldImage();
		int imgHight = fieldImage.getHeight(null);
		int imgWidth = fieldImage.getWidth(null);
		
		Double reducedHightDouble = (imgHight * reducePercent);
		Double reducedWidthDouble = (imgWidth * reducePercent);
		
		int reducedHightAsInt = reducedHightDouble.intValue();
		int reducedWidthAsInt = reducedWidthDouble.intValue();
		
		return fieldImage.getScaledInstance(reducedWidthAsInt, reducedHightAsInt, 0);
	}

	
	////Setters and Getters \\\\\
	
	/**
	 * Reads and creates an Image object that represents the fieldImgPath
	 * @return the Image object created
	 * @throws ImageNotFoundException
	 */
	public Image getFieldImage() throws ImageNotFoundException{
		try {
			return ImageIO.read(new File(fieldImgPath));
		} catch (IOException e) {
			throw new ImageNotFoundException(fieldImgPath, LocalDateTime.now());
		}
	}
	
	/**
	 * @return the attackValue
	 */
	public int getAttackValue() {
		return attackValue;
	}

	/**
	 * @param attackValue the attackValue to set
	 */
	public void setAttackValue(int attackValue) {
		if(attackValue < 0) attackValue = 0;
		this.attackValue = attackValue;
	}

	/**
	 * @return the healthValue
	 */
	public int getHealthValue() {
		return healthValue;
	}

	/**
	 * @param healthValue the healthValue to set
	 */
	public void setHealthValue(int healthValue) {  
		this.healthValue = healthValue;
	}

	/**
	 * @return the attackFatigueValue
	 */
	public int getAttackFatigueValue() {
		return attackFatigueValue;
	}

	/**
	 * @param attackFatigueValue the attackFatigueValue to set
	 */
	public void setAttackFatigueValue(int attackFatigueValue) {
		if(attackFatigueValue < 1) attackFatigueValue = 1;
		this.attackFatigueValue = attackFatigueValue;
	}

	/**
	 * @return the chanceToHit
	 */
	public int getChanceToHit() {
		return chanceToHit;
	}

	/**
	 * @param chanceToHit the chanceToHit to set
	 */
	public void setChanceToHit(int chanceToHit) {
		this.chanceToHit = chanceToHit;
	}

	/**
	 * @return the type
	 */
	public Type getType() {
		return type;
	}

//	/**
//	 * @param type the type to set
//	 */
//	public void setType(Type type) {
//		this.type = type;
//	}
}
