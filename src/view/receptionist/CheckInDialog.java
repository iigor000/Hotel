package view.receptionist;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.PriceListManager;
import manage.ReservationManager;
import model.Reservation;
import net.miginfocom.swing.MigLayout;
import tableModels.CheckInModel;
import tableModels.CheckOutModel;

public class CheckInDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private PriceListManager priceListManager;
	private ReservationManager reservationManager;

	public CheckInDialog(Reservation reservation, CheckInModel checkInModel, CheckOutModel checkOutModel) {
		this.priceListManager = ManagerFactory.getInstance().getPriceListManager();
		this.reservationManager = ManagerFactory.getInstance().getReservationManager();
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][]"));

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
			JLabel lblNewLabel = new JLabel("Izaberite usluge");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
			contentPanel.add(lblNewLabel, "cell 0 0,alignx center");
		}

		JScrollPane scrollPane = new JScrollPane(checkBoxPanel);
		contentPanel.add(scrollPane, "cell 0 2,growx");
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(e -> {
					List<String> selectedServices = new ArrayList<String>();
					for (int i = 0; i < checkBoxPanel.getComponentCount(); i++) {
						JCheckBox checkBox = (JCheckBox) checkBoxPanel.getComponent(i);
						if (checkBox.isSelected()) {
							selectedServices.add(checkBox.getText());
						}
					}
					boolean succesful = reservationManager.checkIn(reservation);
					if (succesful) {
						reservation.setServices(selectedServices);
						dispose();
					}else {
						JOptionPane.showMessageDialog(null, "Ne moze se treunutno izvrsiti prijava. Pokusajte kasnije.", "Greska", JOptionPane.ERROR_MESSAGE);
						dispose();
					}
					
					checkInModel.fireTableDataChanged();
					checkOutModel.fireTableDataChanged();
					
					dispose();
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	}

}
