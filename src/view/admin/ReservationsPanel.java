package view.admin;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;
import tableModels.AdminReservationModel;

public class ReservationsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public ReservationsPanel(JTable table) {
		setLayout(new MigLayout("", "[grow]", "[][grow][]"));
		
		JLabel titlelbl = new JLabel("Rezervacije");
		titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(titlelbl, "cell 0 0,alignx center");
		
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, "cell 0 1,grow");
		
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		Dimension buttonSize = new Dimension(150, 50);
		JButton editRoombtn = new JButton("Izmeni sobu");
		editRoombtn.setMinimumSize(buttonSize);
		editRoombtn.setMaximumSize(buttonSize);
		editRoombtn.setPreferredSize(buttonSize);
		JButton editbtn = new JButton("Izmeni");
		editbtn.setMinimumSize(buttonSize);
		editbtn.setMaximumSize(buttonSize);
		editbtn.setPreferredSize(buttonSize);
		JButton deletebtn = new JButton("Obrisi");
		deletebtn.setMinimumSize(buttonSize);
		deletebtn.setMaximumSize(buttonSize);
		deletebtn.setPreferredSize(buttonSize);
		
		editbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = table.convertRowIndexToModel(selectedRow);
					AdminReservationModel model = (AdminReservationModel) table.getModel();
					new EditReservationDialog(model.getReservation(modelRow), model);
				}
			}
		});
		
		deletebtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = table.convertRowIndexToModel(selectedRow);
					AdminReservationModel model = (AdminReservationModel) table.getModel();
					model.deleteReservation(modelRow);
				}
			}
		});
		
		editRoombtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = table.convertRowIndexToModel(selectedRow);
					AdminReservationModel model = (AdminReservationModel) table.getModel();
					new EditReservationRoomDialog(model.getReservation(modelRow), model);
				}
			}
		});

		buttonPanel.add(editbtn);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(editRoombtn);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(deletebtn);

		add(buttonPanel, "cell 0 2,growx");
	}

}
