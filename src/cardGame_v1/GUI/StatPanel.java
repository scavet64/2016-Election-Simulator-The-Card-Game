package cardGame_v1.GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cardGame_v1.Model.UserProfile;

public class StatPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private final String background = "images\\blackTransparent.png";

	/**
	 * Constructor for StatPanel
	 * @param profile UserProfile that will have their information displayed
	 */
	public StatPanel(UserProfile profile) {	
		setBorder(BorderFactory.createLineBorder(Color.WHITE));
		setLayout(new GridLayout(4, 1));
		setOpaque(false);

		JLabel winLabel = new JLabel("Wins: " + profile.getWins());
		winLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		winLabel.setHorizontalAlignment(SwingConstants.CENTER);
		winLabel.setForeground(Color.WHITE);
		
		JLabel loseLabel = new JLabel("Losses: " + profile.getLosses());
		loseLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		loseLabel.setHorizontalAlignment(SwingConstants.CENTER);
		loseLabel.setForeground(Color.WHITE);
		
		JLabel creditLabel = new JLabel("Credits " + profile.getCredits());
		creditLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		creditLabel.setHorizontalAlignment(SwingConstants.CENTER);
		creditLabel.setForeground(Color.WHITE);
		
		JLabel cardLabel = new JLabel("Number of Cards Collected: " + profile.getCollectedCards().size());
		cardLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		cardLabel.setHorizontalAlignment(SwingConstants.CENTER);
		cardLabel.setForeground(Color.WHITE);

		add(winLabel);
		add(loseLabel);
		add(creditLabel);
		add(cardLabel);
	}
	
	/**
	 * adds the translucent background to the panel
	 */
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		try {
			g.drawImage(ImageIO.read(new File(background)), 0, 0, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
