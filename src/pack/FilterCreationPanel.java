package pack;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pack.filters.ContainsKey;
import pack.filters.Filter;
import pack.filters.KeyEqual;
import pack.filters.KeyNotEqual;
import pack.filters.NotContainsKey;

public class FilterCreationPanel extends JPanel{

	JComboBox<String> filterTypes;
	
	JTextField txt_1;
	JLabel lbl;
	JTextField txt_2;
	
	public FilterCreationPanel() {
		
		setLayout(new FlowLayout());
		
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>();
		comboModel.addElement("Key equals");
		comboModel.addElement("Key NOT equals");
		comboModel.addElement("Hit contains key");
		comboModel.addElement("Hit NOT contains key");
		filterTypes = new JComboBox<String>(comboModel);
		filterTypes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				switch(filterTypes.getSelectedIndex()) {
				case 0:
					System.out.println("zero!");
					remove(lbl);
					remove(txt_2);
					lbl.setText("=");
					add(lbl);
					add(txt_2);
					break;
				case 1:
					System.out.println("um!");
					remove(lbl);
					remove(txt_2);
					lbl.setText("!=");
					add(lbl);
					add(txt_2);
					break;
				case 2:
					System.out.println("dois!");
					remove(lbl);
					remove(txt_2);
					break;
				case 3:
					System.out.println("tres!");
					remove(lbl);
					remove(txt_2);
					break;
					
				}
				repaint();
			}
		});
		add(filterTypes);
		
		txt_1 = new JTextField();
		txt_1.setPreferredSize(new Dimension(100, 30));
		add(txt_1);
		lbl = new JLabel("=", JLabel.CENTER);
		lbl.setPreferredSize(new Dimension(20, 30));
		add(lbl);
		txt_2 = new JTextField();
		txt_2.setPreferredSize(new Dimension(100, 30));
		add(txt_2);
	}
	
	public Filter createFilter() {
		switch(filterTypes.getSelectedIndex()) {
		case 0:
			return new KeyEqual(txt_1.getText(), txt_2.getText());
		case 1:
			return new KeyNotEqual(txt_1.getText(), txt_2.getText());
		case 2:
			return new ContainsKey(txt_1.getText());
		case 3:
			return new NotContainsKey(txt_1.getText());
		}
		return null;
	}
}
