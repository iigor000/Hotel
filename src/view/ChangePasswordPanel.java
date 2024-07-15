package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import manage.UserManager;
import model.User;
import net.miginfocom.swing.MigLayout;

public class ChangePasswordPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JPasswordField oldPass;
	private JPasswordField newPass1;
	private JPasswordField newPass2;
	private User user;
	
	public ChangePasswordPanel(User user) {
		this.user = user;
	
	 	setLayout(new MigLayout("", "[grow]", "[][][][][][][][]"));
        
        JLabel titlelbl = new JLabel("Promenite lozinku");
        titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
        titlelbl.setBorder(new EmptyBorder(10, 0, 10, 0));
        titlelbl.setHorizontalAlignment(SwingConstants.CENTER);
        add(titlelbl, "cell 0 0 4 1,alignx center");
        
        JLabel oldPasslbl = new JLabel("Stara Lozinka");
        add(oldPasslbl, "cell 0 1,alignx center");
        
        oldPass = new JPasswordField();
        add(oldPass, "cell 0 2,wmin 500,alignx center,height 30::");
        
        JLabel newPasslbl = new JLabel("Nova Lozinka");
        add(newPasslbl, "cell 0 3,alignx center");
        
        newPass1 = new JPasswordField();
        add(newPass1, "cell 0 4,wmin 500,alignx center,height 30::");
        
        JLabel repNewPasslbl = new JLabel("Ponovi Novu Lozinku");
        add(repNewPasslbl, "cell 0 5,alignx center");
        
        newPass2 = new JPasswordField();
        add(newPass2, "cell 0 6,wmin 500,alignx center,height 30::");
        
        JButton changePassbtn = new JButton("Promeni");
        
        changePassbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String oldPassstr = new String(oldPass.getPassword());
            	String newPass1str = new String(newPass1.getPassword());
            	String newPass2str = new String(newPass2.getPassword());
            	
				if (!newPass1str.equals(newPass2str)) {
					JOptionPane.showMessageDialog(null, "Lozinke se ne poklapaju", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				if (newPass1str.length() < 8) {
		            JOptionPane.showMessageDialog(null, "Lozinka mora sadrzati bar 8 karaktera", "Greska",
		                JOptionPane.ERROR_MESSAGE);
		            return;
		        }

		        if (!newPass1str.matches(".*\\d.*")) {
		            JOptionPane.showMessageDialog(null, "Lozinka mora sadrzati bar jedan broj", "Greska",
		                JOptionPane.ERROR_MESSAGE);
		            return;
		        }
		        
		        changePassword(oldPassstr, newPass1str);
            }
        });
        
        add(changePassbtn, "cell 0 7,wmin 150,alignx center,hmin 50");
	}
	
    private void changePassword(String oldPass, String newPass) {
		if (!this.user.getPassword().equals(UserManager.hashPassword(oldPass))) {
			JOptionPane.showMessageDialog(null, "Pogresna lozinka", "Greska", JOptionPane.ERROR_MESSAGE);
			return;
		}

		this.user.setPassword(newPass);
		JOptionPane.showMessageDialog(null, "Lozinka je uspesno promenjena", "Uspeh",
				JOptionPane.INFORMATION_MESSAGE);
    }

}
