package cardGame_v1.ExceptionHandling;

import java.io.IOException;
import java.time.LocalDateTime;

public class ProfileNotFoundException extends IOException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String profileNotFound;
	private LocalDateTime dateNotFound;
	
	/**
	 * default constructor for the exception
	 * @param profileNotFound profile name that could not be found
	 * @param dateNotFound date and time the profile could not be loaded
	 */
	public ProfileNotFoundException(String profileNotFound, LocalDateTime dateNotFound) {
		this.profileNotFound = profileNotFound;
		this.dateNotFound = dateNotFound;
	}
	
	public String toString() {
		return profileNotFound + " could not be located.\nError Time: " + dateNotFound;
	}
}
