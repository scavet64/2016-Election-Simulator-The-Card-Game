package cardGame_v1.AI;

public class ApplyActionOutcome {
	private String messageString;
	private PlayOutcome outcome;
	
	/**
	 * @param messageString
	 * @param outcome
	 */
	public ApplyActionOutcome(String messageString, PlayOutcome outcome) {
		super();
		this.messageString = messageString;
		this.outcome = outcome;
	}
	
	/**
	 * @return the messageString
	 */
	public String getMessageString() {
		return messageString;
	}
	
	/**
	 * @param messageString the messageString to set
	 */
	public void setMessageString(String messageString) {
		this.messageString = messageString;
	}
	
	/**
	 * @return the outcome
	 */
	public PlayOutcome getOutcome() {
		return outcome;
	}
	
	/**
	 * @param outcome the outcome to set
	 */
	public void setOutcome(PlayOutcome outcome) {
		this.outcome = outcome;
	}
	

}
