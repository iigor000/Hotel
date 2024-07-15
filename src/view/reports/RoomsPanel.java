package view.reports;

import java.awt.Font;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;
import tableModels.RoomsReportModel;

public class RoomsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField startField;
	private JTextField endField;
	
	public RoomsPanel(JTable roomsTable) {
		setLayout(new MigLayout("", "[grow][grow]", "[][][][][grow]"));
		
		JLabel titlelbl = new JLabel("Broj nocenja i prihod za sobe");
		titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(titlelbl, "cell 0 0 2 1,alignx center");
		
		JLabel startlbl = new JLabel("Datum pocetka");
		add(startlbl, "cell 0 1");
		
		JLabel endlbl = new JLabel("Datum kraja");
		add(endlbl, "cell 1 1,alignx right");
		
		startField = new JTextField();
		add(startField, "cell 0 2,growx,gapright 10,hmin 30");
		startField.setColumns(10);
		
		endField = new JTextField();
		add(endField, "cell 1 2,growx,gapx 10,hmin 30");
		endField.setColumns(10);
		
		JButton showbtn = new JButton("Prikazi");
		
		showbtn.addActionListener(e -> {
			String start = startField.getText();
			String end = endField.getText();

			if (start.isEmpty() || end.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Morate uneti oba datuma", "Greska", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (LocalDate.parse(start).isAfter(LocalDate.parse(end))) {
				JOptionPane.showMessageDialog(this, "Datum kraja mora biti posle datuma pocetka", "Greska",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if (LocalDate.parse(end).isAfter(LocalDate.now())) {
				JOptionPane.showMessageDialog(this, "Datum ne moze biti u buducnosti", "Greska",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			try {
				LocalDate.parse(start);
				LocalDate.parse(end);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, "Datum mora biti u formatu yyy-MM-dd", "Greska",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			roomsTable.setModel(new RoomsReportModel(LocalDate.parse(start), LocalDate.parse(end)));

		});
		
		add(showbtn, "cell 0 3 2 1,wmin 120,alignx center,hmin 40");
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, "cell 0 4 2 1,grow");
		
		scrollPane.setViewportView(roomsTable);
		
	}

}
