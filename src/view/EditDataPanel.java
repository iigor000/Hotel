package view;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.ReservationManager;
import model.Reservation;
import model.User;
import net.miginfocom.swing.MigLayout;

public class EditDataPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField nameField;
	private JTextField lastNameField;
	private JTextField usernameField;
	private JTextField addressField;
	private JTextField dateField;
	private JTextField phoneFIeld;
	private User user;
	
	public EditDataPanel(User user) {
		this.user = user;
		
		setLayout(new MigLayout("", "[grow][][grow]", "[][][][][][][][][][][]"));
		setSize(700, 400);
        
        JLabel titlelbl = new JLabel("Promenite podatke");
        titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
        titlelbl.setBorder(new EmptyBorder(10, 0, 10, 0));
        titlelbl.setHorizontalAlignment(SwingConstants.CENTER);
        add(titlelbl, "cell 0 0 4 1,alignx center");
        
        JLabel namelbl = new JLabel("Ime");
        add(namelbl, "cell 0 1");
        
        JLabel addresslbl = new JLabel("Adresa");
        add(addresslbl, "cell 2 1,alignx right");
        
        nameField = new JTextField(user.getName());
        add(nameField, "cell 0 2,growx,gapright 10,height 30::");
        nameField.setColumns(10);
        
        addressField = new JTextField(user.getAdress());
        add(addressField, "cell 2 2,growx,gapx 10,height 30::");
        addressField.setColumns(10);
        
        JLabel lastNamelbl = new JLabel("Prezime");
        add(lastNamelbl, "cell 0 3");
        
        JLabel birthDatelbl = new JLabel("Datum rodjenja");
        birthDatelbl.setHorizontalAlignment(SwingConstants.RIGHT);
        add(birthDatelbl, "cell 2 3,alignx right");
        
        lastNameField = new JTextField(user.getLastName());
        add(lastNameField, "cell 0 4,growx,gapright 10,height 30::");
        lastNameField.setColumns(10);
        
        dateField = new JTextField(user.getBirthDate().toString());
        add(dateField, "cell 2 4,growx,gapx 10,height 30::");
        dateField.setColumns(10);
        
        JLabel usernamelbl = new JLabel("Korisnicko ime");
        add(usernamelbl, "cell 0 5");
        
        JLabel numberlbl = new JLabel("Broj telefona");
        add(numberlbl, "cell 2 5,alignx right");
        
        usernameField = new JTextField(user.getUsername());
        add(usernameField, "cell 0 6,growx,gapright 10,height 30::");
        usernameField.setColumns(10);
        
        phoneFIeld = new JTextField(user.getPhone());
        add(phoneFIeld, "cell 2 6,growx,gapx 10,hmin 30");
        phoneFIeld.setColumns(10);
        
        JLabel genderlbl = new JLabel("Rod");
        add(genderlbl, "cell 1 7,alignx center");
        
        String[] genders = {"Muski", "Zenski"};
        JComboBox<String> comboBox = new JComboBox<String>(genders);
        add(comboBox, "cell 1 8,growx,hmin 30");
        
        add(Box.createVerticalStrut(30), "cell 1 8");
        JButton savebtn = new JButton("Sacuvaj");
        
        savebtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String lastName = lastNameField.getText();
                String username = usernameField.getText();
                String address = addressField.getText();
                String date = dateField.getText();
                String phone = phoneFIeld.getText();
                String gender = (String) comboBox.getSelectedItem();
                boolean isMale = "Muski".equals(gender);
                
                if (name.isEmpty() || lastName.isEmpty() || username.isEmpty() || address.isEmpty() || date.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Ne smeju biti prazna polja", "Greska", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
				if (username.contains(",") || name.contains(",") || lastName.contains(",") || address.contains(",") || phone.contains(",")) {
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
                
                saveChanges(username, name, lastName, address, date, phone, isMale);
            }
        });
        
        add(savebtn, "cell 1 10,height 40::,width 150::");
	}
	
	private void saveChanges(String username, String name, String lastName, String address, String date, String phone, boolean gender) {
		if (!username.equals(user.getUsername())) {
        	ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
        	List<Reservation> reservations = reservationManager.getReservationsByUsername(user.getUsername());
        	for (Reservation reservation : reservations) {
        		reservation.setGuestUsername(username);
        	}
        }

        user.setName(name);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setAdress(address);
        user.setBirthDate(LocalDate.parse(date));
        user.setPhone(phone);
        user.setGender(gender);
        
        JOptionPane.showMessageDialog(null, "Podaci su usepsno promenjeni", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
	}

}
