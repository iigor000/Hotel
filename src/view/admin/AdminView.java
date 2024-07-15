package view.admin;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import model.Administrator;

import javax.swing.JTabbedPane;
import javax.swing.JTable;

import net.miginfocom.swing.MigLayout;
import tableModels.AdminReservationModel;
import tableModels.EmployeesModel;
import tableModels.GuestModel;
import tableModels.PriceListsModel;
import tableModels.ReceptionistRequestModel;
import tableModels.RoomsModel;
import view.ChangePasswordPanel;
import view.EditDataPanel;
import view.LoginView;


public class AdminView extends JFrame {
	private static final long serialVersionUID = -7745345714270100039L;
	private JPanel contentPane;

	public AdminView(Administrator admin) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 800);
		
		setTitle(admin.getName() + " " + admin.getLastName());
		
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
	
		JTable table = new JTable(new EmployeesModel());
		JTable roomsTable = new JTable(new RoomsModel());
		JTable priceListTable = new JTable(new PriceListsModel());
		JTable reservationsTable = new JTable(new AdminReservationModel());
		JTable guestsTable = new JTable(new GuestModel());
		JTable requestsTable = new JTable(new ReceptionistRequestModel());
		
		tabbedPane.addTab("Zaposleni", null, new EmployeesPanel(table), null);	
		
		tabbedPane.addTab("Dodaj Zaposlenog", null, new AddEmployeePanel(table), null);
		
        tabbedPane.addTab("Sobe", null, new RoomsPanel(roomsTable), null);
        
		tabbedPane.addTab("Cenovnik", null, new PriceListsPanel(priceListTable), null);
		
		tabbedPane.addTab("Izvestaji", null, new ReportsPanel(), null);
		
		tabbedPane.addTab("Zahtevi", null, new RequestsPanel(requestsTable, reservationsTable), null);
		
		tabbedPane.addTab("Rezervacije", null, new ReservationsPanel(reservationsTable), null);
		
		tabbedPane.addTab("Dodaj Rezervaciju", null, new AddReservationPanel(reservationsTable), null);
		
		tabbedPane.addTab("Gosti", null, new GuestPanel(guestsTable), null);
		
		tabbedPane.addTab("Izmeni informacije", null, new EditDataPanel(admin), null);
		
		tabbedPane.addTab("Izmeni Lozinku", null, new ChangePasswordPanel(admin), null);
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	}


}
