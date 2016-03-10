package cardGame_v1.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cardGame_v1.Controller.Menu;

public class MenuPanel extends JPanel {
	private static final long serialVersionUID = 1L;

	private static final Dimension BUTTON_SIZE = new Dimension(200, 25);
	
	// Buttons
	private JButton playButton, editButton, storeButton, 
	        saveButton, changeProfileButton, quitButton;

	/**
	 * Constructor for the menuPanel
	 * @param menu
	 */
	public MenuPanel(Menu menu) {
		//set up panel options
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS)); 

		//create name label
		JLabel welcomeLabel = new JLabel("What would you like to do " + menu.getActiveProfile().getName() + "?");
		welcomeLabel.setForeground(Color.WHITE);
		welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);

		//create stat panel/labels
		JLabel statLabel = new JLabel("Stats for " + menu.getActiveProfile().getName());
		statLabel.setForeground(Color.WHITE);
		statLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		StatPanel statPanel = new StatPanel(menu.getActiveProfile());
		statPanel.setOpaque(false);

		//create buttons
		playButton = new JButton("Play Game");
		playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		playButton.setHorizontalAlignment(SwingConstants.CENTER);
		playButton.setMinimumSize(BUTTON_SIZE);
		playButton.setMaximumSize(BUTTON_SIZE);

		editButton = new JButton("Edit Deck");
		editButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		editButton.setHorizontalAlignment(SwingConstants.CENTER);
		editButton.setMinimumSize(BUTTON_SIZE);
		editButton.setMaximumSize(BUTTON_SIZE);

		storeButton = new JButton("Store");
		storeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		storeButton.setHorizontalAlignment(SwingConstants.CENTER);
		storeButton.setMinimumSize(BUTTON_SIZE);
		storeButton.setMaximumSize(BUTTON_SIZE);

		saveButton = new JButton("Save Profile");
		saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		saveButton.setHorizontalAlignment(SwingConstants.CENTER);
		saveButton.setMinimumSize(BUTTON_SIZE);
		saveButton.setMaximumSize(BUTTON_SIZE);

		changeProfileButton = new JButton("Change Profile");
		changeProfileButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		changeProfileButton.setHorizontalAlignment(SwingConstants.CENTER);
		changeProfileButton.setMinimumSize(BUTTON_SIZE);
		changeProfileButton.setMaximumSize(BUTTON_SIZE);

		quitButton = new JButton("Quit");
		quitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		quitButton.setHorizontalAlignment(SwingConstants.CENTER);
		quitButton.setMinimumSize(BUTTON_SIZE);
		quitButton.setMaximumSize(BUTTON_SIZE);

		//Add buttons and glue
		add(Box.createVerticalGlue());
		add(Box.createHorizontalGlue());
		add(welcomeLabel);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(playButton);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(editButton);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(storeButton);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(saveButton);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(changeProfileButton);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(quitButton);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(Box.createVerticalGlue());
		add(statLabel);
		add(Box.createRigidArea(new Dimension(0, 2)));
		add(statPanel);
		add(Box.createHorizontalGlue());
	}
	
	/**
	 * overridden paintComponent method that takes the graphics object of the panel.
	 * draws the background image on the panel.
	 */
	protected void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		g1.drawImage(new ImageIcon("images//menuBackground.png").getImage(), 0, 0, null);
	}

	/**
	 * @return the changeProfileButton
	 */
	public JButton getChangeProfileButton() {
		return changeProfileButton;
	}

	/**
	 * @return the playButton
	 */
	public JButton getPlayButton() {
		return playButton;
	}

	/**
	 * @return the editButton
	 */
	public JButton getEditButton() {
		return editButton;
	}

	/**
	 * @return the storeButton
	 */
	public JButton getStoreButton() {
		return storeButton;
	}

	/**
	 * @return the saveButton
	 */
	public JButton getSaveButton() {
		return saveButton;
	}

	/**
	 * @return the quitButton
	 */
	public JButton getQuitButton() {
		return quitButton;
	}
}