package view.guest;

import model.Guest;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableRowSorter;

import net.miginfocom.swing.MigLayout;
import tableModels.GuestRequestsModel;
import tableModels.ReservationGuestModel;
import view.ChangePasswordPanel;
import view.EditDataPanel;
import view.LoginView;

public class GuestView extends JFrame {
	private static final long serialVersionUID = 9132891427744028870L;
    private JTable table;
	protected TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();

    public GuestView(Guest guest) {
        setTitle(guest.getName() + " " + guest.getLastName());
        setSize(1000, 800);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(new MigLayout("", "[grow]", "[grow]"));
        
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
        getContentPane().add(tabbedPane, "cell 0 0,grow");
        
        this.table = new JTable(new ReservationGuestModel(guest));
        JLabel total = new JLabel();
        JTable requests = new JTable(new GuestRequestsModel(guest));
        
        tabbedPane.addTab("Rezervacije", null, new ReservationsPanel(guest, table, total, requests), null);
        
        tabbedPane.addTab("Rezervisi sobu", null, new AddReservationPanel(guest, requests, total), null);
        
        tabbedPane.addTab("Izmeni Podatke", null, new EditDataPanel(guest), null);
        
        tabbedPane.addTab("Izmeni Lozinku", null, new ChangePasswordPanel(guest), null);
        
        setLocationRelativeTo(null);
        
        setVisible(true);
    }
	
   
}