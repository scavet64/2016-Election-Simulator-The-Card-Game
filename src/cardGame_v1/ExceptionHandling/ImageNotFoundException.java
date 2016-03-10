package cardGame_v1.ExceptionHandling;

import java.io.IOException;
import java.time.LocalDateTime;

public class ImageNotFoundException extends IOException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fileName;
	private LocalDateTime currentTime;
	
	/**
	 * default constructor for the imageNotFoundException
	 * @param fileName the filename that could not be loaded
	 * @param currentTime the current date and time the file couldnt be loaded
	 */
	public ImageNotFoundException(String fileName, LocalDateTime currentTime){
		this.fileName = fileName;
		this.currentTime = currentTime;
	}
	
	public String toString(){
		return "The image '" + fileName + "' was not found at " + currentTime; 
	}
	
}
