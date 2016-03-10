package cardGame_v1.Model;
import java.util.ArrayList;

public class AllCards {
    
    private static AllCards instance;
    private static ArrayList<Card> allCards = new ArrayList<Card>();
		
    /**
     * Creation of all cards in the game
     */
	private AllCards(){
		//initialize all cards here		
									//name,fatigue,chance,attack,health,type,CardImage,FieldImage
			//Charismatic Cards
			allCards.add(new Creature("Hank Hill", 1, 80, 1, 2,Type.Charasmatic, "CardImages//hankCard.png", "FieldImages//hankField.png"));
			allCards.add(new Creature("Billy Mays", 2, 90, 4, 2, Type.Charasmatic, "CardImages//billyCard.png", "FieldImages//BillyMaysField.png"));
			allCards.add(new Creature("Esteban Winsmore", 3, 90, 5, 3, Type.Charasmatic, "CardImages//EstebanCard.png", "FieldImages//EstebanField.png"));
			allCards.add(new Creature("Handsome Squidward", 5, 75, 5, 5, Type.Charasmatic, "CardImages//HsquidCard.png", "FieldImages//handsomeSquidwardField.png"));
			allCards.add(new RareCreature("Vladimir Putin", 7, 90, 7, 8, Type.Charasmatic, "CardImages//VladimirPutinCard.png", "FieldImages//VladPutinField.png"));
			allCards.add(new RareCreature("Based God", 2, 100, 4, 5,Type.Charasmatic, "CardImages//lilBCard.png", "FieldImages//basedGodField.png"));
			
			//Fighter Cards
			allCards.add(new Creature("Butch DeLoria", 2, 85, 3, 1, Type.Fighter, "CardImages//tunnelCard.png", "FieldImages//tunnelField.png"));
			allCards.add(new RareCreature("Champ", 3, 90, 3, 3, Type.Fighter, "CardImages//champCard.png", "FieldImages//champField.png"));
			allCards.add(new Creature("Adam Jensen", 4, 95, 3, 4, Type.Fighter, "CardImages//adamCard.png", "FieldImages//adamField.png"));
			allCards.add(new Creature("Salty Bouncer", 5, 85, 6, 4, Type.Fighter, "CardImages//saltyCard.png", "FieldImages//saltyField.png"));
			allCards.add(new Creature("Sanic", 5, 75, 6, 6, Type.Fighter, "CardImages//sanicCard.png", "FieldImages//sanicField.png"));
			allCards.add(new RareCreature("The Regginator", 8, 85, 7, 9, Type.Fighter, "CardImages//regginatorCard.png", "FieldImages//regginatorField.png"));
			
			//Genius Cards
			allCards.add(new Creature("Walter White", 3, 85, 3, 4, Type.Genius, "CardImages//waltCard.png", "FieldImages//waltField.png"));
			allCards.add(new Creature("Tony Stark", 5, 85, 5, 7, Type.Genius, "CardImages//tonyCard.png", "FieldImages//tonyStarkField.png"));
			allCards.add(new Creature("Todd Howard", 5, 70, 6, 6, Type.Genius, "CardImages//toddCard.png", "FieldImages//toddField.png"));
			allCards.add(new RareCreature("Gabe Newell", 6, 70, 7, 7, Type.Genius, "CardImages//gabeCard.png", "FieldImages//gabeField.png"));
			allCards.add(new Creature("Bill Nye The Science Guy", 7, 95, 4, 10, Type.Genius, "CardImages//billCard.png", "FieldImages//billField.png"));
			allCards.add(new RareCreature("Rick Sanchez", 7, 80, 7, 10, Type.Genius, "CardImages//rickCard.png", "FieldImages//rickField.png"));
			
			//Magic Cards
			allCards.add(new Creature("Glass Bones", 2, 90, 6, 1, Type.Magic, "CardImages//glassBonesCard.png", "FieldImages//glassBonesField.png"));
			allCards.add(new Creature("Rock Lobster", 3, 85, 5, 3, Type.Magic, "CardImages//rockCard.png", "FieldImages//rockField.png"));
			allCards.add(new RareCreature("Snoop Dogg", 4, 20, 4, 20, Type.Magic, "CardImages//snoopDoggCard.png", "FieldImages//snoopDoggField.png"));
			allCards.add(new Creature("Mr. Meeseeks", 5, 100, 5, 7, Type.Magic, "CardImages//mrMeeSeeksCard.png", "FieldImages//mrMeeseeksField.png"));
			allCards.add(new Creature("Cromulon", 7,  95, 6, 7,Type.Magic, "CardImages//cromulonCard.png", "FieldImages//CromulonField.png"));
			allCards.add(new RareCreature("Bob Ross", 8, 80, 9, 9, Type.Magic, "CardImages//bobrossCard.png", "FieldImages//bobRossField.png"));
			
			//Psycho Cards
			allCards.add(new Creature("Blue Eyes Orange Orange", 1, 40, 6, 2,Type.Psycho, "CardImages//OrangeOrangeCard.png", "FieldImages//OrangeOrange.png"));
			allCards.add(new Creature("Old Timer", 2, 85, 2, 4, Type.Psycho, "CardImages//oldTimerCard.png", "FieldImages//oldTimerField.png"));
			allCards.add(new RareCreature("Kim Jong Un", 4, 50, 7, 5, Type.Psycho, "CardImages//kimJongUnCard.png", "FieldImages//kimJongUnField.png"));
			allCards.add(new Creature("Brian Peppers", 4, 75, 6, 5, Type.Psycho, "CardImages//brianPepsCard.png", "FieldImages//brianPepsField.png"));
			allCards.add(new Creature("Trevor Phillips", 5, 70, 6, 7, Type.Psycho, "CardImages//trevorCard.png", "FieldImages//trevorField.png"));
			allCards.add(new RareCreature("Freaky Fred", 7, 85, 6, 8, Type.Psycho, "CardImages//FreakyFredCard.png", "FieldImages//freakyFredField.png"));
			
			//Spooky Cards
			allCards.add(new Creature("Count Chocula", 1, 85, 2, 1, Type.Spooky, "CardImages//countChocCard.png", "FieldImages//countchocfield.png"));
			allCards.add(new Creature("Spooky Police", 2, 75, 3, 2,Type.Spooky, "CardImages//spookyPoliceCard.png", "FieldImages//spookyPoliceField.png"));
			allCards.add(new RareCreature("Mr. Bones", 3, 95, 1, 10 ,Type.Spooky, "CardImages//MrBonesCard.png", "FieldImages//mrbonesField.png"));
			allCards.add(new Creature("King Ramsee", 3, 80, 4, 4,Type.Spooky, "CardImages//KingRamsesCard.png", "FieldImages//kingramsesField.png"));
			allCards.add(new Creature("Shrek", 6, 70, 8, 6,Type.Spooky, "CardImages//ShrekCard.png", "FieldImages//ShrekField.png"));
			allCards.add(new RareCreature("Mr. Skeltal", 7, 90, 6, 8,Type.Spooky, "CardImages//mrSkeltalCard.png", "FieldImages//mrSkeltalField.png"));
			
			//The Forbidden Cards
			allCards.add(new JackCard("Left Arm of Jack Myers", 3, 95, 2, 4,Type.Forbidden, "CardImages//leftArmOfJack.png", "FieldImages//leftArmOfJackField.png", 1));
			allCards.add(new JackCard("Right Arm of Jack Myers", 3, 95, 2, 4,Type.Forbidden, "CardImages//rightArmOfJack.png", "FieldImages//rightArmOfJackField.png", 2));
			allCards.add(new JackCard("Left Leg of Jack Myers", 3, 95, 2, 4,Type.Forbidden, "CardImages//leftLegOfJack.png", "FieldImages//leftLegOFJackField.png", 3));
			allCards.add(new JackCard("Right Leg of Jack Myers", 3, 95, 2, 4,Type.Forbidden, "CardImages//rightLegOfJack.png", "FieldImages//rightLegOfJackField.png", 4));
			allCards.add(new JackCard("Head of Jack Myers", 3, 95, 2, 4,Type.Forbidden, "CardImages//headOfJack.png", "FieldImages//headofJackField.png", 5));

			//Enhancements
			allCards.add(new Enhancement("The Rock", 4, Enhancement.FATIGUE, -2, "Enhancement images//EtheRock.png"));
			allCards.add(new Enhancement("Airhorn", 2, Enhancement.ATTACK, 3, "Enhancement images//EAirhorn.png"));
			allCards.add(new Enhancement("Cheesy Keyboard", 2, Enhancement.ATTACK, -3, "Enhancement images//ECheese.png"));
			allCards.add(new Enhancement("Fallout 4", 5, Enhancement.CHANCE_TO_ATTACK, -50, "Enhancement images//Efallout4.png"));
			allCards.add(new Enhancement("The Fedora", 2, Enhancement.HEALTH, -2, "Enhancement images//Efedora.png"));
			allCards.add(new Enhancement("Monster Energy", 0, Enhancement.FATIGUE, -1, "Enhancement images//EMonster.png"));
			allCards.add(new Enhancement("Power Armor", 4, Enhancement.HEALTH, 5, "Enhancement images//Epowerarmor.png"));
			allCards.add(new Enhancement("Mom's Spaghetti", 3, Enhancement.CHANCE_TO_ATTACK, -25, "Enhancement images//Espaghetti.png"));
			allCards.add(new Enhancement("Stimpack", 2, Enhancement.HEALTH, 3, "Enhancement images//Estimpack.png"));
			allCards.add(new Enhancement("Oh Baby a Triple!", 3, Enhancement.ATTACK, 3, "Enhancement images//Etriple.png"));
			allCards.add(new Enhancement("Energy Sword", 0, Enhancement.ATTACK, 1, "Enhancement images//Esword.png"));
	}
	
	/**
	 * @Returns instance
	 */
	public static AllCards getInstance(){
		if(instance == null){
			instance = new AllCards();
		}
		return instance;
	}
	
	/**
	 * @return allCards
	 */
	public ArrayList<Card> getAllCards(){
		return allCards;
	}

	/**
	 *@param name the name of the card to look for
	 *@return card the card if the name matches a card in allCards
	 *@return null if card not found 
	 */
	public static Card getCardFromName(String name){
		for (Card card : allCards) {
			if(name.equals(card.getName())){
				return card;
			}
		}
		return null;
	}
	
	/**
	 * @return rareCards a collection of cards that are marked as rare
	 */
	public static ArrayList<Card> getRareCardsList(){
		ArrayList<Card> rareCards = new ArrayList<Card>();
		for(Card card: allCards){
			if(card instanceof Creature){
				if(((Creature) card) instanceof Enhanceable){
					rareCards.add(card);
				}
			}
		}
		return rareCards;
	}
		
}