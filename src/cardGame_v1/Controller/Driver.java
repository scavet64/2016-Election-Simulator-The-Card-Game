package cardGame_v1.Controller;

import cardGame_v1.GUI.MainGUI;
import cardGame_v1.Model.AllCards;
import cardGame_v1.Model.Card;

public class Driver {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Menu menu = new Menu();
		AllCards.getInstance();
		createMasterProfile(menu);
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
