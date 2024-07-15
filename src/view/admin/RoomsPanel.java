package view.admin;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import manage.ManagerFactory;
import model.RoomType;
import net.miginfocom.swing.MigLayout;
import tableModels.RoomsModel;

public class RoomsPanel extends JPanel {

	private static final long serialVersionUID = 1L;


	public RoomsPanel(JTable roomsTable) {
		Dimension buttonSize = new Dimension(150, 50);
		
		setLayout(new MigLayout("", "[grow]", "[][grow][]"));
        
        JLabel titlelbl = new JLabel("Sobe");
        titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
        add(titlelbl, "cell 0 0,alignx center");
        
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        add(scrollPane, "cell 0 1,grow");
        
        JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		JButton addbtn = new JButton("Dodaj sobu");
		addbtn.setMinimumSize(buttonSize);
		addbtn.setMaximumSize(buttonSize);
		addbtn.setPreferredSize(buttonSize);
		JButton deletebtn = new JButton("Obrisi sobu");
		deletebtn.setMinimumSize(buttonSize);
		deletebtn.setMaximumSize(buttonSize);
		deletebtn.setPreferredSize(buttonSize);
		JButton changebtn1 = new JButton("Izmeni sobu");
		changebtn1.setMinimumSize(buttonSize);
		changebtn1.setMaximumSize(buttonSize);
		changebtn1.setPreferredSize(buttonSize);
		
		addbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddRoomDialog((RoomsModel) roomsTable.getModel());
			}
		});
		
		deletebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = roomsTable.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati sobu", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int number = (int) roomsTable.getValueAt(row, 0);
				if (! ManagerFactory.getInstance().getRoomManager().removeRoom(number)) {
					JOptionPane.showMessageDialog(null, "Soba je zauzeta", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				((RoomsModel) roomsTable.getModel()).fireTableDataChanged();

				JOptionPane.showMessageDialog(null, "Soba je obrisana", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		changebtn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = roomsTable.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati sobu", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int number = (int) roomsTable.getValueAt(row, 0);
				
				if (!ManagerFactory.getInstance().getRoomManager().isAvailable(number)) {
					JOptionPane.showMessageDialog(null, "Soba ima rezervacije ili je zauzeta, ne moze se trenutno menjati", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				RoomType type = (RoomType) roomsTable.getValueAt(row, 1);
				
				new EditRoomDialog(number, type, (RoomsModel) roomsTable.getModel());
			}
		});
		
		buttonPanel.add(addbtn);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(changebtn1);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(deletebtn);

		add(buttonPanel, "cell 0 2,growx");
	}

}
