package cardGame_v1.GUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;

import cardGame_v1.ExceptionHandling.CardNotFoundException;
import cardGame_v1.ExceptionHandling.DeckFullException;
import cardGame_v1.Model.AllCards;
import cardGame_v1.Model.Card;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.Deck;
import cardGame_v1.Model.Enhancement;
import cardGame_v1.Model.JackCard;
import cardGame_v1.Model.RareCreature;
import cardGame_v1.Model.UserProfile;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class DeckEditorGUI extends JPanel{
	/**
	 * Removed mouse and key listeners in version 2L
	 */
	private static final long serialVersionUID = 2L;
	private JPanel cardOnDisplayPanel;
	private CardImagePanel cardOnDisplay;
	private UserProfile profile;
	private ListPanel cardsInDeckListPanel;
	private ListPanel cardsAvailableListPanel;
	
	/**
	 * Constructor for DeckEditorGUI
	 * @param profile UserProfile to have the saved deck modified
	 * @param finishButton JButton to leave the gui and return to MainMenu
	 */
	public DeckEditorGUI(UserProfile profile, JButton finishButton){
		setLayout(new BorderLayout());
		this.profile = profile;
		
		//north
		
		//center panel
			cardOnDisplayPanel = new JPanel();
			cardOnDisplayPanel.setOpaque(false);
			cardOnDisplayPanel.setLayout(new BoxLayout(cardOnDisplayPanel, BoxLayout.X_AXIS));
			cardOnDisplay = new CardImagePanel(CardImagePanel.DECK_EDIT);
			Dimension d = new Dimension(330,470);
			cardOnDisplay.setPreferredSize(d);
			cardOnDisplay.setMinimumSize(d);
			cardOnDisplay.setMaximumSize(d);
			cardOnDisplay.getButton().setEnabled(false);
			cardOnDisplayPanel.add(Box.createVerticalGlue());
			cardOnDisplayPanel.add(Box.createHorizontalGlue());
			cardOnDisplayPanel.add(cardOnDisplay);
			cardOnDisplayPanel.add(Box.createVerticalGlue());
			cardOnDisplayPanel.add(Box.createHorizontalGlue());
			add(cardOnDisplayPanel, BorderLayout.CENTER);
		
		//west panel
			//create a collection of available cards to choose from
			ArrayList<String> stringsOfAvaliableCardsForDeck = new ArrayList<String>();
			stringsOfAvaliableCardsForDeck.addAll(profile.getStringsOfCardsOwned());
			
			//remove cards from the available choices if they are in the deck already
			for(String nameOfCardInDeck:profile.getDeck().getStringsOfCards()){
				stringsOfAvaliableCardsForDeck.remove(nameOfCardInDeck);
			}
			//create the panel 
			cardsAvailableListPanel = new ListPanel(stringsOfAvaliableCardsForDeck, 
					stringsOfAvaliableCardsForDeck.size() + "/" + profile.getCollectedCards().size() 
					+ " available Cards", ListPanel.MULTI_SELECTION, ListPanel.DUPLICATES);
			
			//Create the List Selection Listener that will be used to update the card on display when the selection changes
			ListSelectionListener listListen = new ListSelectionListener() {

				@Override
				public void valueChanged(ListSelectionEvent e) {
					@SuppressWarnings("unchecked")
					JList<String>sourceList = (JList<String>) e.getSource();
					String cardSelectedString = (String) sourceList.getSelectedValue();
					if(cardSelectedString != null){
						Card cardSelected = AllCards.getCardFromName(cardSelectedString);
						cardOnDisplay.updateDisplay(cardSelected, true);
						updateUI();
					}
				}
				
			};
			cardsAvailableListPanel.getCardsList().addListSelectionListener(listListen);
			add(cardsAvailableListPanel, BorderLayout.WEST);
		
		//east panel
			cardsInDeckListPanel = new ListPanel(profile.getDeck().getStringsOfCards(), 
					(profile.getDeck().getSize() + "/" + Deck.getLimit() + " Cards in deck"), ListPanel.MULTI_SELECTION, ListPanel.DUPLICATES);
			cardsInDeckListPanel.getCardsList().addListSelectionListener(listListen);
			add(cardsInDeckListPanel, BorderLayout.EAST);
		
		//south panel
			JPanel buttonsPanel = new JPanel();
			buttonsPanel.setOpaque(false);
			buttonsPanel.setLayout(new GridLayout(1,3));
			
				//add card button
				JPanel privateAddButtonPanel = new JPanel();
				privateAddButtonPanel.setOpaque(false);
				JButton addCardButton = new JButton("Add Card");
				privateAddButtonPanel.add(addCardButton);
				
				//remove card button
				JPanel privateRemoveButtonPanel = new JPanel();
				privateRemoveButtonPanel.setOpaque(false);
				JButton removeCardButton = new JButton("Remove Card");
				privateRemoveButtonPanel.add(removeCardButton);
				
				ActionListener addCardListener = new ActionListener() {
		
					@Override
					public void actionPerformed(ActionEvent arg0) {
						List<String> selectedValuesList = cardsAvailableListPanel.getCardsList().getSelectedValuesList();
						
							for(String cardToAdd: selectedValuesList){
								Card c = AllCards.getCardFromName(cardToAdd);
								Card clonedCard = null;
								if(c instanceof JackCard){
									clonedCard = new JackCard((JackCard) c);
								}else if(c instanceof RareCreature){
									clonedCard = new RareCreature((RareCreature) c);
								}else if(c instanceof Creature){
									clonedCard = new Creature((Creature) c);
								}else if(c instanceof Enhancement){
									clonedCard = new Enhancement((Enhancement) c);
								}else{
									JOptionPane.showMessageDialog(null, "Could not add card to the deck", "Add Error", JOptionPane.WARNING_MESSAGE);
								}
								try{
									profile.getDeck().addCard(clonedCard);
									cardsAvailableListPanel.getCollectionInList().remove(cardToAdd);
								}catch(DeckFullException e){
									JOptionPane.showMessageDialog(null, "Could not add " + clonedCard.getName() + ".\nYour deck is full!", "Add Error", JOptionPane.WARNING_MESSAGE);
								}catch(CardNotFoundException e){
									JOptionPane.showMessageDialog(null, "Could not add card to the deck.", "Add Error", JOptionPane.WARNING_MESSAGE);
								}
							}
							cardsAvailableListPanel.populateList(cardsAvailableListPanel.getCollectionInList());
							cardsInDeckListPanel.populateList(profile.getDeck().getStringsOfCards());
							updateLabels();
							cardsAvailableListPanel.getCardsList().addListSelectionListener(listListen);
							cardsInDeckListPanel.getCardsList().addListSelectionListener(listListen);
							cardsInDeckListPanel.updateUI();	
					}
				};
				
				ActionListener removeCardListener = new ActionListener() {
	
					@Override
					public void actionPerformed(ActionEvent arg0) {
						List<String> selectedValuesList = cardsInDeckListPanel.getCardsList().getSelectedValuesList();
						for(String cardToRemove: selectedValuesList){
							profile.getDeck().removeCard(AllCards.getCardFromName(cardToRemove));
							cardsAvailableListPanel.getCollectionInList().add(cardToRemove);
						}
						cardsAvailableListPanel.populateList(cardsAvailableListPanel.getCollectionInList()); 
						cardsInDeckListPanel.populateList(profile.getDeck().getStringsOfCards());
						cardsInDeckListPanel.getCardsList().addListSelectionListener(listListen);
						cardsAvailableListPanel.getCardsList().addListSelectionListener(listListen);
						
						updateLabels();
						cardsInDeckListPanel.updateUI();
					}
					
				};
				
				addCardButton.addActionListener(addCardListener);
				removeCardButton.addActionListener(removeCardListener);
			
				//Finish Editing Button
				JPanel privateFinishButtonPanel = new JPanel();
				privateFinishButtonPanel.setOpaque(false);
				privateFinishButtonPanel.add(finishButton);
				
				buttonsPanel.add(privateAddButtonPanel);
				buttonsPanel.add(privateFinishButtonPanel);
				buttonsPanel.add(privateRemoveButtonPanel);
		
			add(buttonsPanel, BorderLayout.SOUTH);
			setVisible(true);
		
		this.updateUI();
	}
	
	/**
	 * updates the labels to dynamically change
	 */
	private void updateLabels(){
		cardsInDeckListPanel.updateTitleLabel("/" + Deck.getLimit() + " Cards in deck");
		cardsAvailableListPanel.updateTitleLabel("/" + profile.getCollectedCards().size() + " available Cards");
	}
	
	/**
	 * paints the background with the image
	 */
	protected void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		g1.drawImage(new ImageIcon("images//feltBackground.png").getImage(), 0, 0, null);
	}
}
