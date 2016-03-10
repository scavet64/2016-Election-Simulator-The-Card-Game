package cardGame_v1.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import cardGame_v1.Model.Card;
import cardGame_v1.Model.Pack;
import cardGame_v1.Model.UserProfile;

public class StoreGUI extends JPanel{
	private static final long serialVersionUID = 1L;
	private JButton finishButton;
	private JPanel storePanel;
	private JPanel cardpackPanel;
	private JPanel cardPacksImagePanel;
	private JPanel cardsInPackPanel;
	private final int ONE_PACK_PRICE = 5;
	private final int TWO_PACK_PRICE = 8;
	private final int FIVE_PACK_PRICE = 20;
	private final int TEN_PACK_PRICE = 35;
	private final Dimension BUTTON_SIZE = new Dimension(200, 25);
	private JLabel creditsLabel = new JLabel();
	private JLabel packsLabel = new JLabel();

	public StoreGUI(UserProfile profile, JButton finishButton) {
		this.finishButton = finishButton;
		setLayout(new BorderLayout());
		
		createWestSide(profile);
		createCardPackSide(profile);
		
	}
	
	/**
	 * @param profile UserProfile to have all cards owned loaded from
	 */
	private void createWestSide(UserProfile profile){
		JPanel westPanel = new JPanel();
		westPanel.setOpaque(false);
		westPanel.setLayout(new BorderLayout());
		
		storePanel = new JPanel();
		storePanel.setOpaque(false);
		storePanel.setLayout(new BoxLayout(storePanel, BoxLayout.Y_AXIS));

		
		//create labels
		JLabel informationLabel = new JLabel("Click to Purchase!");
		informationLabel.setForeground(Color.WHITE);
//		informationLabel.setHorizontalAlignment(SwingConstants.CENTER);
		informationLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		updateLabels(profile);
		
		//create store buttons
		JButton onePack = new JButton("One Pack: 5 Credits");
		onePack.setName("1");
		onePack.setMinimumSize(BUTTON_SIZE);
		onePack.setMaximumSize(BUTTON_SIZE);
		onePack.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton twoPacks = new JButton("Two Packs: 8 Credits");
		twoPacks.setName("2");
		twoPacks.setMinimumSize(BUTTON_SIZE);
		twoPacks.setMaximumSize(BUTTON_SIZE);
		twoPacks.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton fivePacks = new JButton("Five Packs: 20 Credits");
		fivePacks.setName("5");
		fivePacks.setMinimumSize(BUTTON_SIZE);
		fivePacks.setMaximumSize(BUTTON_SIZE);
		fivePacks.setAlignmentX(Component.CENTER_ALIGNMENT);
		JButton tenPacks = new JButton("Ten Packs: 35 Credits");
		tenPacks.setName("10");
		tenPacks.setMinimumSize(BUTTON_SIZE);
		tenPacks.setMaximumSize(BUTTON_SIZE);
		tenPacks.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//create action listener and add to buttons
		ActionListener buyPackListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				if(JOptionPane.showConfirmDialog(null, "Are you sure you want to purchase packs?", "Are You Sure?", JOptionPane.YES_NO_OPTION)
						== JOptionPane.YES_OPTION){
					int buttonName = Integer.parseInt(((JButton) event.getSource()).getName());
					switch(buttonName){
					case 1:
						profile.purchasePacks(1, ONE_PACK_PRICE);
						break;
					case 2:
						profile.purchasePacks(2, TWO_PACK_PRICE);
						break;
					case 5:
						profile.purchasePacks(5, FIVE_PACK_PRICE);
						break;
					case 10:
						profile.purchasePacks(10, TEN_PACK_PRICE);
						break;
					}
					updateLabels(profile);
				}
			}
		};
		onePack.addActionListener(buyPackListener);
		twoPacks.addActionListener(buyPackListener);
		fivePacks.addActionListener(buyPackListener);
		tenPacks.addActionListener(buyPackListener);
		
		//add to the store panel
		storePanel.add(Box.createVerticalGlue());
		storePanel.add(Box.createHorizontalGlue());
		storePanel.add(informationLabel);
		storePanel.add(Box.createRigidArea(new Dimension(0, 2)));
		storePanel.add(onePack);
		storePanel.add(Box.createRigidArea(new Dimension(0, 2)));
		storePanel.add(twoPacks);
		storePanel.add(Box.createRigidArea(new Dimension(0, 2)));
		storePanel.add(fivePacks);
		storePanel.add(Box.createRigidArea(new Dimension(0, 2)));
		storePanel.add(tenPacks);
		storePanel.add(Box.createVerticalGlue());
		storePanel.add(Box.createHorizontalGlue());
		
		//add to westPanel
		creditsLabel.setForeground(Color.WHITE);
		westPanel.add(creditsLabel, BorderLayout.NORTH);
		westPanel.add(storePanel, BorderLayout.CENTER);
		
		//add to overall panel
		add(westPanel, BorderLayout.WEST);
	}
	
	/**
	 * @param profile the UserProfile that will be able to purchase and open packs
	 * and save the cards they acquired
	 */
	private void createCardPackSide(UserProfile profile){
		cardpackPanel = new JPanel();
		cardpackPanel.setOpaque(false);
		cardpackPanel.setLayout(new BorderLayout());
		
		packsLabel.setForeground(Color.WHITE);
		packsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		packsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		//create buttons and button panel
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setOpaque(false);
		JButton openPack = new JButton("Open Pack");
		buttonsPanel.add(openPack);
		buttonsPanel.add(finishButton);
		
		ActionListener openPackListener = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent event) {
				if(profile.getPacks().size() > 0)
				openPack(profile);
				updateLabels(profile);
			}
		};
		openPack.addActionListener(openPackListener);
		
		//Create panel that will contain image of a card pack.
		cardPacksImagePanel = new JPanel();
		cardPacksImagePanel.setOpaque(false);
		cardPacksImagePanel.setBorder(BorderFactory.createLoweredBevelBorder());
		cardPacksImagePanel.setLayout(new BorderLayout());
		cardPacksImagePanel.add(packsLabel, BorderLayout.NORTH);
		
		cardsInPackPanel = new JPanel();
		cardsInPackPanel.setOpaque(false);
		cardsInPackPanel.setLayout(new FlowLayout());
		cardPacksImagePanel.add(cardsInPackPanel, BorderLayout.CENTER);
		
		//add parts to the cardpackPanel
		cardpackPanel.add(cardPacksImagePanel, BorderLayout.CENTER);
		cardpackPanel.add(buttonsPanel, BorderLayout.SOUTH);
		
		//add to overall panel
		add(cardpackPanel, BorderLayout.CENTER);
		
		
	}
	
	/**
	 * @param profile Userprofile that will have their credits updated
	 * and will change the displays on this GUI
	 */
	private void updateLabels(UserProfile profile){
		creditsLabel.setText("You have " + profile.getCredits().toString() + " Credits!");
		if(profile.getPacks().size() == 1){
			packsLabel.setText("You have 1 pack!");
		} else {
			packsLabel.setText("You have " + profile.getPacks().size() + " packs!");
		}
		creditsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		packsLabel.setHorizontalAlignment(SwingConstants.CENTER);
	}
	
	/**
	 * @param profile UserProfile that will have the credits removed
	 * and will display the cards the player opened
	 * from the pack
	 */
	private void openPack(UserProfile profile){
		cardsInPackPanel.removeAll();
		updateUI();
		Pack packToOpen = profile.getPacks().get(0);
		for(Card card: packToOpen.getCardsInPack()){
			profile.addCard(card);
			CardImagePanel cardImagePanel = new CardImagePanel(CardImagePanel.HAND_MODE);
			cardImagePanel.setActive(false);
			cardImagePanel.updateDisplay(card, true);
			Dimension d = new Dimension(177,250);
			cardImagePanel.setPreferredSize(d);
			cardImagePanel.setMinimumSize(d);
			cardImagePanel.setMaximumSize(d);
			cardsInPackPanel.add(cardImagePanel);
//			cardsInPackPanel.add(new JLabel(card.getReducedSizeImageIcon(0.50)));
		}
		profile.getPacks().remove(0);
		
	}

	protected void paintComponent(Graphics g1) {
		super.paintComponent(g1);
		g1.drawImage(new ImageIcon("images//feltBackground.png").getImage(), 0, 0, null);
	}
}
