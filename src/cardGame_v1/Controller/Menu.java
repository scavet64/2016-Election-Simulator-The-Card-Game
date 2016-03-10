package cardGame_v1.Controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDateTime;

import cardGame_v1.ExceptionHandling.ProfileNotFoundException;
import cardGame_v1.Model.UserProfile;

public class Menu {
	
	private UserProfile activeProfile;

	public Menu() {
		
	}
	
	/**
	 * Load the profile with the given string. Throw a ProfileNotFoundException 
	 * if the profile file could not be found.
	 * @param profileToLoad The profile to load
	 * @return The loaded UserProfile
	 * @throws ProfileNotFoundException
	 * @throws ClassNotFoundException
	 */
	public UserProfile loadProfile(String profileToLoad) throws ProfileNotFoundException, ClassNotFoundException {
		
		//load profile.dat
		try {
		String profileFileNameString = "Profiles\\" + profileToLoad + ".dat";
		FileInputStream profileIn = new FileInputStream(profileFileNameString);
		ObjectInputStream profileois = new ObjectInputStream(profileIn);
		UserProfile profile = (UserProfile) profileois.readObject();
		profileois.close();
		
		//activeProfile = profile;
		
		return profile;
		
		}catch(IOException e) {
			throw new ProfileNotFoundException(profileToLoad, LocalDateTime.now());
		}
	}
	
	/**
	 * Save the given profile.
	 * @param profileToSave The UserProfile to save to a file
	 * @return true if save was succesful, false if not
	 */
	public boolean saveProfile(UserProfile profileToSave){
		
		//point file to a folder named Profiles inside the root folder
		File profileFolder = new File("Profiles");
		
		//create the folder if it does not exist already
		if(!profileFolder.exists()) {
			profileFolder.mkdir();
		}
//		System.out.println(profileFolder.getAbsolutePath());
		
		//try with FileOutputStream resources
		try(FileOutputStream fout = new FileOutputStream("Profiles\\" + profileToSave.getName() + ".dat");
		    ObjectOutputStream oos = new ObjectOutputStream(fout);){
		    oos.writeObject(profileToSave);
		    oos.close();
		    return true;
		} catch(Exception e) { 
			//error saving profile
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Create a UserProfile with the given String
	 * @param name Name of UserProfile to create
	 */
	public void createProfile(String name) {
		UserProfile newUser = new UserProfile(name);
		setActiveProfile(newUser);
		saveProfile(newUser);
	}
	
	/**
	 * Delete a UserProfile with the given String
	 * @param profileToDelete Name of UserProfile to delete
	 */
	public void deleteProfile(String profileToDelete){
		File file = new File("Profiles\\" + profileToDelete + ".dat");		//Creates a file object that points to the file passed to the constructor.
		file.delete();
	}

	/**
	 * Save the active profile.
	 */
	public void saveActiveProfile() {
		saveProfile(activeProfile);
	}

	/**
	 * @return the activeProfile
	 */
	public UserProfile getActiveProfile() {
		return activeProfile;
	}

	/**
	 * @param activeProfile the activeProfile to set
	 */
	public void setActiveProfile(UserProfile activeProfile) {
		this.activeProfile = activeProfile;
	}
}
