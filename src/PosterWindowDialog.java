package Oskroba_Miron_lab_03;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/*
 *    Plik: PosterWindowDialog.java
 *          
 *   Autor: Miron Oskroba
 *    Data: listopad 2019 r.
 */
public class PosterWindowDialog extends JDialog implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	private Window parent;

	private JLabel labelSize = new JLabel("Wymiary np.80x80");
	private JLabel labelType = new JLabel("Typ plakatu:");
	private JLabel labelColor = new JLabel("Kolor:");
	private JLabel labelPhotosNumber = new JLabel("Liczba zdjêæ:");
	
	private JTextField fieldSize =  new JTextField(7);
	private JTextField fieldColor =  new JTextField(12);
	private JTextField fieldPhotosNumber =  new JTextField(7);
	JComboBox<PosterType> boxType = new JComboBox<PosterType>(PosterType.values());
	
	private JButton buttonOk = new JButton("OK");
	private JButton buttonDeny = new JButton("Anuluj");
	
	private Poster poster;
	
	private JPanel panel = new JPanel();
	public PosterWindowDialog(Window parent, Poster pstr) {
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		this.parent = parent;
		this.poster = pstr;
		if(poster == null)
			this.setTitle("Tworzenie nowego plakatu.");
		else
			setTitle("Modyfikacja danych istniej¹cego plakatu.");
		addActionListeners();
		initComponents();
		initDialog();
	}
	
	private void addActionListeners() {
		buttonOk.addActionListener(this);
		buttonDeny.addActionListener(this);
	}
	
	private void initDialog() {
		setContentPane(panel);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(220,200));
		setLocationRelativeTo(parent);
		setVisible(true);
	}
	
	private void initComponents() {
		panel.add(labelType);
		panel.add(boxType);
		panel.add(labelSize);
		panel.add(fieldSize);
		panel.add(labelColor);
		panel.add(fieldColor);
		panel.add(labelPhotosNumber);
		panel.add(fieldPhotosNumber);
		
		panel.add(buttonOk);
		panel.add(buttonDeny);
		
		if(poster!=null) {
			boxType.setSelectedItem(poster.getType());
			fieldSize.setText(poster.getStringSize());
			fieldColor.setText(poster.getColor());
			fieldPhotosNumber.setText(poster.getPhotosNumber()+"");
		}
	}
	
	public static Poster createNewPoster(Window parent) {
		return new PosterWindowDialog(parent, null).poster;
	}

	public static void changePosterData(Window parent, Poster poster) {
		new PosterWindowDialog(parent, poster);
	}

	@Override
	public void actionPerformed(ActionEvent event){
		Object source = event.getSource();
		if(source == buttonOk) {
				try {
					if(poster == null)
						poster = new Poster(boxType.getSelectedItem().toString(), fieldSize.getText());
					else {
						poster.setSize(fieldSize.getText());
						poster.setType(boxType.getSelectedItem().toString());
					}
					poster.setColor(fieldColor.getText());

					int photosNumber = Integer.parseInt(fieldPhotosNumber.getText());
					poster.setPhotosNumber(photosNumber);
					dispose();
				}catch (NumberFormatException | PosterException e) {
					JOptionPane.showMessageDialog(this, e.getMessage() , "B³¹d", JOptionPane.ERROR_MESSAGE);
				}
		}else if(source == buttonDeny){
			dispose();
		}
	}	
}
