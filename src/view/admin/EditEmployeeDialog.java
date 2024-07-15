package view.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.UserManager;
import model.Employee;
import model.EmployeeType;
import net.miginfocom.swing.MigLayout;
import tableModels.EmployeesModel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EditEmployeeDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JTextField lastNameField;
	private JTextField usernameField;
	private JTextField addressField;
	private JTextField dateField;
	private JTextField phoneFIeld;
	private JTextField yearsWorkingField;
	private Employee employee;
	private EmployeesModel employeesModel;


	public EditEmployeeDialog(Employee employee, EmployeesModel employeesModel) {
		this.employee = employee;
		this.employeesModel = employeesModel;
		
		setSize(650, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow][][grow]", "[]"));
			JLabel titlelbl = new JLabel("Promenite podatke");
			titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
			contentPanel.add(titlelbl, "cell 0 0,alignx center,spanx 3");
		
		JLabel namelbl = new JLabel("Ime");
		contentPanel.add(namelbl, "cell 0 1");
        
        JLabel addresslbl = new JLabel("Adresa");
        contentPanel.add(addresslbl, "cell 2 1,alignx right,aligny baseline");
        
        nameField = new JTextField(employee.getName());
        contentPanel.add(nameField, "cell 0 2,growx,gapright 10,height 30::");
        nameField.setColumns(10);
        
        addressField = new JTextField(employee.getAdress());
        contentPanel.add(addressField, "cell 2 2,growx,gapx 10,height 30::");
        addressField.setColumns(10);
        
        JLabel lastNamelbl = new JLabel("Prezime");
        contentPanel.add(lastNamelbl, "cell 0 3,alignx left");
        
        JLabel birthDatelbl = new JLabel("Datum rodjenja");
        birthDatelbl.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPanel.add(birthDatelbl, "cell 2 3,alignx right");
        
        lastNameField = new JTextField(employee.getLastName());
        contentPanel.add(lastNameField, "cell 0 4,growx,gapright 10,height 30::");
        lastNameField.setColumns(10);
        
        dateField = new JTextField(employee.getBirthDate().toString());
        contentPanel.add(dateField, "cell 2 4,growx,gapx 10,height 30::");
        dateField.setColumns(10);
        
        JLabel usernamelbl = new JLabel("Korisnicko ime");
        contentPanel.add(usernamelbl, "cell 0 5");
        
        
        usernameField = new JTextField(employee.getUsername());
        contentPanel.add(usernameField, "cell 0 6,growx,gapright 10,height 30::");
        usernameField.setColumns(10);
        
        JLabel qualificationlbl = new JLabel("Kvalifikacija");
        contentPanel.add(qualificationlbl, "flowx,cell 2 5,alignx right");
        
        JComboBox<Integer> qualificationBox = new JComboBox<>();

        for (int i = 1; i <= 7; i++) {
        	qualificationBox.addItem(i);
        }
        
        qualificationBox.setSelectedItem(employee.getQualification());

        contentPanel.add(qualificationBox, "cell 2 6,growx,hmin 30,gapleft 10");
        
        
        JLabel genderlbl = new JLabel("Rod");
        contentPanel.add(genderlbl, "cell 0 7");
        
        JLabel numberlbl = new JLabel("Broj telefona");
        contentPanel.add(numberlbl, "cell 2 7,alignx right");
        
        
        String[] genders = {"Muski", "Zenski"};
        JComboBox<String> comboBox = new JComboBox<>(genders);
        
		if (employee.getGender()) {
			comboBox.setSelectedItem("Muski");
		} else {
			comboBox.setSelectedItem("Zenski");
		}
		
        contentPanel.add(comboBox, "cell 0 8,growx,gapright 10,height 30::");
        
        phoneFIeld = new JTextField(employee.getPhone());
        contentPanel.add(phoneFIeld, "cell 2 8,growx,gapx 10,height 30::");
        phoneFIeld.setColumns(10);
        
        EmployeeType[] employeeTypes = {EmployeeType.CLEANER, EmployeeType.RECEPTIONIST};
        
        JLabel typelbl = new JLabel("Uloga");
        contentPanel.add(typelbl, "cell 0 9");
        
        JLabel yearsWorkinglbl = new JLabel("Radni staz");
        contentPanel.add(yearsWorkinglbl, "cell 2 9,alignx right");
        JComboBox<EmployeeType> employeeTypeComboBox = new JComboBox<>(employeeTypes);
        
        employeeTypeComboBox.setSelectedItem(employee.getEmployeeType());

        contentPanel.add(employeeTypeComboBox, "cell 0 10,growx,gapright 10,height 30::");
        
        yearsWorkingField = new JTextField(Integer.toString(employee.getYearsWorking()));
        contentPanel.add(yearsWorkingField, "cell 2 10,growx,gapx 10,hmin 30");
        yearsWorkingField.setColumns(10);
        
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				
				okButton.addActionListener(new ActionListener() {
					 @Override
			            public void actionPerformed(ActionEvent e) {
			                String name = nameField.getText();
			                String lastName = lastNameField.getText();
			                String username = usernameField.getText();
			                String address = addressField.getText();
			                String date = dateField.getText();
			                String phone = phoneFIeld.getText();
			                String gender = (String) comboBox.getSelectedItem();
			                String yearsWorking = yearsWorkingField.getText();
			                int years = Integer.parseInt(yearsWorking);
			                int qualification = (int) qualificationBox.getSelectedItem();
			                EmployeeType employeeType = (EmployeeType) employeeTypeComboBox.getSelectedItem();
			                boolean isMale = "Muski".equals(gender);
			                
			                if (name.isEmpty() || lastName.isEmpty() || username.isEmpty() || address.isEmpty() || date.isEmpty() || phone.isEmpty()) {
			                    JOptionPane.showMessageDialog(null, "Ne smeju biti prazna polja", "Greska", JOptionPane.ERROR_MESSAGE);
			                    return;
			                }
				                
							if (username.contains(",") || name.contains(",") || lastName.contains(",") || address.contains(",") || date.contains(",") || phone.contains(",")) {
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
			                
			                saveEmployee(username, name, lastName, address, date, phone, isMale, years, qualification, employeeType);
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
	
	private void saveEmployee(String username, String name, String lastName, String address, String date, String phone, boolean isMale, int years, int qualification, EmployeeType employeeType) {
		UserManager userManager = ManagerFactory.getInstance().getUserManager();
        
        if (!username.equals(this.employee.getUsername()) && userManager.checkIfUserExists(username)){
        	JOptionPane.showMessageDialog(null, "Korisnik sa tim korisnickim imenom vec postoji", "Greska", JOptionPane.ERROR_MESSAGE);
        	return;
        }
        
        this.employee.setName(name);
        this.employee.setLastName(lastName);
        this.employee.setUsername(username);
        this.employee.setAdress(address);
        this.employee.setBirthDate(LocalDate.parse(date));
        this.employee.setPhone(phone);
        this.employee.setGender(isMale);
        this.employee.setYearsWorking(years);
        this.employee.setQualification(qualification);
        this.employee.setEmployeeType(employeeType);
        this.employee.setSalary(0);
        
		JOptionPane.showMessageDialog(null, "Korisnik je promenjen", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
		employeesModel.fireTableDataChanged();
		dispose();
    }

}
