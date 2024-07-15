package view.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import tableModels.PriceListsModel;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddServiceDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField nameField;
	private JTextField priceField;
	private PriceListsModel priceListsModel;

	public AddServiceDialog(PriceListsModel priceListsModel, JTable priceListTable) {
		this.priceListsModel = priceListsModel;
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][][][]"));
		{
			JLabel namelbl = new JLabel("Naziv usluge");
			contentPanel.add(namelbl, "cell 0 0");
		}
		{
			nameField = new JTextField();
			contentPanel.add(nameField, "cell 0 1,growx,hmin 30");
			nameField.setColumns(10);
		}
		{
			JLabel pricelbl = new JLabel("Cena usluge");
			contentPanel.add(pricelbl, "cell 0 2");
		}
		{
			priceField = new JTextField();
			contentPanel.add(priceField, "cell 0 3,growx,hmin 30");
			priceField.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				
				okButton.addActionListener(e -> {
					String name = nameField.getText();
					String price = priceField.getText();
					
					if (name.isEmpty() || price.isEmpty()) {
						JOptionPane.showMessageDialog(this, "Morate popuniti sva polja!", "Greska",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if (name.contains(",")) {
				        JOptionPane.showMessageDialog(null, "Zarez nije dozvoljen karatker", "Greska", JOptionPane.ERROR_MESSAGE);
				        return;
				    }
					
					try {
						Double.parseDouble(price);
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(this, "Cena mora biti broj!", "Greska",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					this.priceListsModel.addService(name, Double.parseDouble(price));
					
					JOptionPane.showMessageDialog(this, "Usluga je uspesno dodata!");
					
					priceListTable.setModel(new PriceListsModel());
					this.dispose();
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
