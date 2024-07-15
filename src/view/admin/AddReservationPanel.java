package view.admin;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import manage.ManagerFactory;
import manage.RoomManager;
import model.RoomQuality;
import model.RoomType;
import net.miginfocom.swing.MigLayout;


public class AddReservationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField checkIn;
	private JTextField checkOut;
	private JTable table;

	public AddReservationPanel(JTable table) {
this.table = table;
		
		setLayout(new MigLayout("", "[grow][grow]", "[][][][][][][][][][]"));
	        
        JLabel titlelbl = new JLabel("Rezervisi sobu");
        titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
        add(titlelbl, "cell 0 0 2 1,alignx center");
        
        JLabel startlbl = new JLabel("Datum pocetka");
        add(startlbl, "cell 0 1");
        
        JLabel qualitylbl = new JLabel("Dodatni kriterijumi");
        add(qualitylbl, "cell 1 1");
        
        checkIn = new JTextField();
        add(checkIn, "cell 0 2,growx,hmin 30");
        checkIn.setColumns(10);
        
        JScrollPane scrollPane = new JScrollPane();
         
		JPanel checkBoxPanel = new JPanel();
		
		RoomQuality[] qualities = RoomQuality.values();

		for (RoomQuality quality : qualities) {
		    JCheckBox checkBox = new JCheckBox(quality.toString());
		    checkBoxPanel.add(checkBox);
		}
		
		scrollPane.setViewportView(checkBoxPanel);
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
		
		add(scrollPane, "cell 1 2 1 5,growx,aligny top");
		
		JLabel endlbl = new JLabel("Datum kraja");
		add(endlbl, "cell 0 3");
		
		checkOut = new JTextField();
		add(checkOut, "cell 0 4,growx,hmin 30");
		checkOut.setColumns(10);
		
		JLabel typelbl = new JLabel("Tip sobe");
		add(typelbl, "cell 0 5");
		
		RoomType[] types = RoomType.values();
		DefaultComboBoxModel<RoomType> roomTypeModel = new DefaultComboBoxModel<RoomType>(types);
			
		JComboBox<RoomType> type = new JComboBox<RoomType>();
		type.setModel(roomTypeModel);
		add(type, "cell 0 6,growx,hmin 30");
		
		JButton checkbtn = new JButton(" Proveri da li ima slobodno");
		add(checkbtn, "cell 0 7 2 1,alignx center,hmin 40");
		
		checkbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String start = checkIn.getText();
				String end = checkOut.getText();
				RoomType roomType = (RoomType) type.getSelectedItem();
				try {
					LocalDate.parse(start);
					LocalDate.parse(end);
				} catch (DateTimeParseException ex) {
					JOptionPane.showMessageDialog(null, "Datum mora biti u formatu yyyy-MM-dd", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				List<RoomQuality> selectedQualities = new ArrayList<>();

			    for (Component component : checkBoxPanel.getComponents()) {

			        if (component instanceof JCheckBox) {
			            JCheckBox checkBox = (JCheckBox) component;

			            if (checkBox.isSelected()) {
			                selectedQualities.add(RoomQuality.getFromReadableString(checkBox.getText()));
			            }
			        }
			    }
				
				checkAvailability(LocalDate.parse(start), LocalDate.parse(end), roomType, selectedQualities);
			}
		});
	}
	
	private void checkAvailability(LocalDate start, LocalDate end, RoomType type, List<RoomQuality> selectedQualities) {
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		int roomNumber = roomManager.checkAvailable(start, end, type, selectedQualities);
		if (start.isAfter(end.minusDays(1))) {
			JOptionPane.showMessageDialog(null, "Datum kraja mora biti posle datuma pocetka", "Greska",
					JOptionPane.ERROR_MESSAGE);
		}else if (start.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(null, "Datum pocetka mora biti posle danasnjeg datuma", "Greska",
                    JOptionPane.ERROR_MESSAGE);
		} else if (roomNumber == 0) {
			JOptionPane.showMessageDialog(null, "Nema slobodnih soba za izabrani period", "Greska",
					JOptionPane.ERROR_MESSAGE);
		} else {
			new AddReservationDialog(start, end, roomNumber, table);
			checkIn.setText("");
			checkOut.setText("");
		}
	}

}
