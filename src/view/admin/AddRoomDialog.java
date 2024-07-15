package view.admin;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.RoomManager;
import model.RoomQuality;
import model.RoomType;
import net.miginfocom.swing.MigLayout;
import tableModels.RoomsModel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

public class AddRoomDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField numberField;
	private RoomsModel roomsModel;

	public AddRoomDialog(RoomsModel roomsModel) {
		this.roomsModel = roomsModel;
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][][][][grow]"));
		
		JLabel numberlbl = new JLabel("Broj sobe");
		contentPanel.add(numberlbl, "cell 0 0");
		
		numberField = new JTextField();
		contentPanel.add(numberField, "cell 0 1,growx,hmin 30");
		numberField.setColumns(10);
	
	
		JLabel typelbl = new JLabel("Tip sobe");
		contentPanel.add(typelbl, "cell 0 2");
	
	
		RoomType[] types = RoomType.values();
		JComboBox<RoomType> typeComboBox = new JComboBox<RoomType>(types);
		contentPanel.add(typeComboBox, "cell 0 3,growx,hmin 30");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));

		RoomQuality[] qualities = RoomQuality.values();

		for (RoomQuality quality : qualities) {
		    JCheckBox checkBox = new JCheckBox(quality.toString());
		    checkBoxPanel.add(checkBox);
		}

		scrollPane.setViewportView(checkBoxPanel);
		
		contentPanel.add(scrollPane, "cell 0 4,grow");
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				
				okButton.addActionListener(e -> {
                    String number = numberField.getText();
                    RoomType type = (RoomType) typeComboBox.getSelectedItem();
                    RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
                    
					if (number.isEmpty()) {
						JOptionPane.showMessageDialog(this, "Morate uneti broj sobe.");
						return;
					}
					
					int num = Integer.parseInt(number);
					
					if (!roomManager.checkNumberAvailable(num)) {
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
					
					roomManager.addRoom(num, type, null, selectedQualities);
					
					JOptionPane.showMessageDialog(this, "Soba uspesno dodata.");
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
