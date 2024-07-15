package view.reports;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class ReportsView extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	
	public ReportsView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "cell 0 0,grow");
		
		JTable table = new JTable();
		JTable roomsTable = new JTable();
		
		tabbedPane.addTab("Cistaci", null, new CleanersPanel(table), null);
		
		tabbedPane.addTab("Grafici za cistace", null, new CleanerGraphPanel(), null);
		
		tabbedPane.addTab("Rezervacije", null, new ReservationsPanel(), null);
	
		tabbedPane.addTab("Grafik za rezervacije", null, new ReservationGraphPanel(), null);
		
		tabbedPane.addTab("Sobe", null, new RoomsPanel(roomsTable), null);
		
		tabbedPane.addTab("Prihod", null, new EarningsPanel(), null);
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	}

}
