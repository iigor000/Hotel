package view.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.RoomManager;
import model.Reservation;
import net.miginfocom.swing.MigLayout;
import tableModels.AdminReservationModel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.Font;

public class EditReservationRoomDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField roomField;

	public EditReservationRoomDialog(Reservation reservation, AdminReservationModel tableModel) {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][]"));
		{
			JLabel roomlbl = new JLabel("Soba");
			roomlbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
			contentPanel.add(roomlbl, "cell 0 0");
		}
		{
			roomField = new JTextField(String.valueOf(reservation.getRoomNumber()));
			contentPanel.add(roomField, "cell 0 1,growx,hmin 30");
			roomField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				
				okButton.addActionListener(e -> {
					int roomNumber = Integer.parseInt(roomField.getText());
					RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
					boolean available = roomManager.checkAvailable(roomNumber, reservation.getCheckIn(), reservation.getCheckOut());
					if (available == false) {
						JOptionPane.showMessageDialog(null, "Soba je zauzeta u tom periodu", "Greska", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
					JOptionPane.showMessageDialog(null, "Soba uspesno promenjena", "Uspesno", JOptionPane.INFORMATION_MESSAGE);
					reservation.setRoomNumber(roomNumber);
					tableModel.fireTableDataChanged();
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
		setVisible(true);
	}
}
