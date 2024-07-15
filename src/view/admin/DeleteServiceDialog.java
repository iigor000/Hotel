package view.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.PriceListManager;
import tableModels.PriceListsModel;

public class DeleteServiceDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	public DeleteServiceDialog(PriceListsModel priceListsModel, JTable priceListTable) {
		PriceListManager priceListManager = ManagerFactory.getInstance().getPriceListManager();
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		List<String> serviceNames = priceListManager.getAllServices();
		
		for (String serviceName : serviceNames) {
            JCheckBox checkBox = new JCheckBox(serviceName);
            contentPanel.add(checkBox);
        }
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				
				okButton.addActionListener(e -> {
					for (int i = 0; i < contentPanel.getComponentCount(); i++) {
						JCheckBox checkBox = (JCheckBox) contentPanel.getComponent(i);
						if (checkBox.isSelected()) {
							priceListManager.deleteService(checkBox.getText());
						}
					}
					
					JOptionPane.showMessageDialog(null, "Usluga je uspesno obrisana", "Uspeh", JOptionPane.INFORMATION_MESSAGE);

                    priceListTable.setModel(new PriceListsModel());
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
