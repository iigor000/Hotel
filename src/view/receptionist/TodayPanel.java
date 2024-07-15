package view.receptionist;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;
import tableModels.CheckInModel;
import tableModels.CheckOutModel;

public class TodayPanel extends JPanel {

	private static final long serialVersionUID = 1L;


	public TodayPanel(JTable checkInTable, JTable checkOutTable) {
		setLayout(new MigLayout("", "[grow]", "[][grow][][grow]"));
		
		JLabel checkInlbl = new JLabel("Danasnji ulazi");
		checkInlbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(checkInlbl, "cell 0 0,alignx center");
		
		JScrollPane checkInPane = new JScrollPane();
		
		checkInPane.setViewportView(checkInTable);
		add(checkInPane, "cell 0 1,grow");
		
		Dimension buttonSize = new Dimension(150, 50);
		JButton checkInbtn = new JButton("Prijavi");
		checkInbtn.setMinimumSize(buttonSize);
		checkInbtn.setMaximumSize(buttonSize);
		checkInbtn.setPreferredSize(buttonSize);
		
		checkInbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = checkInTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = checkInTable.convertRowIndexToModel(selectedRow);
					CheckInModel model = (CheckInModel) checkInTable.getModel();
					new CheckInDialog(model.getReservation(modelRow), model, (CheckOutModel) checkOutTable.getModel());
					
				}
			}
		});
		
		add(checkInbtn, "cell 0 2,alignx center");
		
		JLabel checkOutlbl = new JLabel("Danasnji izlazi");
		checkOutlbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(checkOutlbl, "cell 0 3,alignx center");
		
		JScrollPane checkOutPane = new JScrollPane();
		add(checkOutPane, "cell 0 4,grow");
		
		JButton checkOutbtn = new JButton("Odjavi");
		checkOutbtn.setMinimumSize(buttonSize);
		checkOutbtn.setMaximumSize(buttonSize);
		checkOutbtn.setPreferredSize(buttonSize);
		
		checkOutbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = checkOutTable.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = checkOutTable.convertRowIndexToModel(selectedRow);
					CheckOutModel model = (CheckOutModel) checkOutTable.getModel();
					boolean success = model.CheckOut(modelRow);
					
					if (success) {
					JOptionPane.showMessageDialog(null, "Odjava uspesna", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Odjava nije uspela", "Greska", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		add(checkOutbtn, "cell 0 5,alignx center");
		
		checkOutPane.setViewportView(checkOutTable);
	}

}
