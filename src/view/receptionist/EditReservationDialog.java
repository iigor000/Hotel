package view.receptionist;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.PriceListManager;
import manage.RoomManager;
import model.Reservation;
import model.RoomType;
import net.miginfocom.swing.MigLayout;
import tableModels.ReservationReceptionistModel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class EditReservationDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField checkIn;
	private JTextField checkOut;
	private PriceListManager priceListManager;
	private RoomManager roomManager;

	public EditReservationDialog(Reservation reservation, JTable table) {
		
		this.priceListManager = ManagerFactory.getInstance().getPriceListManager();
		this.roomManager = ManagerFactory.getInstance().getRoomManager();
		
		setSize(600, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][][][][][]"));
		{
			JLabel startlbl = new JLabel("Datum Pocetka");
			contentPanel.add(startlbl, "cell 0 0");
		}
		{
			checkIn = new JTextField(reservation.getCheckIn().toString());
			contentPanel.add(checkIn, "cell 0 1,growx,hmin 30");
			checkIn.setColumns(10);
		}
		{
			JLabel endlbl = new JLabel("Datum kraja");
			contentPanel.add(endlbl, "cell 0 2");
		}
		{
			checkOut = new JTextField(reservation.getCheckOut().toString());
			contentPanel.add(checkOut, "cell 0 3,growx,hmin 30");
			checkOut.setColumns(10);
		}
		{
			JLabel typrlbl = new JLabel("Tip sobe");
			contentPanel.add(typrlbl, "cell 0 4");
		}
		
			RoomType[] types = RoomType.values();
			DefaultComboBoxModel<RoomType> roomTypeModel = new DefaultComboBoxModel<RoomType>(types);
			JComboBox<RoomType> type = new JComboBox<RoomType>();
			type.setSelectedItem(roomManager.getRoom(reservation.getRoomNumber()).getType());
			type.setModel(roomTypeModel);
			contentPanel.add(type, "cell 0 5,growx,hmin 30");
		
		
			JPanel checkBoxPanel = new JPanel();
			checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

			
			List<String> services = priceListManager.getAllServices();
			
			for (String service : services) {
			    JCheckBox checkBox = new JCheckBox(service);
				if (reservation.getServices().contains(service)) {
					checkBox.setSelected(true);
				}
			    checkBoxPanel.add(checkBox);
			}
			{
				JLabel servicelbl = new JLabel("Usluge");
				contentPanel.add(servicelbl, "cell 0 6");
			}

			JScrollPane scrollPane = new JScrollPane(checkBoxPanel);
			contentPanel.add(scrollPane, "cell 0 7,growx");
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				    	List<String> selectedServices = new ArrayList<>();
				    	
				    	for (Component component : checkBoxPanel.getComponents()) {
				            if (component instanceof JCheckBox) {
				                JCheckBox checkBox = (JCheckBox) component;

				                if (checkBox.isSelected()) {
				                    selectedServices.add(checkBox.getText());
				                }
				            }
				        }
				    	LocalDate start = LocalDate.parse(checkIn.getText());
				    	LocalDate end = LocalDate.parse(checkOut.getText());
				    	RoomType roomtype = (RoomType) type.getSelectedItem();
				    	
				    	RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
						int roomNumber = roomManager.checkAvailable(start, end, roomtype);
						if (start.isAfter(end)) {
							JOptionPane.showMessageDialog(null, "Datum kraja mora biti posle datuma pocetka", "Greska",
									JOptionPane.ERROR_MESSAGE);
						}else if (start.isBefore(LocalDate.now())) {
				            JOptionPane.showMessageDialog(null, "Datum pocetka mora biti posle danasnjeg datuma", "Greska",
				                    JOptionPane.ERROR_MESSAGE);
						} else if (roomNumber == 0) {
							JOptionPane.showMessageDialog(null, "Nema slobodnih soba za izabrani period", "Greska",
									JOptionPane.ERROR_MESSAGE);
						} else {
							reservation.setCheckIn(start);
							reservation.setCheckOut(end);
							reservation.setRoomNumber(roomNumber);
							reservation.setServices(selectedServices);
							
							((ReservationReceptionistModel) table.getModel()).fireTableDataChanged();
							
							dispose();
						}
						
				    }
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			
			setLocationRelativeTo(null);
			
			setVisible(true);
		}
	}

}