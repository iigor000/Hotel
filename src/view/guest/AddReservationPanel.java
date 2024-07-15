package view.guest;

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
import manage.PriceListManager;
import manage.ReservationRequestManager;
import model.Guest;
import model.RoomQuality;
import model.RoomType;
import net.miginfocom.swing.MigLayout;
import tableModels.GuestRequestsModel;

public class AddReservationPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField checkIn;
	private JTextField checkOut;
	
	public AddReservationPanel(Guest guest, JTable table, JLabel total) {
		
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
		
		JPanel checkBoxPanel1 = new JPanel();
		checkBoxPanel1.setLayout(new BoxLayout(checkBoxPanel1, BoxLayout.Y_AXIS));

		PriceListManager priceListManager = ManagerFactory.getInstance().getPriceListManager();
		List<String> services = priceListManager.getAllServices();
		
		for (String service : services) {
		    JCheckBox checkBox = new JCheckBox(service);
		    checkBoxPanel1.add(checkBox);
		}
		
		JScrollPane scrollPane1 = new JScrollPane(checkBoxPanel1);
		add(scrollPane1, "cell 0 7,growx");
		
		
		JButton checkbtn = new JButton("Posalji zahtev");
		add(checkbtn, "cell 0 8 2 1,alignx center,hmin 40");
		
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
				
				LocalDate startDate = LocalDate.parse(start);
				LocalDate endDate = LocalDate.parse(end);
				
				if (startDate.isAfter(endDate.minusDays(1))) {
					JOptionPane.showMessageDialog(null, "Datum kraja mora biti posle datuma pocetka", "Greska",
							JOptionPane.ERROR_MESSAGE);
				}
				if (startDate.isBefore(LocalDate.now())) {
		            JOptionPane.showMessageDialog(null, "Datum pocetka mora biti posle danasnjeg datuma", "Greska",
		                    JOptionPane.ERROR_MESSAGE);
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
			    
			    List<String> selectedServices = new ArrayList<>();
			    
				for (Component component : checkBoxPanel1.getComponents()) {
					if (component instanceof JCheckBox) {
						JCheckBox checkBox = (JCheckBox) component;

						if (checkBox.isSelected()) {
							selectedServices.add(checkBox.getText());
						}
					}
				}
				
				ReservationRequestManager reservationRequestManager = ManagerFactory.getInstance().getReservationRequestManager();
				GuestRequestsModel model = (GuestRequestsModel) table.getModel();
				boolean success = reservationRequestManager.addRequest(startDate, endDate, roomType, selectedQualities, selectedServices, 0, LocalDate.now(), guest.getUsername());
				if (success) {
					JOptionPane.showMessageDialog(null, "Uspesno ste poslali zahtev za rezervaciju", "Uspesno",
							JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Nemoguce je poslati zahtev za rezervaciju", "Greska",
							JOptionPane.ERROR_MESSAGE);
				}
				model.fireTableDataChanged();
			}
		});
    }
	


}
