package view.admin;

import java.awt.BorderLayout;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.PriceListManager;
import manage.ReservationManager;
import manage.UserManager;
import model.ReservationStatus;
import net.miginfocom.swing.MigLayout;
import tableModels.AdminReservationModel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
public class AddReservationDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private ReservationManager reservationManager;
	private LocalDate startDate;
	private LocalDate endDate;
	private int RoomNumber;
	
	public AddReservationDialog(LocalDate startDate, LocalDate endDate, int RoomNumber, JTable table) {
		PriceListManager priceListManager = ManagerFactory.getInstance().getPriceListManager();
		reservationManager = ManagerFactory.getInstance().getReservationManager();
		
		this.startDate = startDate;
		this.endDate = endDate;
		this.RoomNumber = RoomNumber;
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][]"));
		
		JLabel lblNewLabel = new JLabel("Korisnicko ime");
		contentPanel.add(lblNewLabel, "cell 0 0");
		
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

		
		List<String> services = priceListManager.getAllServices();
		
		for (String service : services) {
		    JCheckBox checkBox = new JCheckBox(service);
		    checkBoxPanel.add(checkBox);
		}

		JScrollPane scrollPane = new JScrollPane(checkBoxPanel);
		contentPanel.add(scrollPane, "cell 0 2,growx");
		textField = new JTextField();
		contentPanel.add(textField, "cell 0 1,growx");
		textField.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Dodaj");
				okButton.setActionCommand("OK");
				okButton.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent e) {
				    	String username = textField.getText();
				    	List<String> selectedServices = new ArrayList<>();
				    	
				    	for (Component component : checkBoxPanel.getComponents()) {
				            if (component instanceof JCheckBox) {
				                JCheckBox checkBox = (JCheckBox) component;

				                if (checkBox.isSelected()) {
				                    selectedServices.add(checkBox.getText());
				                }
				            }
				        }
				    	
						if (textField.getText().isEmpty()) {
							JOptionPane.showMessageDialog(null, "Morate uneti korisnicko ime", "Greska", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						if (username.contains(",")) {
					        JOptionPane.showMessageDialog(null, "Zarez nije dozvoljen karakter", "Greska", JOptionPane.ERROR_MESSAGE);
					        return;
					    }
						
						UserManager userManager = ManagerFactory.getInstance().getUserManager();
						if (!userManager.checkIfUserExists(username)) {
							JOptionPane.showMessageDialog(null, "Korisnik ne postoji", "Greska", JOptionPane.ERROR_MESSAGE );
							return;
						}
						
						((AdminReservationModel) table.getModel()).fireTableDataChanged();
						addReservation(username, selectedServices);
						dispose();
				    }
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Otkazi");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	}
	
	private void addReservation(String username, List<String> services) {
		
		reservationManager.addReservation(this.RoomNumber, username, this.startDate, this.endDate, Double.parseDouble("0"), ReservationStatus.CONFIRMED, services, LocalDate.now(), null);
		
		JOptionPane.showMessageDialog(null, "Uspesno dodata rezervacija");
	}

}
