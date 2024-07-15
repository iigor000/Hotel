package view.admin;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.UserManager;
import model.EmployeeType;
import net.miginfocom.swing.MigLayout;
import tableModels.EmployeesModel;

public class AddEmployeePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField lastNameField;
	private JTextField usernameField;
	private JTextField addressField;
	private JTextField dateField;
	private JTextField passwordField;
	private JTextField phoneFIeld;
	private JTextField yearsWorkingField;
	private JTable table;
	
	public AddEmployeePanel(JTable table) {
		this.table = table;
		setLayout(new MigLayout("", "[grow][][grow]", "[][][][][][][][][][][][]"));
		
		JLabel titlelbl = new JLabel("Dodajte zaposlenog");
        titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
        titlelbl.setBorder(new EmptyBorder(10, 0, 10, 0));
        titlelbl.setHorizontalAlignment(SwingConstants.CENTER);
        add(titlelbl, "cell 0 0 4 1,alignx center");
        
        JLabel namelbl = new JLabel("Ime");
        add(namelbl, "cell 0 1");
        
        JLabel addresslbl = new JLabel("Adresa");
        add(addresslbl, "cell 2 1,alignx right,aligny baseline");
        
        nameField = new JTextField();
        add(nameField, "cell 0 2,growx,gapright 10,height 30::");
        nameField.setColumns(10);
        
        addressField = new JTextField();
        add(addressField, "cell 2 2,growx,gapx 10,height 30::");
        addressField.setColumns(10);
        
        JLabel lastNamelbl = new JLabel("Prezime");
        add(lastNamelbl, "cell 0 3,alignx left");
        
        JLabel birthDatelbl = new JLabel("Datum rodjenja");
        birthDatelbl.setHorizontalAlignment(SwingConstants.RIGHT);
        add(birthDatelbl, "cell 2 3,alignx right");
        
        lastNameField = new JTextField();
        add(lastNameField, "cell 0 4,growx,gapright 10,height 30::");
        lastNameField.setColumns(10);
        
        dateField = new JTextField();
        add(dateField, "cell 2 4,growx,gapx 10,height 30::");
        dateField.setColumns(10);
        
        JLabel usernamelbl = new JLabel("Korisnicko ime");
        add(usernamelbl, "cell 0 5");
        
        JLabel passwordlbl = new JLabel("Lozinka");
        add(passwordlbl, "cell 2 5,alignx right");
        
        usernameField = new JTextField();
        add(usernameField, "cell 0 6,growx,gapright 10,height 30::");
        usernameField.setColumns(10);
        
        passwordField = new JTextField();
        add(passwordField, "cell 2 6,growx,gapx 10,height 30::");
        passwordField.setColumns(10);
        
        JLabel genderlbl = new JLabel("Rod");
        add(genderlbl, "cell 0 7");
        
        JLabel numberlbl = new JLabel("Broj telefona");
        add(numberlbl, "cell 2 7,alignx right");
        
        JComboBox<String> comboBox = new JComboBox<String>();
        comboBox.setModel(new DefaultComboBoxModel<String>(new String[] {"Muski", "Zenski"}));
        add(comboBox, "cell 0 8,growx,gapright 10,height 30::");
        
        phoneFIeld = new JTextField();
        add(phoneFIeld, "cell 2 8,growx,gapx 10,height 30::");
        phoneFIeld.setColumns(10);
        
        EmployeeType[] employeeTypes = {EmployeeType.CLEANER, EmployeeType.RECEPTIONIST};
        
        JLabel typelbl = new JLabel("Uloga");
        add(typelbl, "cell 0 9");
        
        JLabel yearsWorkinglbl = new JLabel("Radni staz");
        add(yearsWorkinglbl, "cell 2 9,alignx right");
        JComboBox<EmployeeType> employeeTypeComboBox = new JComboBox<>(employeeTypes);

        add(employeeTypeComboBox, "cell 0 10,growx,gapright 10,height 30::");
        
        yearsWorkingField = new JTextField();
        add(yearsWorkingField, "cell 2 10,growx,gapx 10,hmin 30");
        yearsWorkingField.setColumns(10);
        
        JLabel qualificationlbl = new JLabel("Kvalifikacija");
        add(qualificationlbl, "flowx,cell 1 11,alignx center");
        
        JComboBox<Integer> qualificationBox = new JComboBox<>();

        for (int i = 1; i <= 7; i++) {
        	qualificationBox.addItem(i);
        }

        add(qualificationBox, "cell 1 12,growx,hmin 30");
        
        JButton savebtn = new JButton("Dodaj");
        
        savebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String lastName = lastNameField.getText();
                String username = usernameField.getText();
                String address = addressField.getText();
                String date = dateField.getText();
                String password = passwordField.getText();
                String phone = phoneFIeld.getText();
                String gender = (String) comboBox.getSelectedItem();
                String yearsWorking = yearsWorkingField.getText();
                int years = Integer.parseInt(yearsWorking);
                int qualification = (int) qualificationBox.getSelectedItem();
                EmployeeType employeeType = (EmployeeType) employeeTypeComboBox.getSelectedItem();
                boolean isMale = "Muski".equals(gender);
                
                if (name.isEmpty() || lastName.isEmpty() || username.isEmpty() || address.isEmpty() || date.isEmpty() || password.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ne smeju biti prazna polja", "Greska", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
				if (name.contains(",") || lastName.contains(",") || username.contains(",") || address.contains(",")
						|| date.contains(",") || password.contains(",") || phone.contains(",")) {
					JOptionPane.showMessageDialog(null, "Zarez nije dozvoljen karakter", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

                if (!phone.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Telefon moze imati samo brojeve", "Greska", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (!yearsWorking.matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Radni staz moze imati samo brojeve", "Greska", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    LocalDate.parse(date);
                } catch (DateTimeParseException ex) {
                    JOptionPane.showMessageDialog(null, "Datum mora biti u formatu yyyy-MM-dd", "Greska", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                saveEmployee(username, name, lastName, address, date, password, phone, isMale, years, qualification, employeeType);
            }
        });
		
        
        add(savebtn, "cell 1 13,wmin 150,hmin 40");
	}
	
	private void saveEmployee(String username, String name, String lastName, String address, String date, String password, String phone, boolean gender, int yearsWorking, int qualification, EmployeeType employeeType) {

        UserManager userManager = ManagerFactory.getInstance().getUserManager();
        
        if (userManager.checkIfUserExists(username)){
        	JOptionPane.showMessageDialog(null, "Korisnik sa tim korisnickim imenom vec postoji", "Greska", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
        userManager.addUser(name, lastName, gender, LocalDate.parse(date), phone, address, username, password, 0, qualification, yearsWorking, employeeType);
        
		JOptionPane.showMessageDialog(null, "Korisnik je dodat", "Uspeh", JOptionPane.INFORMATION_MESSAGE);

		((EmployeesModel) table.getModel()).fireTableDataChanged();
	}

}
