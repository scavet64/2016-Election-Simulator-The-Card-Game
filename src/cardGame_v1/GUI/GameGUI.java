package cardGame_v1.GUI;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.function.Predicate;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import cardGame_v1.AI.AI;
import cardGame_v1.Controller.Game;
import cardGame_v1.Controller.Player;
import cardGame_v1.Model.Card;
import cardGame_v1.Model.Creature;
import cardGame_v1.Model.Enhancement;
import cardGame_v1.Model.UserProfile;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Box;

public class GameGUI extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Game game;
	private MainGUI menu;
	
	// ActionListeners
	private ActionListener cardButtonListener;
	private ActionListener deckButtonListener;
	
	// South Side Buttons
	private ArrayList<CardPanel> southFieldCards = new ArrayList<CardPanel>();
	private ArrayList<CardPanel> southHandCards = new ArrayList<CardPanel>();
	private PlayerPanel southPlayer;
	private JButton southDeckButton = new JButton();

	// North Side Buttons
	private ArrayList<CardPanel> northFieldCards = new ArrayList<CardPanel>();
	private ArrayList<CardPanel> northHandCards = new ArrayList<CardPanel>();
	private PlayerPanel northPlayer;
	private JButton northDeckButton = new JButton();

	// For the listeners
	private static final String[] NOT_SELECTED = {"", "", ""};
	private String[] label_position_playerSideSelectionOne = NOT_SELECTED;
	private String[] label_position_playerSideSelectionTwo = NOT_SELECTED;
	
	// Log Area
	private JTextArea actionLog = new JTextArea(null, 10, 1);
	
	// Lambda Expressions
	private Predicate<Card> ifCard = c -> c != null;
	private Predicate<Card> ifEmpty = c -> c == null;
	
	// Final Sizes
	private final int POSITION_WIDTH = 177;
	private final int POSITION_HEIGHT = 250;
	private final Dimension POSITION_DIMENSION = new Dimension(POSITION_WIDTH, POSITION_HEIGHT);
	
	private final int FIELD_POSITION_WIDTH = 220;
	private final int FIELD_POSITION_HEIGHT = 220;
	private final Dimension FIELD_POSITION_DIMENSION = new Dimension(FIELD_POSITION_WIDTH, FIELD_POSITION_HEIGHT);
	
	private final int AREA_WIDTH = 7;
	private final int AREA_HEIGHT = 6;
	private final Dimension AREA_DIMENSION = new Dimension(AREA_WIDTH, AREA_HEIGHT);
	
	private final static double IMAGE_REDUCTION_SIZE = 0.535;
	private static final ImageIcon CARDBACK = Card.getReducedSizeImageIcon(IMAGE_REDUCTION_SIZE, new ImageIcon("CardImages//CardBack.png"));
	
	// Other Finals
	private final int PLAYER_ONE = 1;
	private final int PLAYER_TWO = 2;
	private final boolean CARDS_VISIBLE = true;
	private final boolean HIDE_CARDS = false;
	private final int FIELD_POSITION = 1;
	private final int HAND_POSITION = 2;
	
	/**
	 * Main constructor for GameGUI
	 * @param game Game to display
	 * @param mainGUI To go back to the MainGUI
	 */
	public GameGUI(Game game, MainGUI mainGUI) {
		this.game = game;
		this.menu = mainGUI;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		InputMap inMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		Action quitGame = new AbstractAction(){
			private static final long serialVersionUID = 1L;

			/**
			 * Quit the game and return to MainGUI
			 */
			public void actionPerformed(ActionEvent e){
				int choice = JOptionPane.showConfirmDialog(null, "Would you like to quit the game?", "Quit?", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION){
					getGame().quitGame();
					returnToMenu();
				}
			}
		};
		
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "quit");
		getActionMap().put("quit", quitGame);
		
		// Initialize ActionListeners
		cardButtonListener = new ActionListener() {
			JButton lastButtonPressed;

			/**
			 * Apply selections, alter button enablization depending on selection.
			 * When two selections, call applyAction
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				JButton buttonPressed = (JButton) e.getSource();
				if(buttonPressed.equals(lastButtonPressed)) {
					lastButtonPressed = null;
					startMove();
					return;
				}
				String[] label_position_side = (buttonPressed.getName().split(" "));
				String label = label_position_side[0];
				int position = Integer.parseInt(label_position_side[1]);
				disableAllPositions();
				if(label_position_playerSideSelectionOne == NOT_SELECTED) {
					switch(label){
					case "hand":
						if(getGame().getCurrentPlayer().getHandOfCards().get(position) instanceof Creature) {
							if(getGame().getCurrentPlayerTurn() == PLAYER_ONE) {
								enableButtons(1, southFieldCards, ifEmpty);
							}else {
								enableButtons(2, northFieldCards, ifEmpty);
							}
						}else if(getGame().getCurrentPlayer().getHandOfCards().get(position) instanceof Enhancement) {
							enableButtons(1, southFieldCards, ifCard);
							enableButtons(2, northFieldCards, ifCard);
						}
						break;
					case "field":
						if(getGame().getCurrentPlayerTurn() == PLAYER_ONE) {
							if(!enableButtons(2, northFieldCards, ifCard)) {
								northPlayer.setActive(true);
							}
						}else {
							if(!enableButtons(1, southFieldCards, ifCard)) {
								southPlayer.setActive(true);
							}
						}
						break;
					}
					buttonPressed.setEnabled(true);
					lastButtonPressed = buttonPressed;
					label_position_playerSideSelectionOne = label_position_side;
				}else if(label_position_playerSideSelectionTwo == NOT_SELECTED) {
					label_position_playerSideSelectionTwo = label_position_side;
					lastButtonPressed = null;
					applyAction();
				}
			}
		};
		deckButtonListener = new ActionListener() {
			/**
			 * End Player's turn, flip hand Cards to back, update hand positions when
			 * prompted
			 */
			@Override
			public void actionPerformed(ActionEvent e) {
				endTurn();
			}
		};

		// Initialize North Side
		JPanel northSidePanel = new JPanel();
		northSidePanel.setOpaque(false);
		add(northSidePanel);
		northSidePanel.setLayout(new BoxLayout(northSidePanel, BoxLayout.X_AXIS));
		northSidePanel.add(Box.createHorizontalGlue());
		northDeckButton.setPreferredSize(POSITION_DIMENSION);
		northDeckButton.setIcon(CARDBACK);
		northDeckButton.addActionListener(deckButtonListener);
		northDeckButton.setToolTipText(game.getPlayerTwo().getProfile().getDeck().getCardsLeftString());
		northSidePanel.add(northDeckButton);
		northSidePanel.add(Box.createHorizontalGlue());
			// Initialize North Hand
			initializeCardPanelList(HAND_POSITION, "north", northHandCards);
			northHandCards.forEach(CardPanel -> {
				northSidePanel.add(CardPanel);
				northSidePanel.add(Box.createRigidArea(AREA_DIMENSION));
				});
		northSidePanel.add(Box.createHorizontalGlue());
		northPlayer = new PlayerPanel();
		northPlayer.getButton().addActionListener(cardButtonListener);
		northPlayer.getButton().setName("player 0 north");
		northPlayer.setMinimumSize(POSITION_DIMENSION);
		northPlayer.setMaximumSize(POSITION_DIMENSION);
		northPlayer.setPreferredSize(POSITION_DIMENSION);
		northSidePanel.add(northPlayer);
		northSidePanel.add(Box.createHorizontalGlue());
		
		add(Box.createRigidArea(AREA_DIMENSION));
		
		// Initialize Center Panel
		JPanel centerPanel = new JPanel();
		centerPanel.setOpaque(false);
		add(centerPanel);
		centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
		
		// Initialize Field
		JPanel fieldPanel = new JPanel();
		fieldPanel.setOpaque(false);
		centerPanel.add(fieldPanel);
		fieldPanel.setAlignmentX(CENTER_ALIGNMENT);
		fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
		
			// Initialize North Field
			JPanel northFieldPanel = new JPanel();
			northFieldPanel.setOpaque(false);
			fieldPanel.add(northFieldPanel);
			northFieldPanel.setLayout(new BoxLayout(northFieldPanel, BoxLayout.X_AXIS));
			northFieldPanel.add(Box.createHorizontalGlue());
			initializeCardPanelList(FIELD_POSITION, "north", northFieldCards);
			northFieldCards.forEach(CardPanel -> {
				northFieldPanel.add(CardPanel);
				northFieldPanel.add(Box.createRigidArea(AREA_DIMENSION));
				});
			northFieldPanel.add(Box.createHorizontalGlue());
	
			fieldPanel.add(Box.createVerticalGlue());
			
			// Initialize South Field
			JPanel southFieldPanel = new JPanel();
			southFieldPanel.setOpaque(false);
			fieldPanel.add(southFieldPanel);
			southFieldPanel.setLayout(new BoxLayout(southFieldPanel, BoxLayout.X_AXIS));
			southFieldPanel.add(Box.createHorizontalGlue());
			initializeCardPanelList(FIELD_POSITION, "south", southFieldCards);
			southFieldCards.forEach(CardPanel -> {
				southFieldPanel.add(CardPanel);
				southFieldPanel.add(Box.createRigidArea(AREA_DIMENSION));
				});
			southFieldPanel.add(Box.createHorizontalGlue());
		
		// Initialize Action Log
		JPanel actionLogPanel = new JPanel(new GridLayout());
		actionLogPanel.setOpaque(false);
		centerPanel.add(actionLogPanel);
		actionLogPanel.setAlignmentX(RIGHT_ALIGNMENT);
		actionLog.setEditable(false);
		Font font = new Font(null, Font.PLAIN, 17);
		actionLog.setFont(font);
		JScrollPane scrollPane = new JScrollPane(actionLog);
		actionLogPanel.add(scrollPane);
		centerPanel.add(Box.createRigidArea(new Dimension(30,0)));
		
		add(Box.createRigidArea(AREA_DIMENSION));
		
		// Initialize South Side
		JPanel southSidePanel = new JPanel();
		southSidePanel.setOpaque(false);
		add(southSidePanel);
		southSidePanel.setLayout(new BoxLayout(southSidePanel, BoxLayout.X_AXIS));
		southSidePanel.add(Box.createHorizontalGlue());
		southDeckButton.setPreferredSize(POSITION_DIMENSION);
		southDeckButton.setIcon(CARDBACK);
		southDeckButton.addActionListener(deckButtonListener);
		southDeckButton.setToolTipText(game.getPlayerOne().getProfile().getDeck().getCardsLeftString());
		southSidePanel.add(southDeckButton);
		southSidePanel.add(Box.createHorizontalGlue());
			// Initialize South Hand
			initializeCardPanelList(HAND_POSITION, "south", southHandCards);
			southHandCards.forEach(CardPanel -> {
				southSidePanel.add(CardPanel);
				southSidePanel.add(Box.createRigidArea(AREA_DIMENSION));
				});
		southSidePanel.add(Box.createHorizontalGlue());
		southPlayer = new PlayerPanel();
		southPlayer.getButton().addActionListener(cardButtonListener);
		southPlayer.getButton().setName("player 0 south");
		southPlayer.setMinimumSize(POSITION_DIMENSION);
		southPlayer.setMaximumSize(POSITION_DIMENSION);
		southPlayer.setPreferredSize(POSITION_DIMENSION);
		southSidePanel.add(southPlayer);
		southSidePanel.add(Box.createHorizontalGlue());
		
		startMove();
	}

	/**
	 * Create specified CardPanels and add to a given list
	 * @param cardType Type of CardPanel to create
	 * @param side Player's side the CardPanel will be on
	 * @param list List to add the CardPanels to
	 */
	public void initializeCardPanelList(int cardType, String side, ArrayList<CardPanel> list) {
		if(cardType == FIELD_POSITION) {
			int amount = Player.getMAX_FIELD_SIZE();
			String name = "field ";
			for(int i = 0; i < amount; i++) {
				FieldPanel card = new FieldPanel();
				card.getButton().setName(name + i + " " + side);
				card.getButton().addActionListener(cardButtonListener);
				card.setMinimumSize(FIELD_POSITION_DIMENSION);
				card.setMaximumSize(FIELD_POSITION_DIMENSION);
				card.setPreferredSize(FIELD_POSITION_DIMENSION);
				list.add(card);
			}
		}else if(cardType == HAND_POSITION) {
			int amount =Player.getMAX_HAND_SIZE();
			String name = "hand ";
			for(int i = 0; i < amount; i++) {
				CardImagePanel card = new CardImagePanel(CardImagePanel.HAND_MODE);
				card.getButton().setName(name + i + " " + side);
				card.getButton().addActionListener(cardButtonListener);
				card.setMinimumSize(POSITION_DIMENSION);
				card.setMaximumSize(POSITION_DIMENSION);
				card.setPreferredSize(POSITION_DIMENSION);
				list.add(card);
			}
		}
	}

	/**
	 * Update position selections to not selected, update all position images,
	 * and enable button selections for the current Player's turn
	 */
	private void startMove() {
		label_position_playerSideSelectionOne = NOT_SELECTED;
		label_position_playerSideSelectionTwo = NOT_SELECTED;
		updatePositions();
		disableAllPositions();
		if(!(game.getCurrentPlayer() instanceof AI)) {
			if(game.getCurrentPlayerTurn() == PLAYER_ONE) {
				enableButtons(PLAYER_ONE, southHandCards, ifCard);
				enableButtons(PLAYER_ONE, southFieldCards, ifCard);
				southDeckButton.setEnabled(true);
			}else{
				enableButtons(PLAYER_TWO, northHandCards, ifCard);
				enableButtons(PLAYER_TWO, northFieldCards, ifCard);
				northDeckButton.setEnabled(true);
			}
		}else {
			game = ((AI) game.getCurrentPlayer()).playTurn(game, this);
			endTurn();
		}
	}
	
	//TODO like startMove
	private void endTurn() {
		addToActionLog(game.endTurn());
//		if(game.getCurrentPlayerTurn() == PLAYER_TWO) {
//			if(game.getPlayerOne() instanceof AI) updateHandPositions(PLAYER_ONE, southHandCards, HIDE_CARDS);
//			else updateHandPositions(PLAYER_ONE, southHandCards, CARDS_VISIBLE);
//			southDeckButton.setToolTipText(game.getPlayerOne().getProfile().getDeck().getCardsLeftString());
//		}else {
//			if(game.getPlayerTwo() instanceof AI) updateHandPositions(PLAYER_TWO, northHandCards, HIDE_CARDS);
//			else updateHandPositions(PLAYER_TWO, northHandCards, CARDS_VISIBLE);
//			northDeckButton.setToolTipText(game.getPlayerTwo().getProfile().getDeck().getCardsLeftString());
//		}
		updateHandPositions(PLAYER_ONE, southHandCards, HIDE_CARDS);
		updateHandPositions(PLAYER_TWO, northHandCards, HIDE_CARDS);
		southDeckButton.setToolTipText(game.getPlayerOne().getProfile().getDeck().getCardsLeftString());
		northDeckButton.setToolTipText(game.getPlayerTwo().getProfile().getDeck().getCardsLeftString());
		updateUI();
		if(!Game.isAI) {
			JOptionPane.showMessageDialog(null, "Swap seats and press okay!", "Swap Player", JOptionPane.INFORMATION_MESSAGE);
		}
		startMove();
	}

	/**
	 * Update positions to show updated images, hide the opposing Player's hand Cards
	 */
	private void updatePositions() {
		if(game.getCurrentPlayerTurn() == PLAYER_ONE) {
			if(game.getPlayerOne() instanceof AI) updateHandPositions(PLAYER_ONE, southHandCards, HIDE_CARDS);
			else updateHandPositions(PLAYER_ONE, southHandCards, CARDS_VISIBLE);
			updateHandPositions(PLAYER_TWO, northHandCards, HIDE_CARDS);
		}else {
			if(game.getPlayerTwo() instanceof AI) updateHandPositions(PLAYER_TWO, northHandCards, HIDE_CARDS);
			else updateHandPositions(PLAYER_TWO, northHandCards, CARDS_VISIBLE);
			updateHandPositions(PLAYER_ONE, southHandCards, HIDE_CARDS);
		}
		updateFieldPositions(PLAYER_ONE, southFieldCards);
		updateFieldPositions(PLAYER_TWO, northFieldCards);
		southPlayer.updateDisplay(game.getPlayerOne());
		northPlayer.updateDisplay(game.getPlayerTwo());
	}

	/**
	 * Update the hand positions' display
	 * @param player Player's side to update
	 * @param list List of CardPanels to update
	 * @param isVisible Visibility of card. If true, show Card, else hide Card
	 */
	public void updateHandPositions(int player, ArrayList<CardPanel> list, boolean isVisible) {
		for(int i = 0; i < Player.getMAX_HAND_SIZE(); i++) {
			Card card = game.getCardInHandPostition(player, i);
			list.get(i).updateDisplay(card, isVisible);
		}
	}

	/**
	 * Update the field positions' display
	 * @param player Player's side to update
	 * @param list List of CardPanels to update
	 */
	public void updateFieldPositions(int player, ArrayList<CardPanel> list) {
		for(int i = 0; i < Player.getMAX_FIELD_SIZE(); i++) {
			Creature card = game.getCreatureAtPosition(player, i);
			list.get(i).updateDisplay(card, CARDS_VISIBLE);
			this.updateUI();
		}
	}

	/**
	 * Disable every position's button
	 */
	private void disableAllPositions() {
		southHandCards.forEach(CardPanel -> CardPanel.setActive(false));
		southFieldCards.forEach(CardPanel -> CardPanel.setActive(false));
		southDeckButton.setEnabled(false);
		southPlayer.setActive(false);
		northHandCards.forEach(CardPanel -> CardPanel.setActive(false));
		northFieldCards.forEach(CardPanel -> CardPanel.setActive(false));
		northDeckButton.setEnabled(false);
		northPlayer.setActive(false);
	}

	/**
	 * Enable the specified buttons if the given test is passed
	 * @param playerSide Player's side to enable
	 * @param list List of CardPanels' buttons to enable
	 * @param test Lambda test to determine if button will be enabled
	 * @return true if a button was enabled, false if not
	 */
	private boolean enableButtons(int playerSide, ArrayList<CardPanel> list, Predicate<Card> test) {
		boolean enabledButton = false;
		for(CardPanel CardPanel : list) {
			String[] position = CardPanel.getButton().getName().split(" ");
			switch(position[0]) {
			case "field":
				if(test.test(game.getCreatureAtPosition(playerSide, Integer.parseInt(position[1])))) {
					CardPanel.setActive(true);
					enabledButton = true;
				}
				break;
			case "hand":
				Card cardToTest;
				try {
					cardToTest = game.getCurrentPlayer().getHandOfCards().get(Integer.parseInt(position[1]));
				}catch(Exception e) {
					cardToTest = null;
				}
				if(test.test(cardToTest)) {
					CardPanel.setActive(true);
					enabledButton = true;
				}
				break;
			}
		}
		return enabledButton;
	}

	/**
	 * Use the two selections to apply an action in Game,
	 * after an action, determine if the game has been won.
	 * Update the action log and start the next move.
	 */
	public void applyAction() {
		String message = game.applyAction(label_position_playerSideSelectionOne, label_position_playerSideSelectionTwo).getMessageString();
		if(game.isGameOver()) {
			displayWin(game.getCurrentPlayer().getProfile(), game.getOpposingPlayer().getProfile());
		}
		addToActionLog(message);
		startMove();
	}

	/**
	 * @return Image reduction size
	 */
	public static double getImageReductionSize() {
		return IMAGE_REDUCTION_SIZE;
	}

	/**
	 * Display a JOptionPane when the game is won, save profiles, and return to main menu.
	 * @param winningPlayer The profile that won
	 * @param losingPlayer The profile that lost
	 */
	public void displayWin(UserProfile winningPlayer, UserProfile losingPlayer) {
		menu.getMenu().saveProfile(winningPlayer);
		menu.getMenu().saveProfile(losingPlayer);
		JOptionPane.showMessageDialog(null, winningPlayer.getName() + " defeated " + losingPlayer.getName() + "\nCongratulations!1!!!Woo!!1!", "GAME OVER", JOptionPane.INFORMATION_MESSAGE);
		returnToMenu();
	}
	
	/**
	 * Return the main center panel to the main menu.
	 */
	private void returnToMenu() {
		menu.getMainPanel().remove(menu.getActiveCenterPanel());
		menu.initializeMenu();
	}
	
	/**
	 * Draw the game board to the background
	 */
	protected void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		g1.drawImage(new ImageIcon("images//gameBackground.png").getImage(), 0, 0, null);
	}

	public Game getGame() {
		return game;
	}
	
	public void addToActionLog(String message) {
		actionLog.setText(actionLog.getText() + message);
	}
}