package cardGame_v1.GUI;

import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;

import cardGame_v1.Model.UserProfile;

import java.awt.Color;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JButton;

public class StartGamePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel challengerLabel, adversaryLabel;
	private JTextField oppenentTextField;
	private JButton enterButton, startGameButton, backButton;
	private StatPanel challengerStatPanel, adversaryStatPanel;
	private JPanel adversaryPanel;
	
	/**
	 * Constructor for StartGamePanel
	 * @param activeProfile UserProfile that will be displayed
	 */
	public StartGamePanel(UserProfile activeProfile) {
		setLayout(new GridLayout(2, 1, 0, 0));
		
		// Initialize Player Information Panels
		JPanel playersContainerPanel = new JPanel();
		playersContainerPanel.setOpaque(false);
		add(playersContainerPanel);
		playersContainerPanel.setLayout(new BoxLayout(playersContainerPanel, BoxLayout.X_AXIS));
		playersContainerPanel.add(Box.createHorizontalGlue());
		
		// Initialize First Player Panel
		JPanel challengerPanel = new JPanel();
		challengerPanel.setOpaque(false);
		playersContainerPanel.add(challengerPanel);
		challengerPanel.setLayout(new BoxLayout(challengerPanel, BoxLayout.Y_AXIS));
		challengerPanel.add(Box.createVerticalGlue());
		challengerLabel = new JLabel("Challenger: " + activeProfile.getName());
		challengerLabel.setForeground(Color.WHITE);
		challengerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		challengerPanel.add(challengerLabel);
		challengerStatPanel = new StatPanel(activeProfile);
		challengerPanel.add(challengerStatPanel);

		playersContainerPanel.add(Box.createHorizontalGlue());
		
		// Initialize Second Player Panel
		adversaryPanel = new JPanel();
		adversaryPanel.setOpaque(false);
		playersContainerPanel.add(adversaryPanel);
		adversaryPanel.setLayout(new BoxLayout(adversaryPanel, BoxLayout.Y_AXIS));
		adversaryPanel.add(Box.createVerticalGlue());
		adversaryLabel = new JLabel("Adversary: ");
		adversaryLabel.setForeground(Color.WHITE);
		adversaryPanel.add(adversaryLabel);
		adversaryLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		adversaryStatPanel = new StatPanel(new UserProfile(""));
		adversaryPanel.add(adversaryStatPanel);

		playersContainerPanel.add(Box.createHorizontalGlue());
		
		// Initialize Player Enter Panel
		JPanel enterPlayer = new JPanel();
		enterPlayer.setOpaque(false);
		add(enterPlayer);
		enterPlayer.setLayout(new BoxLayout(enterPlayer, BoxLayout.Y_AXIS));
		enterPlayer.add(Box.createVerticalGlue());
		JLabel textTopLabel = new JLabel("Enter a player to challenge:");
		textTopLabel.setForeground(Color.WHITE);
		enterPlayer.add(textTopLabel);
		textTopLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		textTopLabel.setHorizontalTextPosition(SwingConstants.CENTER);
		textTopLabel.setHorizontalAlignment(SwingConstants.CENTER);
		enterPlayer.add(Box.createRigidArea(new Dimension(5, 5)));
		oppenentTextField = new JTextField();
		enterPlayer.add(oppenentTextField);
		oppenentTextField.setMaximumSize(new Dimension(350, 500));
		oppenentTextField.setColumns(10);
		enterPlayer.add(Box.createRigidArea(new Dimension(8, 8)));
		enterButton = new JButton("Enter");
		enterPlayer.add(enterButton);
		enterButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterPlayer.add(Box.createVerticalGlue());
		startGameButton = new JButton("Start Game");
		startGameButton.setEnabled(false);
		startGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterPlayer.add(startGameButton);
		enterPlayer.add(Box.createVerticalGlue());
		backButton = new JButton("Back");
		backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		enterPlayer.add(backButton);
		enterPlayer.add(Box.createVerticalGlue());
	}

	/**
	 * @return opponentTextField
	 */
	public JTextField getOppenentTextField() {
		return oppenentTextField;
	}

	/**
	 * @return enterButton
	 */
	public JButton getEnterButton() {
		return enterButton;
	}

	/**
	 * @return startGameButton
	 */
	public JButton getStartGameButton() {
		return startGameButton;
	}

	/**
	 * @return backButton
	 */
	public JButton getBackButton() {
		return backButton;
	}

	/**
	 * @return adversaryLabel
	 */
	public JLabel getAdversaryLabel() {
		return adversaryLabel;
	}

	/**
	 * @param adversaryStatPanel Panel containing information about the opposing player
	 */
	public void createAdversaryPanel(StatPanel adversaryStatPanel) {
		adversaryPanel.remove(this.adversaryStatPanel);
		this.adversaryStatPanel = adversaryStatPanel;
		adversaryPanel.add(adversaryStatPanel);
	}
	
	/**
	 * Draw the background image and regular component features
	 */
	@Override
	protected void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		g1.drawImage(new ImageIcon("images//menuBackground.png").getImage(), 0, 0, null);
	}
}
