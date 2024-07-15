package view.receptionist;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Receptionist;

import javax.swing.JTabbedPane;

import net.miginfocom.swing.MigLayout;
import tableModels.CheckInModel;
import tableModels.CheckOutModel;
import tableModels.ReceptionistRequestModel;
import tableModels.ReservationReceptionistModel;
import tableModels.RoomsModel;
import view.ChangePasswordPanel;
import view.EditDataPanel;
import view.LoginView;

import javax.swing.JTable;

public class ReceptionistView extends JFrame {
	private static final long serialVersionUID = -3049719349218251954L;
	private JPanel contentPane;

	public ReceptionistView(Receptionist receptionist) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 800);
		
		setTitle(receptionist.getName() + " " + receptionist.getLastName());
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[grow]", "[grow]"));
		
		JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Meni");
        JMenuItem logoutMenuItem = new JMenuItem("Logout");

        logoutMenuItem.addActionListener(e -> {
            new LoginView();
            dispose();
        });

        menu.add(logoutMenuItem);
        menuBar.add(menu);
        setJMenuBar(menuBar);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, "cell 0 0,grow");
		
		JTable table = new JTable(new ReservationReceptionistModel());
		JTable requests = new JTable(new ReceptionistRequestModel());
		JTable checkInTable = new JTable(new CheckInModel());
		JTable checkOutTable = new JTable(new CheckOutModel());
		JTable roomsTable = new JTable(new RoomsModel());
		
		tabbedPane.addTab("Dodaj Korisnika", null, new RegisterPanel(), null);
		
		tabbedPane.addTab("Napravi Rezervaciju", null, new AddReservationPanel(table), null);
		
		tabbedPane.addTab("Zahtevi", null, new RequestsPanel(requests, checkInTable, checkOutTable, table), null);
		
		tabbedPane.addTab("Rezervacije", null, new ReservationsPanel(table, checkInTable, checkOutTable), null);
		
        tabbedPane.addTab("Sobe", null, new RoomsPanel(roomsTable), null);
        
		tabbedPane.addTab("Danasnji promet", null, new TodayPanel(checkInTable, checkOutTable), null);
		
        tabbedPane.addTab("Izmeni informacije", null, new EditDataPanel(receptionist), null);
        
        tabbedPane.addTab("Izmeni Lozinku", null, new ChangePasswordPanel(receptionist), null);
        
        setLocationRelativeTo(null);
     
		setVisible(true);
	}
}
