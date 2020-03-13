package Oskroba_Miron_lab_03;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
/*
 *    Plik: GroupOfPostersWindowDialog.java
 *          
 *   Autor: Miron Oskroba
 *    Data: listopad 2019 r.
 */
public class GroupOfPostersWindowDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 1L;
	private final String ABOUT_MESSAGE = "Autor: Miron Oskroba\n"
			+ "Data: Listopad 2019\n"
			+ "Aplikacja do zarz¹dzania grupami plakatów";
	private Window parent;
	
	//infoHeader:
		private static JLabel labelGroupName = new JLabel("Nazwa grupy:");
		private static JLabel labelGroupType = new JLabel("Rodzaj kolekcji:");
		private static JTextField fieldGroupName = new JTextField(22);
		private static JTextField fieldGroupType = new JTextField(22);	
	//menu	
		private JMenuBar menuBar = new JMenuBar();
		
		private JMenu menuList = new JMenu("Lista plakatów");
		private JMenu menuSort = new JMenu("Sortowanie");
		private JMenu menuPrefs = new JMenu("W³aœciwoœci");
		private JMenu menuAbout = new JMenu("O programie");
		
		private JMenuItem menuNewPoster = new JMenuItem("Dodaj nowy plakat");
		private JMenuItem menuEditPoster = new JMenuItem("Edytuj plakat");
		private JMenuItem menuDeletePoster = new JMenuItem("Usuñ plakat");
		private JMenuItem menuReadPoster = new JMenuItem("Wczytaj plakat z pliku");
		private JMenuItem menuSavePoster = new JMenuItem("Zapisz plakat do pliku");
		
		private JMenuItem menuSortColor = new JMenuItem("Sortuj alfabetycznie");
		private JMenuItem menuSortPhotosNumber = new JMenuItem("Sortuj wg. iloœci zdjêæ");
		private JMenuItem menuSortType = new JMenuItem("Sortuj wg. typu");
		private JMenuItem menuSortSize = new JMenuItem("Sortuj wg. wymiarów");
		
		
		private JMenuItem menuChangeName = new JMenuItem("Zmieñ nazwê grupy");
		private JMenuItem menuChangeCollectionType = new JMenuItem("Zmie typ kolekcji");
		
		private JMenuItem menuAuthor = new JMenuItem("about");
		
	//buttons
		private JPanel  panelButton = new JPanel();
		
		private JButton buttonNewPoster = new JButton("Utwórz nowy plakat");
		private JButton buttonEditPoster = new JButton("Edytuj zaznaczony");
		private JButton buttonDeletePoster = new JButton("Usuñ zaznaczony");
		private JButton buttonReadPoster = new JButton("Wczytaj z pliku");
		private JButton buttonSavePoster = new JButton("Zapisz do pliku");
	
		private JPanel panel = new JPanel();
		
	//ViewPosterList
		private static PosterViewList posterViewList;
		
		private static GroupOfPosters posterList;//TODO:: rename posterList
		
		public GroupOfPostersWindowDialog(Window parent, GroupOfPosters pList) {	super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		this.parent = parent;
		posterList = pList;
		if(posterList!=null) {
			fieldGroupName.setText(posterList.getGroupName());
			fieldGroupType.setText(posterList.getGroupType().toString());
		}
		addActionListeners();
		initMenu();
		initHeader();
		initViewPosterList();
		initButtons();
		initDialog();
		
		
		setContentPane(panel);
		setVisible(true);
	}
		
	private void addActionListeners() {
		menuNewPoster.addActionListener(this);
		menuEditPoster.addActionListener(this);
		menuDeletePoster.addActionListener(this);
		menuReadPoster.addActionListener(this);
		menuSavePoster.addActionListener(this);
		
		menuSortColor.addActionListener(this);
		menuSortPhotosNumber.addActionListener(this);
		menuSortType.addActionListener(this);
		menuSortSize.addActionListener(this);
		
		menuChangeName.addActionListener(this);
		menuChangeCollectionType.addActionListener(this);
		
		menuAuthor.addActionListener(this);
		
		buttonNewPoster.addActionListener(this);
		buttonEditPoster.addActionListener(this);
		buttonDeletePoster.addActionListener(this);
		buttonReadPoster.addActionListener(this);
		buttonSavePoster.addActionListener(this);	
		}

	private void initMenu() {
		menuBar.add(menuList);
		menuBar.add(menuSort);
		menuBar.add(menuPrefs);
		menuBar.add(menuAbout);

		menuList.add(menuNewPoster);
		menuList.add(menuEditPoster);
		menuList.add(menuDeletePoster);
		menuList.add(menuReadPoster);
		menuList.add(menuSavePoster);
		
		menuSort.add(menuSortColor);		
		menuSort.add(menuSortPhotosNumber);	
		menuSort.add(menuSortType);	
		menuSort.add(menuSortSize);
		
		menuPrefs.add(menuChangeName);
		menuPrefs.add(menuChangeCollectionType);
		
		menuAbout.add(menuAuthor);
		setJMenuBar(menuBar);
	}

	private void initHeader() {
		JPanel tempPanel = new JPanel();
		tempPanel.add(labelGroupName);
		tempPanel.add(fieldGroupName);
		tempPanel.add(labelGroupType);
		tempPanel.add(fieldGroupType);
		fieldGroupName.setEditable(false);
		fieldGroupType.setEditable(false);
		panel.add(tempPanel);
	}
	
	private void initButtons(){
		panelButton.add(buttonNewPoster);
		panelButton.add(buttonEditPoster);
		panelButton.add(buttonDeletePoster);
		panelButton.add(buttonReadPoster);
		panelButton.add(buttonSavePoster);
		panel.add(panelButton,BorderLayout.SOUTH);
	}
	
	private void initViewPosterList() {
		posterViewList = new PosterViewList(posterList);
		posterViewList.updateView();
		panel.add(posterViewList,BorderLayout.CENTER);
	}
	
	private void initDialog() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(755,300));
		setLocationRelativeTo(parent);
		setTitle("Okno grupy plakatów.");
	}
	
	public static GroupOfPosters createGroupOfPosters(Window parent) {
		String gName = (String)JOptionPane.showInputDialog("Podaj nazwê nowej grupy");
		
		if(gName == null || gName.equals("")){
			JOptionPane.showMessageDialog(parent, "Nazwa grupy musi mieæ przynajmniej jeden znak.");
			return null;
		}
		
		Object[] groupsObj = GroupType.values();
		GroupType gType = (GroupType) JOptionPane.showInputDialog(parent,"Wybieranie typu kolekcji",
				"Wybierz grupê:",JOptionPane.QUESTION_MESSAGE,null,groupsObj,null);
		
		if(gType == null || gType.toString().equals("")){
			JOptionPane.showMessageDialog(parent, "Nie wybrano typu grupy.");
			return null;
		}
		
		try {
			posterList = new GroupOfPosters(gType+"", gName);
		} catch (PosterException e) {
			JOptionPane.showMessageDialog(parent, "Nieprawid³owy typ grupy lub brak nazwy.");
		}
		if(posterList!=null){
			fieldGroupName.setText(gName);
			fieldGroupType.setText(gType.toString());
			new GroupOfPostersWindowDialog(parent, posterList);		
		}
		return posterList;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		
		if(source == buttonNewPoster || source == menuNewPoster) {
			Poster poster = PosterWindowDialog.createNewPoster(this);
			if(poster!=null) {
				posterList.add(poster);
			}
			
		}else if(source == buttonEditPoster|| source == menuEditPoster) {
			int index = posterViewList.getSelectedIndex();
			if(index<0) return;
			Iterator<Poster> iterator = posterList.iterator();
			while (index-- > 0)
				iterator.next();
			Poster posterToChange = iterator.next();
			PosterWindowDialog.changePosterData(parent, posterToChange);//posterList.get(index))
			
		}else if(source == buttonDeletePoster|| source == menuDeletePoster) {
			int index = posterViewList.getSelectedIndex();
			if(index < 0 ) return;
			Iterator<Poster> iterator = posterList.iterator();
			while (index-- >= 0)
				iterator.next();
			iterator.remove();
	
		}else if(source == buttonReadPoster|| source == menuReadPoster) {
			JFileChooser chooser = new JFileChooser(".");
			int returnVal = chooser.showOpenDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				Poster poster;
				try {
					poster = Poster.readFromFile(chooser.getSelectedFile().getName());
					posterList.add(poster);
				} catch (PosterException e) {
					e.printStackTrace();
				}
			}
			
		}else if(source == buttonSavePoster|| source == menuSavePoster) {
			int index = posterViewList.getSelectedIndex();
			if (index >= 0) {
				Iterator<Poster> iterator = posterList.iterator();
				while (index-- > 0)
					iterator.next();
				Poster posterToSave = iterator.next();//get last item
				JFileChooser chooser = new JFileChooser(".");
				int returnVal = chooser.showSaveDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						Poster.printToFile(chooser.getSelectedFile().getName(), posterToSave);
					} catch (PosterException e) {
						JOptionPane.showMessageDialog(this, "We wskazanym pliku dane nie s¹ poprawne.", "B³¹d", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}else if(source == menuAuthor) {
			JOptionPane.showMessageDialog(parent, ABOUT_MESSAGE);
			
		}else if(source == menuChangeName) {
			String newName = (String)JOptionPane.showInputDialog("Podaj now¹ nazwê grupy.");
			try {
				posterList.setGroupName(newName);
				fieldGroupName.setText(newName);
			} catch (PosterException e) {
//				System.out.println("Zamkniêto okno zmiany nazwy.");
			}
		}else if(source == menuChangeCollectionType) {
			try {
				Object[] groupsObj = GroupType.values();
				GroupType gType = (GroupType)JOptionPane.showInputDialog(parent, "Wybierz nowy typ kolekcji:",
						"Wybierz grupê:",JOptionPane.QUESTION_MESSAGE,null,groupsObj,null);
				
				posterList.changeGroupType(gType);
				
				fieldGroupType.setText(posterList.getGroupType()+"");
			} catch (HeadlessException | PosterException e) {
//				System.out.println("ChangeGroupType window closed//Error.");
			}
					
		}else if(source == menuSortColor) {
			try {
				posterList.sortColor();
			} catch (PosterException e) {
				JOptionPane.showMessageDialog(this, "menuSortColor exception");
			}
		}else if(source == menuSortPhotosNumber) {
			try {
				posterList.sortPhotosNumber();
			} catch (PosterException e) {
				JOptionPane.showMessageDialog(this, "menuSortPhotosNumber exception");
			}
		}else if(source == menuSortType) {
			try {
				posterList.sortPosterType();
			} catch (PosterException e) {
				JOptionPane.showMessageDialog(this, "menuSortType exception");
			}
		}else if(source == menuSortSize) {
			try {
				posterList.sortSize();
			} catch (PosterException e) {
				JOptionPane.showMessageDialog(this, "menuSortSize exception");
			}
		}
		posterViewList.updateView();
	}

}
class PosterViewList extends JScrollPane {
private static final long serialVersionUID = 1L;

	private GroupOfPosters posterList;
	private JTable table;
	private DefaultTableModel tableModel;

	public PosterViewList(GroupOfPosters posters) {
		this.posterList = posters;
		setPreferredSize(new Dimension(600,150));
		String[] header = {"Typ plakatu","Wymiary","kolor", "Liczba zdjêæ na plakacie"};
		tableModel = new DefaultTableModel(header,0);
		setBorder(BorderFactory.createTitledBorder("Lista grup:"));
		
		table = new JTable(tableModel){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		setViewportView(table);
	}
	
	void updateView(){
		tableModel.setRowCount(0);
		for (Poster poster : posterList) {
			if (poster != null) {
				String[] row = { poster.getType().toString(), poster.getStringSize(), poster.getColor(), poster.getPhotosNumber()+""};
				tableModel.addRow(row);
			}
		}
	}

	int getSelectedIndex(){
		int index = table.getSelectedRow();
		if (index<0) {
			JOptionPane.showMessageDialog(this, "¯aden plakat nie jest zaznaczony.", "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}

} // class ViewPosterList
