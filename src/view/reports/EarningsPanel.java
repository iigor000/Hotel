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
import manage.UserManager;
import net.miginfocom.swing.MigLayout;

public class EarningsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField startField;
	private JTextField endField;

	public EarningsPanel() {
		setLayout(new MigLayout("", "[grow][grow][]", "[][][][][][]"));
		
		JLabel titlelbl = new JLabel("Prihod");
		titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(titlelbl, "cell 0 0 3 1,alignx center");
		
		JLabel stratlbl = new JLabel("Datum pocetka");
		add(stratlbl, "cell 0 1");
		
		JLabel endlbl = new JLabel("Datum kraja");
		add(endlbl, "cell 1 1");
		
		JLabel earinnglbl = new JLabel("Ukupan prihod:");
		earinnglbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(earinnglbl, "cell 0 3");
		
		JLabel earningNumberlbl = new JLabel("");
		earningNumberlbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(earningNumberlbl, "cell 2 3");
		
		startField = new JTextField();
		add(startField, "cell 0 2,growx,gapright 10,hmin 30");
		startField.setColumns(10);
		
		endField = new JTextField();
		add(endField, "cell 1 2,growx,gapright 10,hmin 30");
		endField.setColumns(10);
		
		JLabel spendingslbl = new JLabel("Ukupan rashod:");
		spendingslbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(spendingslbl, "cell 0 4");
		
		JLabel spendingNumberlbl = new JLabel("");
		spendingNumberlbl.setFont(new Font("Tahoma", Font.PLAIN, 20));
		add(spendingNumberlbl, "cell 2 4");
		
		JButton earningbtn = new JButton("Izracunaj");
		
		earningbtn.addActionListener(new ActionListener() {
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

				double earning = ManagerFactory.getInstance().getReservationManager()
						.getEarnings(LocalDate.parse(start), LocalDate.parse(end));
				earningNumberlbl.setText(String.valueOf(earning) + " EUR");
				
				UserManager userManager = ManagerFactory.getInstance().getUserManager();
				int spening = userManager.calculateSpendings(LocalDate.parse(start), LocalDate.parse(end));
				spendingNumberlbl.setText(String.valueOf(spening) + " EUR");
			}
		});
		
		add(earningbtn, "cell 2 2,hmin 30");
		
	}

}
