package view;

import javax.swing.*;

import manage.ManagerFactory;
import manage.UserManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import model.Administrator;
import model.Cleaner;
import model.Guest;
import model.Receptionist;
import model.User;
import view.admin.AdminView;
import view.cleaner.CleanerView;
import view.guest.GuestView;
import view.receptionist.ReceptionistView;
import net.miginfocom.swing.MigLayout;

public class LoginView extends JFrame {
	private static final long serialVersionUID = -5563846534711860861L;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginView() {
        setTitle("Login");
        setSize(400, 220);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        getContentPane().add(panel);
        panel.setLayout(new MigLayout("", "[grow]", "[][][][][][]"));
        
        JLabel usernamelbl = new JLabel("Korisnicko ime");
        panel.add(usernamelbl, "cell 0 0");
        
        usernameField = new JTextField();
        panel.add(usernameField, "cell 0 1,growx,gapx 10 10,hmin 30");
        usernameField.setColumns(10);
        
        JLabel passwordlbl = new JLabel("Lozinka");
        panel.add(passwordlbl, "cell 0 2");
        
        passwordField = new JPasswordField();
        panel.add(passwordField, "cell 0 3,growx,gapx 10 10,hmin 30");
        
        JButton loginbtn = new JButton("Uloguj se");
        
        loginbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	UserManager userManager = ManagerFactory.getInstance().getUserManager();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                User user = userManager.login(username, password);
                if (user != null) {
                    if (user instanceof Guest) {
                    	new GuestView((Guest) user);
					} else if (user instanceof Receptionist) {
						new ReceptionistView((Receptionist) user);
					} else if (user instanceof Administrator) {
						new AdminView((Administrator) user);
					} else if (user instanceof Cleaner){
						new CleanerView((Cleaner) user);
					} else {
						JOptionPane.showMessageDialog(LoginView.this, "Interna greska, pokusajte ponovo", "Greska", JOptionPane.ERROR_MESSAGE);
					}
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginView.this, "Pogresno korisnicko ime ili lozinka", "Greska", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
       
        
        panel.add(loginbtn, "cell 0 5,wmin 100,alignx center,hmin 40");

        setLocationRelativeTo(null);
        
        setVisible(true);
    }
/*
    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Korisnicko ime:");
        userLabel.setBounds(10, 20, 80, 25);
        panel.add(userLabel);

        usernameField = new JTextField(20);
        usernameField.setBounds(100, 20, 260, 25);
        panel.add(usernameField);

        JLabel passwordLabel = new JLabel("Lozinka:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        passwordField = new JPasswordField(20);
        passwordField.setBounds(100, 50, 260, 25);
        panel.add(passwordField);

        loginButton = new JButton("Ulogujte se");
        loginButton.setBounds(150, 90, 100, 30);
        panel.add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	UserManager userManager = ManagerFactory.getInstance().getUserManager();
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                User user = userManager.login(username, password);
                if (user != null) {
                    if (user instanceof Guest) {
                    	new GuestView((Guest) user);
					} else if (user instanceof Receptionist) {
						new ReceptionistView((Receptionist) user);
					} else if (user instanceof Administrator) {
						new AdminView((Administrator) user);
					} else if (user instanceof Cleaner){
						new CleanerView((Cleaner) user);
					} else {
						JOptionPane.showMessageDialog(LoginView.this, "Interna greska, pokusajte ponovo", "Greska", JOptionPane.ERROR_MESSAGE);
					}
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginView.this, "Pogresno korisnicko ime ili lozinka", "Greska", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    } */

}