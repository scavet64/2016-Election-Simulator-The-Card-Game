package cardGame_v1.GUI;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;
import cardGame_v1.Controller.Game;
import cardGame_v1.Controller.Menu;
import cardGame_v1.ExceptionHandling.ProfileNotFoundException;
import cardGame_v1.Model.Deck;
import cardGame_v1.Model.UserProfile;

public class MainGUI  extends JFrame{
	private static final long serialVersionUID = 1L;
	//sizes for this frame
	private static final int PREFERRED_WIDTH = 500;
	private static final int PREFERRED_HIGHT = 500;
	private static final Dimension PREFERRED_SIZE = new Dimension (PREFERRED_WIDTH,PREFERRED_HIGHT);
	private static final int MIN_WIDTH = 350;
	private static final int MIN_HIGHT = 350;
	private static final Dimension MIN_SIZE = new Dimension(MIN_WIDTH, MIN_HIGHT);
	private static final int MAX_WIDTH = 510;
	private static final int MAX_HIGHT = 510;
	private static final Dimension MAX_SIZE = new Dimension(MAX_WIDTH,MAX_HIGHT);
	private Menu menu;
	private JPanel mainPanel = new JPanel();
	private JLabel topLabel = new JLabel();
	private JLabel errorLabel = new JLabel();
	private JPanel activeCenterPanel;
	private JButton finishButton = new JButton();
	private static Font font;

	/**
	 * Constructor for MenuGUI
	 */
	public MainGUI(Menu menu) {
		this.menu = menu;
		setResizable(false);
		
		try {
			this.setIconImage(ImageIO.read(new File("Images\\icon.png")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//create inputMap for main Panel
		InputMap inMap = mainPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		
		//create actions for input map
		Action showHelp = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				showHelpMessage();
			}
		};
		Action showTypeEffects = new AbstractAction(){
			private static final long serialVersionUID = 1L;
			@Override
			public void actionPerformed(ActionEvent e) {
				showTypeEffects();
			}
		};
		
		ActionListener finishListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				mainPanel.remove(activeCenterPanel);
				initializeMenu();
			}
		};
		
		//place input keys into map
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0), "help");
		mainPanel.getActionMap().put("help", showHelp);
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_F2, 0), "typeEffects");
		mainPanel.getActionMap().put("typeEffects", showTypeEffects);
		
		//create and set the font
		try {
			font = Font.createFont(Font.TRUETYPE_FONT, new File("OPTIBelwe-Medium.ttf"));
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}

		//sets up the top label's properties
		topLabel.setForeground(Color.WHITE);
		topLabel.setSize(new Dimension(100,100));
		//		topLabel.setFont(topLabel.getFont().deriveFont(20.0f));
		topLabel.setFont(font.deriveFont(20.0f));
		topLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//sets location of frame
		this.setLocation(650, 250);

		//set sizes, Color, text, and default close option
		mainPanel.setLayout(new BorderLayout());
		setSize(PREFERRED_SIZE);
		setMinimumSize(MIN_SIZE);
		setMaximumSize(MAX_SIZE);
		mainPanel.setBackground(Color.black);
		setTitle("Election Simulator 2016 Beta 0.9");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//adds the main panel to the frame and initializes the login
		add(mainPanel);
		initializeLogin();

		mainPanel.add(topLabel, BorderLayout.NORTH);
		finishButton.addActionListener(finishListener);
		this.setVisible(true);
	}

	/**
	 * Start the login GUI
	 */
	private void initializeLogin(){
		//Welcome label for the top of the mainPanel
		topLabel.setText("Welcome to our super serious card game!");

		//The login panel, Goes into the center of the mainPanel
		LoginPanel loginPanel = new LoginPanel();
		mainPanel.add(loginPanel, BorderLayout.CENTER);

		//label down at the bottom, shows if there is an error
		errorLabel = new JLabel(" ");
		errorLabel.setForeground(Color.red);
		errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(errorLabel, BorderLayout.SOUTH);

		//ActionListener for our buttons and text field
		ActionListener myButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				if(!loginPanel.getProfileTextField().getText().equals("")) {
					try{
						if(event.getSource().getClass().getSimpleName().equals("JTextField") || ((JButton) event.getSource()).getText().equals("Login")){
							menu.setActiveProfile(menu.loadProfile(loginPanel.getProfileTextField().getText()));
						} else {
							//The Create Profile button was pressed
							menu.createProfile(loginPanel.getProfileTextField().getText());
						}
						//Prep for UI Change
						mainPanel.remove(loginPanel);
						initializeMenu();
						errorLabel.setText(" ");
					}catch(Exception exeption){
						//There was an error loading the profile or it does not exist.
						errorLabel.setText("That profile does not exist!");
					}
				} else {
					//Nothing was in the textfield
					//JOptionPane.showMessageDialog(null, "Input a profile in the textfield", "Error", JOptionPane.ERROR_MESSAGE);
					errorLabel.setText("Input a profile in the textfield");
				}	
			}
		};

		//link buttons and actionListener
		loginPanel.getLogin().addActionListener(myButtonListener);
		loginPanel.getRegister().addActionListener(myButtonListener);
		loginPanel.getProfileTextField().addActionListener(myButtonListener);
	}

	/**
	 * Start the main menu
	 */
	public void initializeMenu() {
		setLocation(650, 250);
		errorLabel.setText("");
		setSize(PREFERRED_SIZE);
		topLabel.setText("Main Menu");
		topLabel.setForeground(Color.WHITE);
		errorLabel.setText(" ");
		MenuPanel menuPanel = new MenuPanel(menu);
		ActionListener myButtonListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JButton button = (JButton) event.getSource();
				switch(button.getText()){

				case "Single Player":
					try{
						mainPanel.remove(menuPanel);
						initializePlayGUI(menu.loadProfile("aiPlayer"), Game.AI_GAME);
					
					}catch(ProfileNotFoundException | ClassNotFoundException e){
						errorLabel.setText("That profile does not exist!");
					}
					break;
					
					
				case "Multiplayer":
					if(!menu.getActiveProfile().getDeck().isFull()) {
						JOptionPane.showMessageDialog(null, "You cannot start a game without a full deck!", "Unfinished Deck", JOptionPane.WARNING_MESSAGE);
						mainPanel.updateUI();
						return;
					}
					mainPanel.remove(menuPanel);
					initializeStartGamePanel();
					break;

				case "Edit Deck":
					//edit deck GUI
					mainPanel.remove(menuPanel);
					initializeEditDeck();
					break;

				case "Store":
					//store GUI
					mainPanel.remove(menuPanel);
					initializeStoreGUI();
					break;

				case "Save Profile":
					menu.getActiveProfile().setFirstLoad(false);
					menu.saveActiveProfile();
					break;

				case "Change Profile":
					mainPanel.remove(menuPanel);
					initializeLogin();
					break;

				case "Quit":
					System.exit(0);
					break;
				}

			}
		};
		// Add listeners to buttons
		menuPanel.getPlayButton().addActionListener(myButtonListener);
		menuPanel.getEditButton().addActionListener(myButtonListener);
		menuPanel.getStoreButton().addActionListener(myButtonListener);
		menuPanel.getSaveButton().addActionListener(myButtonListener);
		menuPanel.getChangeProfileButton().addActionListener(myButtonListener);
		menuPanel.getQuitButton().addActionListener(myButtonListener);
		menuPanel.getPlayAI().addActionListener(myButtonListener);
		mainPanel.add(menuPanel, BorderLayout.CENTER);			
		mainPanel.updateUI();
		if(menu.getActiveProfile().isFirstLoad()){
			showHelpMessage();
			menu.getActiveProfile().setFirstLoad(false);
		}
	}

	/**
	 * Start the edit deck gui
	 */
	private void initializeEditDeck(){
		topLabel.setText("Edit Deck Menu");
		setLocation(450, 200);
		setSize(new Dimension(850,750));

		//Adds the gui to the center
		finishButton.setText("Finish Editing");
		DeckEditorGUI deckEditorGUI = new DeckEditorGUI(menu.getActiveProfile(), finishButton); 
		mainPanel.add(deckEditorGUI, BorderLayout.CENTER);
		this.activeCenterPanel = deckEditorGUI;

		mainPanel.updateUI();

	}

	/**
	 * Start the store gui
	 */
	private void initializeStoreGUI(){
		topLabel.setText("Welcome to the store!");
		setLocation(500, 250);
		setSize(new Dimension(800,700));

		//adds the gui to the center
		finishButton.setText("Leave Store");
		StoreGUI storeGUI = new StoreGUI(menu.getActiveProfile(), finishButton);
		mainPanel.add(storeGUI, BorderLayout.CENTER);
		this.activeCenterPanel = storeGUI;

		mainPanel.updateUI();
	}

	/**
	 * Initialize the Start Game gui
	 */
	private void initializeStartGamePanel(){
		topLabel.setText("");
		StartGamePanel startGame = new StartGamePanel(menu.getActiveProfile());
		ActionListener startGameButtonListener = new ActionListener() {
			UserProfile oppsingPlayer;

			@Override
			public void actionPerformed(ActionEvent event) {
				if((event.getSource() instanceof JButton) && ((JButton) event.getSource()).getText().equals(startGame.getBackButton().getText())) {
					mainPanel.remove(startGame);
					initializeMenu();
				}
				else if(!startGame.getOppenentTextField().getText().equals("")){
					try {
						if(event.getSource() instanceof JTextField || ((JButton) event.getSource()).getText().equals(startGame.getEnterButton().getText())){
							oppsingPlayer = menu.loadProfile(startGame.getOppenentTextField().getText());
							startGame.getAdversaryLabel().setText("Adversary: " + oppsingPlayer.getName());
							startGame.createAdversaryPanel(new StatPanel(oppsingPlayer));
							if(oppsingPlayer.getDeck().getSize() == Deck.getLimit()){
								startGame.getStartGameButton().setEnabled(true);
							} else {
								JOptionPane.showMessageDialog(null, "The opposing player does not have a finished deck!", "Unfinished Deck", JOptionPane.WARNING_MESSAGE);
								mainPanel.updateUI();
							}
							//mainPanel.updateUI();
							errorLabel.setText(" ");
							return;
						}else if(((JButton) event.getSource()).getText().equals(startGame.getStartGameButton().getText())) {
							mainPanel.remove(startGame);
							initializePlayGUI(oppsingPlayer, Game.MULTIPLAYER_GAME);
						}
					}catch(ProfileNotFoundException | ClassNotFoundException e){
						errorLabel.setText("That profile does not exist!");
					}
				}else {
					errorLabel.setText("Input a profile in the textfield");

				}
				startGame.getStartGameButton().setEnabled(false);
				oppsingPlayer = null;
				return;
			}
		};

		startGame.getOppenentTextField().addActionListener(startGameButtonListener);
		startGame.getEnterButton().addActionListener(startGameButtonListener);
		startGame.getStartGameButton().addActionListener(startGameButtonListener);
		startGame.getBackButton().addActionListener(startGameButtonListener);
		mainPanel.add(startGame);
		mainPanel.updateUI();
	}

	/**
	 * initialize the Game gui
	 * @param opposingPlayer player to be active player's opponent
	 */
	private void initializePlayGUI(UserProfile opposingPlayer,  boolean isMultiplayer){
		topLabel.setText("");
		setLocation(0, 60);
		setSize(new Dimension(1910,1050));
//		setSize(new Dimension(1600,900));
		errorLabel.setText(" ");

		//Adds the gui to the center
		Game game = new Game(menu.getActiveProfile(), opposingPlayer, isMultiplayer);
		activeCenterPanel = new GameGUI(game, this);
		mainPanel.add(activeCenterPanel);
		mainPanel.updateUI();
	}
	
	/**
	 * Displayed a pop-up that gives a general description about
	 * the game and how to play
	 */
	private void showHelpMessage(){
		JOptionPane.showMessageDialog(null, "Welcome to Election Simulator 2016!"
				+ "\n\nIf this is your first time playing this game, here are is a wall of text with some possible help"
				+ "\nThe objective of this game is to collect cards and compete against your opponent. "
				+ "\nThe winner of the game decides who will become the next president of the United States."
				+ "\n\nIn order to play the game you must first collect some cards. Lucky for you, We provide you"
				+ "\nwith some starting credits. These credits can be used in the store to purchase packs. In"
				+ "\neach pack there are 5 cards. One of these cards is always #Rare. This is important as only"
				+ "\nrare cards can be enhanced. Rare cards are denoted by a gemstone in the center of the card."
				+ "\nOnce you have some cards, head into the deck editor to create your deck. "
				+ "\n\nEvery creature has a type. These types are important to know as they can affect how well a "
				+ "\ncreature attacks another. These type effects can be seen at any time by pressing F2."
				+ "\n\nIf you are unsure what you are doing in a particular menu, press F1 for some help.", "Thanks for Playing!", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Gives general information about how card types interact with 
	 * each other
	 */
	private void showTypeEffects(){
		String s = "                                TYPE EFFECTS"
				+ "\nFighter: +2 attack on Genius, -2 attack on Charasmatic"
				+ "\nPsycho: +2 attack on Spooky, -2 attack on Magic"
				+ "\nMagic: +2 attack on Psycho, -2 attack on Genius"
				+ "\nGenius: +2 attack on Magic, -2 attack on Fighter"
				+ "\nCharasmatic: +2 attack on Fighter, -2 attack on Spooky"
				+ "\nSpooky: +2 attack on Charasmatic, -2 attack on Psycho";
		JOptionPane.showMessageDialog(null, s, "Type Modifications", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * @return the font
	 */
	public static Font getGameFont() {
		return font;
	}

	/**
	 * @return mainPanel
	 */
	public JPanel getMainPanel() {
		return mainPanel;
	}

	/**
	 * @return gameGUI
	 */
	public JPanel getActiveCenterPanel() {
		return activeCenterPanel;
	}

	/**
	 * @return menu
	 */
	public Menu getMenu() {
		return menu;
	}
}