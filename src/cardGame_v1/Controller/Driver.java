package cardGame_v1.Controller;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

import cardGame_v1.ExceptionHandling.DeckFullException;
import cardGame_v1.GUI.MainGUI;
import cardGame_v1.Model.AllCards;
import cardGame_v1.Model.Card;
import cardGame_v1.Model.Deck;
import cardGame_v1.Model.UserProfile;

public class Driver {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		printToFile(); //careful using this as it could get out of hand
		Menu menu = new Menu();
		AllCards.getInstance();
		createMasterProfile(menu);
		createAIProfile(menu);
		//createNonRandAIProfile(menu);
		MainGUI menuGUI = new MainGUI(menu);
	}

	private static void createMasterProfile(Menu menu){
		menu.createProfile("Master");
		menu.getActiveProfile().setFirstLoad(false);
		for(Card card: AllCards.getInstance().getAllCards()){
			menu.getActiveProfile().addCard(card);
			menu.getActiveProfile().addCard(card);
		}
		menu.saveActiveProfile();
	}
	
	private static void createNonRandAIProfile(Menu menu){
		menu.createProfile("aiPlayer");
		UserProfile aiProfile = menu.getActiveProfile();
		ArrayList<Card> allCards = AllCards.getInstance().getAllCards();
		for(Card card: allCards){
			aiProfile.addCard(card);
			aiProfile.addCard(card);
		}
		Deck aiDeck = aiProfile.getDeck();
			try {
				aiDeck.addCard(allCards.get(0));
				//aiDeck.addCard(allCards.get(0));
				//aiDeck.addCard(allCards.get(42));
				//aiDeck.addCard(allCards.get(1));
				aiDeck.addCard(allCards.get(26));
				//aiDeck.addCard(allCards.get(24));
			} catch (DeckFullException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			menu.saveActiveProfile();
	}
	
	private static void createAIProfile(Menu menu){
		Random rng = new Random();
		menu.createProfile("aiPlayer");
		UserProfile aiProfile = menu.getActiveProfile();
		ArrayList<Card> allCards = AllCards.getInstance().getAllCards();
		for(Card card: allCards){
			aiProfile.addCard(card);
			aiProfile.addCard(card);
		}
		Deck aiDeck = aiProfile.getDeck();
		int totalCardsInGame = allCards.size();
		boolean isFinished = false;
		while(!isFinished){
			try {
				aiDeck.addCard(allCards.get(rng.nextInt(totalCardsInGame-1)));
			} catch (DeckFullException e) {
				// TODO Auto-generated catch block
				isFinished = true;
			}
		}
		
		menu.saveActiveProfile();
	}
	
	private static void printToFile(){
		try {
			System.setOut(new PrintStream(new File("output.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	//not used ATM but can be used if we want to have more than one master profile with different names
	@SuppressWarnings("unused")
	private static void createDemoPlayer(Menu menu, String playerName){
		menu.createProfile(playerName);
		for(Card card: AllCards.getInstance().getAllCards()){
			menu.getActiveProfile().addCard(card);
			menu.getActiveProfile().addCard(card);
		}
		menu.saveActiveProfile();
	}
}
