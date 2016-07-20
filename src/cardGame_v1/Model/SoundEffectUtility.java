package cardGame_v1.Model;

import java.io.File;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundEffectUtility {

	private static final boolean LOOP = true;
	private static final boolean NOLOOP = false;
	private static Clip clip;
	private static Clip music;
	
	//Sound effects array
	private static final String[] TrumpSoundFileLocations = {"Sounds/TrumpAttack1.wav", "Sounds/TrumpAttack2.wav", "Sounds/TrumpAttack3.wav"};
	private static final String[] TrumpMissSoundFileLocations = {"Sounds/TrumpMiss1.wav","Sounds/TrumpMiss2.wav"};
	
	public static void playMusic() {
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Sounds/AmericaMusicLow.wav").getAbsoluteFile());
	        music = AudioSystem.getClip();
	        music.open(audioInputStream);
	        //clip.start();
	        music.loop(Clip.LOOP_CONTINUOUSLY);
	    } catch(Exception ex) {
	        System.err.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	
	public static void PlayTrumpAttackHitEffect(){
		Random rng = new Random();
		playSoundEffect(TrumpSoundFileLocations[rng.nextInt(TrumpSoundFileLocations.length)],NOLOOP);
	}
	
	public static void PlayTrumpAttackMissEffect(){
		Random rng = new Random();
		playSoundEffect(TrumpMissSoundFileLocations[rng.nextInt(TrumpMissSoundFileLocations.length)],NOLOOP);
	}
	
	private static void playSoundEffect(String fileName, Boolean isLoop){
		try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(fileName).getAbsoluteFile());
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        
	        if(isLoop){
	        	clip.loop(Clip.LOOP_CONTINUOUSLY);
	        } else {
	        	clip.start();
	        }
	    } catch(Exception ex) {
	        System.err.println("Error with playing sound.");
	        ex.printStackTrace();
	    }
	}
	
	public static void playGameOverSound(boolean didTrumpWin){
		if(didTrumpWin){
			playSoundEffect("Sounds/YouLost.wav",NOLOOP);
			playSoundEffect("Sounds/TrumpWin.wav",LOOP);
			music.stop();
		} else {
			music.stop();
			//playSoundEffect("Sounds/YouLost.wav",NOLOOP);
			playSoundEffect("Sounds/TrumpLost.wav",NOLOOP);
		}
		
	}
	
	public static void playGameStartSound(){
		playSoundEffect("Sounds/TrumpGameStart.wav",NOLOOP);
	}
	
	public static void stopSounds(){
		if(music != null){
			music.stop();
		}
		
		if(clip != null){
			clip.stop();
		}
	}
	
}
