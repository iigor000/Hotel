package view.reports;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import manage.ManagerFactory;
import manage.ReservationManager;
import model.ReservationStatus;
import net.miginfocom.swing.MigLayout;

public class ReservationsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField startField;
	private JTextField endField;

	public ReservationsPanel() {
		ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
		
		setLayout(new MigLayout("", "[grow][grow][]", "[][][][][][]"));
		
		JLabel titlelbl = new JLabel("Izvestaji za rezervacije");
		titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(titlelbl, "cell 0 0 3 1,alignx center");
		
		JLabel startlbl = new JLabel("Datum pocetka");
		add(startlbl, "cell 0 1");
		
		JLabel endlbl = new JLabel("Datuk kraja");
		add(endlbl, "cell 1 1");
		
		JButton showbtn = new JButton("Prikazi");
		
		startField = new JTextField();
		add(startField, "cell 0 2,growx,gapright 10,hmin 30");
		startField.setColumns(10);
		
		endField = new JTextField();
		add(endField, "cell 1 2,growx,gapright 10,hmin 30");
		endField.setColumns(10);
		
		JLabel approvedlbl = new JLabel("Ukuno odobrenih:");
		approvedlbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(approvedlbl, "cell 0 3");
		
		JLabel approvednumlbl = new JLabel("");
		approvednumlbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(approvednumlbl, "cell 1 3");
		
		JLabel rejectedlbl = new JLabel("Ukupno odbijenih:");
		rejectedlbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(rejectedlbl, "cell 0 4");
		
		JLabel rejectednumlbl = new JLabel("");
		rejectednumlbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(rejectednumlbl, "cell 1 4");
		
		JLabel canceledlbl = new JLabel("Ukupno otkazanih:");
		canceledlbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(canceledlbl, "cell 0 5");
		
		JLabel cancelednumlbl = new JLabel("");
		cancelednumlbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(cancelednumlbl, "cell 1 5");
		
		showbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String start = startField.getText();
				String end = endField.getText();

				if (start.isEmpty() || end.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Morate uneti oba datuma", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					LocalDate.parse(start);
					LocalDate.parse(end);
				} catch (DateTimeParseException ex) {
					JOptionPane.showMessageDialog(null, "Datum mora biti u formatu yyyy-MM-dd", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (LocalDate.parse(start).isAfter(LocalDate.parse(end))) {
					JOptionPane.showMessageDialog(null, "Datum pocetka mora biti pre datuma kraja", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (LocalDate.parse(end).isAfter(LocalDate.now())) {
					JOptionPane.showMessageDialog(null, "Datum kraja mora biti pre danasnjeg datuma", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				int approved = reservationManager
						.getNumberOfReservationsByAction(LocalDate.parse(start), LocalDate.parse(end), ReservationStatus.CONFIRMED);
				int rejected = reservationManager
						.getNumberOfReservationsByAction(LocalDate.parse(start), LocalDate.parse(end), ReservationStatus.REJECTED);
				int canceled = reservationManager
						.getNumberOfReservationsByAction(LocalDate.parse(start), LocalDate.parse(end), ReservationStatus.CANCELED);
				
				approvednumlbl.setText(String.valueOf(approved));
				rejectednumlbl.setText(String.valueOf(rejected));
				cancelednumlbl.setText(String.valueOf(canceled));
			}
		});
		
		add(showbtn, "cell 2 1 1 2,wmin 100,hmin 40");
	}

}
