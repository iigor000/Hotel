package view.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.UserManager;
import net.miginfocom.swing.MigLayout;
import tableModels.GuestModel;

public class AddGuestDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JTextField lastNameField;
	private JTextField usernameField;
	private JTextField addressField;
	private JTextField dateField;
	private JTextField passwordField;
	private JTextField phoneFIeld;
	private JTable table;

	public AddGuestDialog(JTable table) {
		this.table = table;
		
		setSize(700, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		contentPanel.setLayout(new MigLayout("", "[grow][][grow]", "[][][][][][][][][][]"));
		
		JLabel titlelbl = new JLabel("Dodajte novog gosta");
        titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
        titlelbl.setBorder(new EmptyBorder(10, 0, 10, 0));
        titlelbl.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titlelbl, "cell 0 0 4 1,alignx center");
        
        JLabel namelbl = new JLabel("Ime");
        contentPanel.add(namelbl, "cell 0 1");
        
        JLabel addresslbl = new JLabel("Adresa");
        contentPanel.add(addresslbl, "cell 2 1,alignx right,aligny baseline");
        
        nameField = new JTextField();
        contentPanel.add(nameField, "cell 0 2,growx,gapright 10,height 30::");
        nameField.setColumns(10);
        
        addressField = new JTextField();
        contentPanel.add(addressField, "cell 2 2,growx,gapx 10,height 30::");
        addressField.setColumns(10);
        
        JLabel lastNamelbl = new JLabel("Prezime");
        contentPanel.add(lastNamelbl, "cell 0 3,alignx left");
        
        JLabel birthDatelbl = new JLabel("Datum rodjenja");
        birthDatelbl.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPanel.add(birthDatelbl, "cell 2 3,alignx right");
        
        lastNameField = new JTextField();
        contentPanel.add(lastNameField, "cell 0 4,growx,gapright 10,height 30::");
        lastNameField.setColumns(10);
        
        dateField = new JTextField();
        contentPanel.add(dateField, "cell 2 4,growx,gapx 10,height 30::");
        dateField.setColumns(10);
        
        JLabel emaillbl = new JLabel("Email");
        contentPanel.add(emaillbl, "cell 0 5");
        
        JLabel jmbglbl = new JLabel("JMBG");
        contentPanel.add(jmbglbl, "cell 2 5,alignx right");
        
        usernameField = new JTextField();
        contentPanel.add(usernameField, "cell 0 6,growx,gapright 10,height 30::");
        usernameField.setColumns(10);
        
        passwordField = new JTextField();
        contentPanel.add(passwordField, "cell 2 6,growx,gapx 10,height 30::");
        passwordField.setColumns(10);
        
        JLabel genderlbl = new JLabel("Rod");
        contentPanel.add(genderlbl, "cell 0 7");
        
        JLabel numberlbl = new JLabel("Broj telefona");
        contentPanel.add(numberlbl, "cell 2 7,alignx right");
        
        
        String[] genders = {"Muski", "Zenski"};
        JComboBox<String> comboBox = new JComboBox<String>(genders);
        contentPanel.add(comboBox, "cell 0 8,growx,gapright 10,height 30::");
        
        phoneFIeld = new JTextField();
        contentPanel.add(phoneFIeld, "cell 2 8,growx,gapx 10,height 30::");
        phoneFIeld.setColumns(10);
        
        
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				
				okButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                    	 String name = nameField.getText();
                         String lastName = lastNameField.getText();
                         String username = usernameField.getText();
                         String address = addressField.getText();
                         String date = dateField.getText();
                         String password = passwordField.getText();
                         String phone = phoneFIeld.getText();
                         String gender = (String) comboBox.getSelectedItem();
                         boolean isMale = "Muski".equals(gender);
                         
                         if (name.isEmpty() || lastName.isEmpty() || username.isEmpty() || address.isEmpty() || date.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                             JOptionPane.showMessageDialog(null, "Ne smeju biti prazna polja", "Greska", JOptionPane.ERROR_MESSAGE);
                             return;
                         }
                         
         				if (username.contains(",") || name.contains(",") || lastName.contains(",") || address.contains(",") || date.contains(",") || password.contains(",") || phone.contains(",")) {
         					JOptionPane.showMessageDialog(null, "Zarez nije dozvoljen karakter", "Greska",
         							JOptionPane.ERROR_MESSAGE);
         					return;
         				}

                         if (!phone.matches("\\d+")) {
                             JOptionPane.showMessageDialog(null, "Telefon moze imati samo brojeve", "Greska", JOptionPane.ERROR_MESSAGE);
                             return;
                         }

                         try {
                             LocalDate.parse(date);
                         } catch (DateTimeParseException ex) {
                             JOptionPane.showMessageDialog(null, "Datum mora biti u formatu yyyy-MM-dd", "Greska", JOptionPane.ERROR_MESSAGE);
                             return;
                         }
                         
                         saveGuest(username, name, lastName, address, date, password, phone, isMale);
                     }
                 });
				
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		setLocationRelativeTo(null);
		
		setVisible(true);
	}
	
	
	private void saveGuest(String username, String name, String lastName, String address, String date, String password, String phone, boolean gender) {

        UserManager userManager = ManagerFactory.getInstance().getUserManager();
        
        if ( userManager.checkIfUserExists(username)){
        	JOptionPane.showMessageDialog(null, "Korisnik sa tim korisnickim imenom vec postoji", "Greska", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
        userManager.addUser(name, lastName, gender, LocalDate.parse(date), phone, address, username, password);
        
        JOptionPane.showMessageDialog(null, "Gost je uspesno dodat123", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
        table.setModel(new GuestModel());
	}

}
