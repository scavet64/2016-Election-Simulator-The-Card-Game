package cardGame_v1.GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

public class ListPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final boolean MULTI_SELECTION = true;
	public static final boolean SINGLE_SELECTION = false;
	public static final boolean DUPLICATES = true;
	public static final boolean NO_DUPLICATES = false;
	private JList<String> cardsList;
	private ArrayList<String> collectionInList;
	private JPanel listPanel = new JPanel();
	private boolean isMultiSelected;
	private JLabel titleLabel;
	
	/**
	 * Constructor for ListPanel
	 */
	public ListPanel(ArrayList<String> collection, String titleString, Boolean isMultiSelected, Boolean isDuplicates) {
		this.isMultiSelected = isMultiSelected;
		this.collectionInList = collection;
		
		setBorder(BorderFactory.createStrokeBorder(new BasicStroke()));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
//		JLabel titleLabel = new JLabel("             " + titleString + "             ");
//		JLabel titleLabel = new JLabel("             " + collectionInList.size() + "/" + Deck.getLimit() + "             ");
		titleLabel = new JLabel(titleString);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(titleLabel);
		listPanel.setLayout(new BorderLayout());
		listPanel.setOpaque(false);

		if(!isDuplicates){
			HashSet<String> hashset = new HashSet<String>(collectionInList);
			ArrayList<String> noDuplicateCollection = new ArrayList<String>(hashset);
			populateList(noDuplicateCollection);
		} else {
			populateList(collectionInList);
		}
		
		add(listPanel);
		setPreferredSize(new Dimension(200,500));
	}
	
	/**
	 * @param newText new String to be displayed
	 */
	public void updateTitleLabel(String newText){
		titleLabel.setText(collectionInList.size() + newText);
	}
	
	/**
	 * @return cardsList
	 */
	public JList<String> getCardsList(){
		return cardsList;
	}
	
	/**
	 * @return collectionInList
	 */
	public ArrayList<String> getCollectionInList(){
		return collectionInList;
	}
	
	/**
	 * @param collectionToSort  ArrayList to be put in alphabetical order
	 */
	private void sortInAlphebeticalOrder(ArrayList<String> collectionToSort){
		Collections.sort(collectionToSort, new Comparator<String>(){
			 @Override
		        public int compare(String s1, String s2) {
		            return s1.compareToIgnoreCase(s2);
		        }
		});
	}
	
	/**
	 * populates the JList with the new values passed into this method.
	 * @param collection the new values for the list
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void populateList(ArrayList<String> collection) {
		this.collectionInList = collection;
		listPanel.removeAll();
		sortInAlphebeticalOrder(collection);
		cardsList = new JList(collection.toArray());
		
		DefaultListSelectionModel sourceListModel = new DefaultListSelectionModel();
		if(isMultiSelected){
			sourceListModel.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		} else {
			sourceListModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		cardsList.setSelectionModel(sourceListModel);
		
		JScrollPane scrollPane = new JScrollPane(cardsList);

		listPanel.add(scrollPane,BorderLayout.CENTER);
	}

}
