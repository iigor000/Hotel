package view.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

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
import manage.ReservationManager;
import model.Guest;
import model.Reservation;
import net.miginfocom.swing.MigLayout;
import tableModels.GuestModel;

public class EditGuestDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JTextField lastNameField;
	private JTextField usernameField;
	private JTextField addressField;
	private JTextField dateField;
	private JTextField phoneFIeld;
	private Guest guest;


	public EditGuestDialog(JTable table, Guest guest) {
		this.guest = guest;
		
		setSize(700, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		contentPanel.setLayout(new MigLayout("", "[grow][][grow]", "[][][][][][][][][][]"));
		
		JLabel titlelbl = new JLabel("Promenite podatke");
        titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
        titlelbl.setBorder(new EmptyBorder(10, 0, 10, 0));
        titlelbl.setHorizontalAlignment(SwingConstants.CENTER);
        contentPanel.add(titlelbl, "cell 0 0 4 1,alignx center");
        
        JLabel namelbl = new JLabel("Ime");
        contentPanel.add(namelbl, "cell 0 1");
        
        JLabel addresslbl = new JLabel("Adresa");
        contentPanel.add(addresslbl, "cell 2 1,alignx right");
        
        nameField = new JTextField(guest.getName());
        contentPanel.add(nameField, "cell 0 2,growx,gapright 10,height 30::");
        nameField.setColumns(10);
        
        addressField = new JTextField(guest.getAdress());
        contentPanel.add(addressField, "cell 2 2,growx,gapx 10,height 30::");
        addressField.setColumns(10);
        
        JLabel lastNamelbl = new JLabel("Prezime");
        contentPanel.add(lastNamelbl, "cell 0 3");
        
        JLabel birthDatelbl = new JLabel("Datum rodjenja");
        birthDatelbl.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPanel.add(birthDatelbl, "cell 2 3,alignx right");
        
        lastNameField = new JTextField(guest.getLastName());
        contentPanel.add(lastNameField, "cell 0 4,growx,gapright 10,height 30::");
        lastNameField.setColumns(10);
        
        dateField = new JTextField(guest.getBirthDate().toString());
        contentPanel.add(dateField, "cell 2 4,growx,gapx 10,height 30::");
        dateField.setColumns(10);
        
        JLabel usernamelbl = new JLabel("Korisnicko ime");
        contentPanel.add(usernamelbl, "cell 0 5");
        
        JLabel numberlbl = new JLabel("Broj telefona");
        contentPanel.add(numberlbl, "cell 2 5,alignx right");
        
        usernameField = new JTextField(guest.getUsername());
        contentPanel.add(usernameField, "cell 0 6,growx,gapright 10,height 30::");
        usernameField.setColumns(10);
        
        phoneFIeld = new JTextField(guest.getPhone());
        contentPanel.add(phoneFIeld, "cell 2 6,growx,gapx 10,hmin 30");
        phoneFIeld.setColumns(10);
        
        JLabel genderlbl = new JLabel("Rod");
        contentPanel.add(genderlbl, "cell 1 7,alignx center");
        
        String[] genders = {"Muski", "Zenski"};
        JComboBox<String> comboBox = new JComboBox<String>(genders);
        
		if (guest.getGender()) {
			comboBox.setSelectedItem("Muski");
		} else {
			comboBox.setSelectedItem("Zenski");
		}
        
        contentPanel.add(comboBox, "cell 1 8,growx,hmin 30");
        
        
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
                         String phone = phoneFIeld.getText();
                         String gender = (String) comboBox.getSelectedItem();
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

                         try {
                             LocalDate.parse(date);
                         } catch (DateTimeParseException ex) {
                             JOptionPane.showMessageDialog(null, "Datum mora biti u formatu yyyy-MM-dd", "Greska", JOptionPane.ERROR_MESSAGE);
                             return;
                         }
                         
                         saveChanges(username, name, lastName, address, date, phone, isMale);
                         GuestModel model = (GuestModel) table.getModel();
                         model.fireTableDataChanged();
                         dispose();
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
	
	private void saveChanges(String username, String name, String lastName, String address, String date, String phone, boolean gender) {
		if (!username.equals(guest.getUsername())) {
        	ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
        	List<Reservation> reservations = reservationManager.getReservationsByUsername(guest.getUsername());
        	for (Reservation reservation : reservations) {
        		reservation.setGuestUsername(username);
        	}
        }

        guest.setName(name);
        guest.setLastName(lastName);
        guest.setUsername(username);
        guest.setAdress(address);
        guest.setBirthDate(LocalDate.parse(date));
        guest.setPhone(phone);
        guest.setGender(gender);
        
        JOptionPane.showMessageDialog(null, "Podaci su usepsno promenjeni", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
	}

}
