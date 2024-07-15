package view.cleaner;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import model.Cleaner;
import net.miginfocom.swing.MigLayout;
import view.ChangePasswordPanel;
import view.EditDataPanel;
import view.LoginView;

public class CleanerView extends JFrame {

	private static final long serialVersionUID = 1289411288872384102L;


	public CleanerView(Cleaner cleaner) {
		setTitle(cleaner.getName() + " " + cleaner.getLastName());
        setSize(1000, 800);
        
        setTitle(cleaner.getName() + " " + cleaner.getLastName());
        
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
        
        tabbedPane.addTab("Sobe za ciscenje", null, new RoomsPanel(cleaner), null);
        
        tabbedPane.addTab("Izmeni Podatke", null, new EditDataPanel(cleaner), null);
       
        tabbedPane.addTab("Izmeni Lozinku", null, new ChangePasswordPanel(cleaner), null);
        
        setLocationRelativeTo(null);
        
        setVisible(true);
	}
}
