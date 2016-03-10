package cardGame_v1.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.TextLayout;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginPanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private JButton login = new JButton("Login");
	private JButton register = new JButton("Create Profile");
	private JTextField profileTextField = new JTextField(20);
	
	/**
	 * Constructor for LoginPanel
	 */
	public LoginPanel(){
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		//Profile textbox
		profileTextField.setAlignmentX(Component.CENTER_ALIGNMENT);
		profileTextField.setHorizontalAlignment(SwingConstants.CENTER);
		profileTextField.setMaximumSize(new Dimension(400,22));
		profileTextField.setPreferredSize(new Dimension(400,22));

		//Login button
		login.setAlignmentX(Component.CENTER_ALIGNMENT);
		login.setHorizontalAlignment(SwingConstants.CENTER);

		//create profile button
		register.setAlignmentX(Component.CENTER_ALIGNMENT);
		register.setHorizontalAlignment(SwingConstants.CENTER);

		add(Box.createVerticalGlue());
		add(profileTextField);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(login);
		add(Box.createRigidArea(new Dimension(0, 10)));
		add(register);
		add(Box.createVerticalGlue());
	}
	
	/**
	 * Overridden method that will draw the background for the panel and text
	 */
	@Override
	  protected void paintComponent(Graphics g1) {

	    super.paintComponent(g1);
			Graphics2D g = (Graphics2D) g1;
			g1.drawImage(new ImageIcon("images//blackBackground.png").getImage(), 0, 0, null);
			
			boolean antialiasing = true;
			if(antialiasing){
				g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			}
			String s = "Please enter your profile name below";
			Font f = MainGUI.getGameFont().deriveFont(Font.PLAIN, (float) 22.5);
			TextLayout t1 = new TextLayout(s,f,g.getFontRenderContext());
			Shape shape = t1.getOutline(null);
			g.setStroke(new BasicStroke((float) 4));
			g.setColor(Color.BLACK);
			g.translate(40, 150);
			g.draw(shape);
			g.setColor(Color.white);
			g.fill(shape);
			g.translate(-40, -150);
	}

	/**
	 * @return the login
	 */
	public JButton getLogin() {
		return login;
	}
	
	/**
	 * @return the register
	 */
	public JButton getRegister() {
		return register;
	}

	/**
	 * @return the profileTextField
	 */
	public JTextField getProfileTextField() {
		return profileTextField;
	}
}
