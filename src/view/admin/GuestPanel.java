package view.admin;

import javax.swing.JPanel;
import javax.swing.JTable;

import model.Guest;
import net.miginfocom.swing.MigLayout;
import tableModels.GuestModel;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JScrollPane;

public class GuestPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public GuestPanel(JTable table) {
		setLayout(new MigLayout("", "[grow]", "[][grow][]"));
		
		JLabel titlelbl = new JLabel("Gosti");
		titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(titlelbl, "cell 0 0,alignx center");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 1,grow");
		
		scrollPane.setViewportView(table);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		
		JButton addbtn= new JButton("Dodaj");
		Dimension buttonSize = new Dimension(150, 50);
		addbtn.setMinimumSize(buttonSize);
		addbtn.setMaximumSize(buttonSize);
		addbtn.setPreferredSize(buttonSize);
		JButton editbtn = new JButton("Izmeni");
		editbtn.setMinimumSize(buttonSize);
		editbtn.setMaximumSize(buttonSize);
		editbtn.setPreferredSize(buttonSize);
		JButton deletebtn = new JButton("Obrisi");
		deletebtn.setMinimumSize(buttonSize);
		deletebtn.setMaximumSize(buttonSize);
		deletebtn.setPreferredSize(buttonSize);
		
		addbtn.addActionListener(e -> {
			new AddGuestDialog(table);
		});
		
		editbtn.addActionListener(e -> {
			if (table.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(null, "Morate selektovati gosta.", "Greska", JOptionPane.ERROR_MESSAGE);
				return;
			}
			GuestModel model = (GuestModel) table.getModel();
			Guest guest = model.getGuestAt(table.getSelectedRow());
			new EditGuestDialog(table, guest);
		});
		
		deletebtn.addActionListener(e -> {
			if (table.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(null, "Morate selektovati gosta.", "Greska", JOptionPane.ERROR_MESSAGE);
				return;
			}
			GuestModel model = (GuestModel) table.getModel();
			model.deleteGuest(table.getSelectedRow());
			
			JOptionPane.showMessageDialog(null, "Gost uspesno obrisan.", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
		});
		
		buttonPanel.add(addbtn);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(editbtn);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(deletebtn);

		add(buttonPanel, "cell 0 2,growx");
	}

}
