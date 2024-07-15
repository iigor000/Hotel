package view.receptionist;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import manage.ManagerFactory;
import manage.RoomManager;
import net.miginfocom.swing.MigLayout;
import tableModels.RoomsModel;

public class RoomsPanel extends JPanel {

	private static final long serialVersionUID = 1L;


	public RoomsPanel(JTable roomsTable) {
		setLayout(new MigLayout("", "[grow][grow][]", "[][grow][][]"));
        
        JLabel titlelbl = new JLabel("Sobe");
        titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
        add(titlelbl, "cell 0 0,alignx center,spanx 3");
        
        JScrollPane scrollPane = new JScrollPane(roomsTable);
        add(scrollPane, "cell 0 1,grow,spanx 3");
        
        JLabel startlbl = new JLabel("Datum pocetka");
        add(startlbl, "cell 0 2");
        
        JTextField startField = new JTextField();
        add(startField, "cell 0 3,growx,height 30::");
        
        JLabel endlbl = new JLabel("Datum kraja");
        add(endlbl, "cell 1 2");
        
        JTextField endField = new JTextField();
        add(endField, "cell 1 3,growx,height 30::");
        
        JButton checkbtn1 = new JButton("Proveri slobodnost");
        
		checkbtn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = roomsTable.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Morate izabrati sobu", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String start = startField.getText();
				String end = endField.getText();
				try {
					LocalDate.parse(start);
					LocalDate.parse(end);
				} catch (DateTimeParseException ex) {
					JOptionPane.showMessageDialog(null, "Datum mora biti u formatu yyyy-MM-dd", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				RoomsModel model = (RoomsModel) roomsTable.getModel();
				RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
				if (roomManager.checkRoomAvailable(model.getRoomNumberAt(selectedRow), LocalDate.parse(start),
						LocalDate.parse(end))) {
					JOptionPane.showMessageDialog(null, "Soba je slobodna", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Soba nije slobodna", "Greska", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		Dimension buttonSize = new Dimension(150, 50);
		checkbtn1.setMinimumSize(buttonSize);
		checkbtn1.setMaximumSize(buttonSize);
		checkbtn1.setPreferredSize(buttonSize);
		
		add(checkbtn1, "cell 2 3,alignx center");
	}

}
