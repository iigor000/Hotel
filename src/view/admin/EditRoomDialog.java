package view.admin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.RoomManager;
import model.Room;
import model.RoomQuality;
import model.RoomType;
import tableModels.RoomsModel;
import net.miginfocom.swing.MigLayout;

public class EditRoomDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private RoomsModel roomsModel;
	private JTextField numberField;

	public EditRoomDialog(int number, RoomType type, RoomsModel roomsModel) {
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		
		this.roomsModel = roomsModel;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][][][]"));
		
		JLabel numberlbl = new JLabel("Broj sobe");
		contentPanel.add(numberlbl, "cell 0 0");
		
		numberField = new JTextField(Integer.toString(number));
		contentPanel.add(numberField, "cell 0 1,growx,hmin 30");
		numberField.setColumns(10);
	
	
		JLabel typelbl = new JLabel("Tip sobe");
		contentPanel.add(typelbl, "cell 0 2");
	
	
		RoomType[] types = RoomType.values();
		JComboBox<RoomType> typeComboBox = new JComboBox<RoomType>(types);
		
		for (int i = 0; i < types.length; i++) {
			if (types[i].equals(type)) {
				typeComboBox.setSelectedIndex(i);
			}
		}
		
		JScrollPane scrollPane = new JScrollPane();
		
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

		RoomQuality[] qualities = RoomQuality.values();

		for (RoomQuality quality : qualities) {
		    JCheckBox checkBox = new JCheckBox(quality.toString());
			if (roomManager.getRoom(number).getQualities().contains(quality)) {
				checkBox.setSelected(true);
			}
		    
		    checkBoxPanel.add(checkBox);
		}

		scrollPane.setViewportView(checkBoxPanel);
		
		contentPanel.add(scrollPane, "cell 0 4,grow");
		
		contentPanel.add(typeComboBox, "cell 0 3,growx,hmin 30");
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				
				okButton.addActionListener(e -> {
                    String roomNumber = numberField.getText();
                    RoomType roomType = (RoomType) typeComboBox.getSelectedItem();
                    
					if (roomNumber.isEmpty()) {
						JOptionPane.showMessageDialog(this, "Morate uneti broj sobe.");
						return;
					}
					
					int num = Integer.parseInt(roomNumber);
					
					if (num != number && !roomManager.checkNumberAvailable(num)) {
						JOptionPane.showMessageDialog(this, "Soba sa tim brojem vec postoji.");
						return;
					}
					
					List<RoomQuality> selectedQualities = new ArrayList<>();

				    for (Component component : checkBoxPanel.getComponents()) {

				        if (component instanceof JCheckBox) {
				            JCheckBox checkBox = (JCheckBox) component;

				            if (checkBox.isSelected()) {
				                selectedQualities.add(RoomQuality.getFromReadableString(checkBox.getText()));
				            }
				        }
				    }
					
					Room room = roomManager.getRoom(number);
					room.setNumber(num);
					room.setType(roomType);
					room.setQualities(selectedQualities);
					
					JOptionPane.showMessageDialog(this, "Soba uspesno promenjena");
					this.roomsModel.fireTableDataChanged();
					dispose();
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

}
