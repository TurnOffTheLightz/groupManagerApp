package Oskroba_Miron_lab_03;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
/*
 *    Plik: GroupManagerApp.java
 *          
 *   Autor: Miron Oskroba
 *    Data: listopad 2019 r.
 */
public class GroupManagerApp extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	private final String ABOUT_MESSAGE = "Autor: Miron Oskroba\n"
			+ "Data: Listopad 2019\n"
			+ "Aplikacja do zarz¹dzania grupami plakatów";
	
	private final String ALL_GROUPS_FILE = "LISTA_GRUP_PLAKATOW.TMP";
	
	WindowAdapter windowListener = new WindowAdapter() {

		@Override
		public void windowClosed(WindowEvent e) {
			saveGroupsToFile(ALL_GROUPS_FILE);
			JOptionPane.showMessageDialog(null, "Program zakoñczy³ dzia³anie!");
		}

		@Override
		public void windowClosing(WindowEvent e) {
			windowClosed(e);
		}
	};//klasa anonimowa
	
//menu	
	private JMenuBar menuBar = new JMenuBar();
	
	private JMenu menuGroups = new JMenu("Grupy");
	private JMenu menuOperations = new JMenu("Operacje specjalne");
	private JMenu menuAbout = new JMenu("O programie");
	
	private JMenuItem menuNewGroup = new JMenuItem("Nowa grupa");
	private JMenuItem menuEditGroup = new JMenuItem("Edytuj grupê");
	private JMenuItem menuDeleteGroup = new JMenuItem("Usuñ grupê");
	private JMenuItem menuReadGroup = new JMenuItem("Wczytaj grupê z pliku");
	private JMenuItem menuSaveGroup = new JMenuItem("Zapisz grupê do pliku");
	
	private JMenuItem menuSumOperation = new JMenuItem("Suma grup");
	private JMenuItem menuIntersectionOperation = new JMenuItem("Iloczyn grup");
	private JMenuItem menuDifferenceOperation = new JMenuItem("Ró¿nica grup");
	private JMenuItem menuSymmetricDifferenceOperation = new JMenuItem("Ró¿nica symetryczna grup");
	
	private JMenuItem menuAuthor = new JMenuItem("Autor");
	
//buttons
	private JPanel panelButton = new JPanel();
	private JButton buttonNewGroup = new JButton("Utwórz");
	private JButton buttonEditGroup = new JButton("Edytuj");
	private JButton buttonDeleteGroup = new JButton("Usuñ");
	private JButton buttonReadGroup = new JButton("Wczytaj");
	private JButton buttonSaveGroup = new JButton("Zapisz");
	private JButton buttonSumOperation= new JButton("Sum");
	private JButton buttonIntersectionOperation = new JButton("Iloczyn");
	private JButton buttonDifferenceOperation = new JButton("Ró¿nica");
	private JButton buttonSymmetricDifferenceOperation = new JButton("Ró¿nica symetryczna");
//GroupScrollPane	
	private GroupViewList groupViewList;
//list of groups of posters
	private List<GroupOfPosters> groups = new ArrayList<GroupOfPosters>();
	

	public GroupManagerApp() {
		addWindowListener(windowListener);
		addActionListeners();
		initMenu();
		initButtons();
		initGroupViewList();
		initFrame();
		
	}
	
	private void initFrame() {
		setTitle("GroupManager - zarz¹dzanie grupami plakatów");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void addActionListeners() {
	//menu	
		menuNewGroup.addActionListener(this);
		menuEditGroup.addActionListener(this);
		menuDeleteGroup.addActionListener(this);
		menuReadGroup.addActionListener(this);
		menuSaveGroup.addActionListener(this);
		
		menuSumOperation.addActionListener(this);
		menuIntersectionOperation.addActionListener(this);
		menuDifferenceOperation.addActionListener(this);
		menuSymmetricDifferenceOperation.addActionListener(this);
		
		menuAuthor.addActionListener(this);
	//buttons	
		buttonNewGroup.addActionListener(this);
		buttonEditGroup.addActionListener(this);
		buttonDeleteGroup.addActionListener(this);
		buttonReadGroup.addActionListener(this);
		buttonSaveGroup.addActionListener(this);
		buttonSumOperation.addActionListener(this);
		buttonIntersectionOperation.addActionListener(this);
		buttonDifferenceOperation.addActionListener(this);
		buttonSymmetricDifferenceOperation.addActionListener(this);
	}
	
	private void initMenu() {
		menuBar.add(menuGroups);
		menuBar.add(menuOperations);
		menuBar.add(menuAbout);
		
		menuGroups.add(menuNewGroup);
		menuGroups.add(menuEditGroup);
		menuGroups.add(menuDeleteGroup);
		menuGroups.add(menuReadGroup);
		menuGroups.add(menuSaveGroup);
		
		menuOperations.add(menuSumOperation);
		menuOperations.add(menuIntersectionOperation);
		menuOperations.add(menuDifferenceOperation);
		menuOperations.add(menuSymmetricDifferenceOperation);
		
		menuAbout.add(menuAuthor);

		setJMenuBar(menuBar);
	}

	private void initButtons() {
		panelButton.setPreferredSize(new Dimension(500,65));
		panelButton.add(buttonNewGroup);
		panelButton.add(buttonEditGroup);
		panelButton.add(buttonDeleteGroup);
		panelButton.add(buttonReadGroup);
		panelButton.add(buttonSaveGroup);
		panelButton.add(buttonSumOperation);
		panelButton.add(buttonIntersectionOperation);
		panelButton.add(buttonDifferenceOperation);
		panelButton.add(buttonSymmetricDifferenceOperation);
		add(panelButton,BorderLayout.SOUTH);
	}
	
	private void initGroupViewList() {
		loadGroupsFromFile(ALL_GROUPS_FILE);
		
		groupViewList = new GroupViewList(groups);
		add(groupViewList);
		groupViewList.updateView();
	}
	
	private GroupOfPosters chooseGroup(Window parent, String message) {
		Object[] groupsObj = groups.toArray();
		GroupOfPosters groupOfPosters = 
				(GroupOfPosters)JOptionPane.showInputDialog(parent, message,
				"Wybierz grupê:",JOptionPane.QUESTION_MESSAGE,null,groupsObj,null);
		return groupOfPosters;
	}
	
	@Override
	public void actionPerformed(ActionEvent ev) {
		Object source = ev.getSource();
		
		try {
		if(source == buttonNewGroup || source == menuNewGroup) {
			GroupOfPosters groupOfPosters = GroupOfPostersWindowDialog.createGroupOfPosters(this);
			if(groupOfPosters != null) {
				groups.add(groupOfPosters);
			}
			
		}else if(source == buttonEditGroup || source == menuEditGroup) {
			int index = groupViewList.getSelectedIndex();
			if(index<0)
				return;
			Iterator<GroupOfPosters> iterator = groups.iterator();
			while(index-- > 0 )
				iterator.next();
			new GroupOfPostersWindowDialog(this, iterator.next());
			
		}else if(source == buttonDeleteGroup || source == menuDeleteGroup) {
			int index = groupViewList.getSelectedIndex();
			if(index<0)
				return;
			Iterator<GroupOfPosters> iterator = groups.iterator();
			while(index-- >= 0 )
				iterator.next();
			iterator.remove();
			
		}else if(source == buttonReadGroup || source == menuReadGroup) {
			JFileChooser chooser = new JFileChooser(".");
			int returnVal = chooser.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				try {
					GroupOfPosters gOfPosters =  GroupOfPosters.readFromFile(chooser.getSelectedFile().getName());
					groups.add(gOfPosters);
				} catch (PosterException e) {
					JOptionPane.showMessageDialog(this, "Nie znaleziono pliku" ,"B³¹d.",JOptionPane.ERROR_MESSAGE);
				}
			}
			
		}else if(source == buttonSaveGroup || source == menuSaveGroup) {
			int index = groupViewList.getSelectedIndex();
			if(index < 0) return;
			JFileChooser chooser = new JFileChooser(".");
			int returnVal = chooser.showSaveDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
				Iterator<GroupOfPosters> iterator = groups.iterator();
				while(index-- > 0 )
					iterator.next();
				try {
					GroupOfPosters.printToFile(chooser.getSelectedFile().getName(), iterator.next());
				} catch (PosterException e) {
					JOptionPane.showMessageDialog(this, "Nie znaleziono pliku" ,"B³¹d.",JOptionPane.ERROR_MESSAGE);
				}
			}
		}else if(source == buttonSumOperation || source == menuSumOperation) {
			String msg1 = "SUMA GRUP\n"
					+ "Tworzy grupê, która jest sum¹ dwóch wybranych grup\n"
					+ "Wybierz pierwsz¹ grupê:";
			String msg2 = "SUMA GRUP\n"
					+ "Tworzy grupê, która jest sum¹ dwóch wybranych grup\n"
					+ "Wybierz drug¹ grupê:";
			GroupOfPosters g1 = chooseGroup(this, msg1);
			if(g1 == null) return;
			GroupOfPosters g2 = chooseGroup(this, msg2);
			if(g2 == null) return;
			groups.add(GroupOfPosters.createGroupSum(g1, g2));
			
		}else if(source == buttonIntersectionOperation || source == menuIntersectionOperation) {
			String msg1 = "ILOCZYN GRUP\n"
					+ "Tworzy grupê, która zawiera elementy powtarzaj¹ce siê w dwóch wybranych grupach\n"
					+ "Wybierz pierwsz¹ grupê:";
			String msg2 = "SUMA GRUP\n"
					+ "Tworzy grupê, która zawiera elementy powtarzaj¹ce siê w dwóch wybranych grupach\n"
					+ "Wybierz drug¹ grupê:";
			GroupOfPosters g1 = chooseGroup(this, msg1);
			if(g1 == null) return;
			GroupOfPosters g2 = chooseGroup(this, msg2);
			if(g2 == null) return;
			groups.add(GroupOfPosters.createGroupIntersection(g1, g2));
		}else if(source == buttonDifferenceOperation || source == menuDifferenceOperation) {
			String msg1 = "RÓ¯NICA GRUP\n"
					+ "Tworzy grupê, której elementy s¹ w pierwszej grupie i nie ma w drugiej\n"
					+ "Wybierz pierwsz¹ grupê:";
			String msg2 = "SUMA GRUP\n"
					+ "Tworzy grupê, której elementy s¹ w pierwszej grupie i nie ma w drugiej\n"
					+ "Wybierz drug¹ grupê:";
			GroupOfPosters g1 = chooseGroup(this, msg1);
			if(g1 == null) return;
			GroupOfPosters g2 = chooseGroup(this, msg2);
			if(g2 == null) return;
			groups.add(GroupOfPosters.createGroupDifference(g1, g2));
		}else if(source == buttonSymmetricDifferenceOperation|| source == menuSymmetricDifferenceOperation) {
			String msg1 = "RÓ¯NICA SYMETRYCZNA GRUP\n"
					+ "Tworzy grupê, której elementy s¹ tylko w jednej z dwóch grup\n"
					+ "Wybierz pierwsz¹ grupê:";
			String msg2 = "SUMA GRUP\n"
					+ "Tworzy grupê, której elementy s¹ tylko w jednej z dwóch grup\n"
					+ "Wybierz drug¹ grupê:";
			GroupOfPosters g1 = chooseGroup(this, msg1);
			if(g1 == null) return;
			GroupOfPosters g2 = chooseGroup(this, msg2);
			if(g2 == null) return;
			groups.add(GroupOfPosters.createGroupSymmetricDifference(g1, g2));
		}else if(source == menuAuthor) {
			JOptionPane.showMessageDialog(this, ABOUT_MESSAGE);
		}
		}catch(PosterException e) {
			JOptionPane.showMessageDialog(this, "B³¹d przy tworzeniu grupy.","B³¹d.", JOptionPane.ERROR_MESSAGE);
		}
		groupViewList.updateView();
	}

//save on close, read on startup methods:
	private void saveGroupsToFile(String fileName) {
		try(ObjectOutputStream out  = new ObjectOutputStream(new FileOutputStream(fileName))){
			out.writeObject(groups);
			out.close();
			JOptionPane.showMessageDialog(this, "Zapisano grupy do pliku: " + ALL_GROUPS_FILE);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Nie znaleziono pliku: "+ fileName, "B³¹d.", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "B³¹d podczas zapisywania grup do pliku. Zmiany niezapisane.", "B³¹d.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void loadGroupsFromFile(String fileName) {
		try(ObjectInputStream in  = new ObjectInputStream(new FileInputStream(fileName))){
			groups = (List<GroupOfPosters>) in.readObject();
			in.close();
			JOptionPane.showMessageDialog(this, "Wczytano grupy z pliku: " + ALL_GROUPS_FILE);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Nie znaleziono pliku: " + fileName, "B³¹d.", JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "B³¹d podczas czytania danych z pliku.", "B³¹d.", JOptionPane.ERROR_MESSAGE);
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(this, "B³¹d w czytaniu plików", "B³¹d.", JOptionPane.ERROR_MESSAGE);
		}
	}
}

class GroupViewList extends JScrollPane{
	private static final long serialVersionUID = 1L;
	
	private List<GroupOfPosters> groupOfPosters;
	private JTable table;
	private DefaultTableModel tableModel;
	
	public GroupViewList(List<GroupOfPosters> gOfPosters) {
		this.groupOfPosters = gOfPosters;
		setPreferredSize(new Dimension(300,300));
		String[] header = {"nazwa grupy","typ kolekcji","liczba plakatów w grupie"};
		tableModel = new DefaultTableModel(header,0);
		setBorder(BorderFactory.createTitledBorder("Lista grup:"));
		
		table = new JTable(tableModel){
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int col) {//blokada edycji
				return false;
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		setViewportView(table);
	}
	
	void updateView() {
		tableModel.setRowCount(0);
		for(GroupOfPosters g : groupOfPosters) {
			if(g!=null) {
				String[] row = {g.getGroupName(), g.getGroupType().toString(), g.size() + ""};
				tableModel.addRow(row);
			}
		}
//		table.setModel(tableModel);
	}
	
	int getSelectedIndex() {
		int index = table.getSelectedRow();
		if(index<0) {
			JOptionPane.showMessageDialog(this, "Zaznacz wiersz.", "B³¹d.", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}
}
