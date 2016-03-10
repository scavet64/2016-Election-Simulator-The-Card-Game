package cardGame_v1.Model;

public enum Type {
	Fighter ("Genius", "Charasmatic"),
	Psycho ("Spooky", "Magic"),
	Magic ("Psycho", "Genius"),
	Genius ("Magic", "Fighter"),
	Charasmatic ("Fighter", "Spooky"),
	Spooky ("Charasmatic", "Psycho"), 
	Forbidden("", "");
	
	private final int HIGHLY_EFFECTIVE_MODIFIER = 2;
	private final int NOT_VERY_EFFECTIVE_MODIFIER = -2;
	private final String HIGHLY_EFFECTIVE_STRING = "Attack was highly effective!\n";
	private final String NOT_VERY_EFFECTIVE_STRING = "Attack was not very effective\n";
	
	String highlyEffective;
	String notVeryEffective;
	
	Type(String highlyEffective, String notVeryEffective) {
		this.highlyEffective = highlyEffective;
		this.notVeryEffective = notVeryEffective;
	}
	
	/**
	 * @param typeToCompare type to check for changes from other types
	 * @return int value for attack change due to types
	 */
	public int applyModifier(Type typeToCompare) {
		String opposingType = typeToCompare.toString();
		if(opposingType.equals(highlyEffective)) {
			return HIGHLY_EFFECTIVE_MODIFIER;
		}else if(opposingType.equals(notVeryEffective) || opposingType.equals(this.toString())) {
			return NOT_VERY_EFFECTIVE_MODIFIER;
		}else {
			return 0;
		}
	}

	/**
	 * @param typeToCompare Type to be checked for bonus effects on damage exchange
	 */
	public String modifierString(Type typeToCompare) {
		String opposingType = typeToCompare.toString();
		if(opposingType.equals(highlyEffective)) {
			return HIGHLY_EFFECTIVE_STRING;
		}else if(opposingType.equals(notVeryEffective) || opposingType.equals(this.toString())) {
			return NOT_VERY_EFFECTIVE_STRING;
		}else {
			return "";
		}
	}
}