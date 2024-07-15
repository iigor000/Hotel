package view.admin;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import manage.ManagerFactory;
import manage.PriceListManager;
import tableModels.PriceListsModel;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddPriceListDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private PriceListsModel priceListsModel;
	private JTextField singleField;
	private JTextField double1_1Field;
	private JTextField double2Field;
	private JTextField triple1_1_1Field;
	private JTextField triple1_2Field;
	private List<JTextField> serviceFields;
	private JTextField startField;
	private JTextField endField;

	public AddPriceListDialog(PriceListsModel priceListsModel) {
		this.priceListsModel = priceListsModel;
		PriceListManager priceListManager = ManagerFactory.getInstance().getPriceListManager();
		
		setSize(300, 500);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[grow]", "[][][][][][][][][][][][][][]"));
		{
			JLabel startlbl = new JLabel("Datum pocetka");
			contentPanel.add(startlbl, "cell 0 0");
		}
		{
			startField = new JTextField(priceListManager.getNextStartDate().toString());
			contentPanel.add(startField, "cell 0 1,growx");
			startField.setColumns(10);
		}
		{
			JLabel endlbl = new JLabel("Datum kraja");
			contentPanel.add(endlbl, "cell 0 2");
		}
		{
			endField = new JTextField();
			contentPanel.add(endField, "cell 0 3,growx");
			endField.setColumns(10);
		}
		{
			JLabel singlelbl = new JLabel("Jednokrevetna soba");
			contentPanel.add(singlelbl, "cell 0 4");
		}
		{
			singleField = new JTextField();
			contentPanel.add(singleField, "cell 0 5,growx");
			singleField.setColumns(10);
		}
		{
			JLabel double1_1lbl = new JLabel("Dvokrevetna sa dva  kreveta");
			contentPanel.add(double1_1lbl, "cell 0 6");
		}
		{
			double1_1Field = new JTextField();
			contentPanel.add(double1_1Field, "cell 0 7,growx");
			double1_1Field.setColumns(10);
		}
		{
			JLabel doubl2lbl = new JLabel("Dvokrevetna sa bracnim krevetom");
			contentPanel.add(doubl2lbl, "cell 0 8");
		}
		{
			double2Field = new JTextField();
			contentPanel.add(double2Field, "cell 0 9,growx");
			double2Field.setColumns(10);
		}
		{
			JLabel triple1_1_1lbl = new JLabel("Trokrevetna sa 3 kreveta");
			contentPanel.add(triple1_1_1lbl, "cell 0 10");
		}
		{
			triple1_1_1Field = new JTextField();
			contentPanel.add(triple1_1_1Field, "cell 0 11,growx");
			triple1_1_1Field.setColumns(10);
		}
		{
			JLabel triple1_2lbl = new JLabel("Trokrevetna sa dva kreveta");
			contentPanel.add(triple1_2lbl, "cell 0 12");
		}
		{
			triple1_2Field = new JTextField();
			contentPanel.add(triple1_2Field, "cell 0 13,growx");
			triple1_2Field.setColumns(10);
		}
		
		serviceFields = new ArrayList<>();
        List<String> services = priceListManager.getServices();
        int row = 14;
        for (String service : services) {
            JLabel serviceLabel = new JLabel(service);
            contentPanel.add(serviceLabel, "cell 0 " + row++);

            JTextField serviceField = new JTextField();
            contentPanel.add(serviceField, "cell 0 " + row++ + ",growx");
            serviceField.setColumns(10);

            serviceFields.add(serviceField);
        }
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				
				okButton.addActionListener(e ->{
					String start = startField.getText();
					String end = endField.getText();
					
					if (startField.getText().isEmpty() || endField.getText().isEmpty() || singleField.getText().isEmpty()
							|| double1_1Field.getText().isEmpty() || double2Field.getText().isEmpty()
							|| triple1_1_1Field.getText().isEmpty() || triple1_2Field.getText().isEmpty() || serviceFields.stream().anyMatch(field -> field.getText().isEmpty())) {
						JOptionPane.showMessageDialog(this, "Morate popuniti sva polja.");
						return;
					}
					
					try {
						LocalDate.parse(start);
						LocalDate.parse(end);
					} catch (DateTimeParseException ex) {
						JOptionPane.showMessageDialog(null, "Datum mora biti u formatu yyyy-MM-dd", "Greska", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					LocalDate startDate = LocalDate.parse(start);
					LocalDate endDate = LocalDate.parse(end);
					
					if (startDate.isAfter(endDate.minusDays(1))) {
						JOptionPane.showMessageDialog(null, "Datum kraja mora biti posle datuma pocetka", "Greska",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					List<Double> prices = new ArrayList<>();
				    try {
				        prices.add(Double.parseDouble(singleField.getText()));
				        prices.add(Double.parseDouble(double1_1Field.getText()));
				        prices.add(Double.parseDouble(double2Field.getText()));
				        prices.add(Double.parseDouble(triple1_1_1Field.getText()));
				        prices.add(Double.parseDouble(triple1_2Field.getText()));

				        for (JTextField serviceField : serviceFields) {
				            prices.add(Double.parseDouble(serviceField.getText()));
				        }
				    } catch (NumberFormatException ex) {
				        JOptionPane.showMessageDialog(this, "Sva polja moraju imati validne brojeve.", "Greska", JOptionPane.ERROR_MESSAGE);
				        return;
				    }

				    List<String> keys = priceListManager.getAllServices();
				    
				    HashMap<String, Double> priceMap = new HashMap<>();
				    priceMap.put("SINGLE", prices.get(0));
				    priceMap.put("DOUBLE1_1", prices.get(1));
				    priceMap.put("DOUBLE2", prices.get(2));
				    priceMap.put("TRIPLE1_1_1", prices.get(3));
				    priceMap.put("TRIPLE1_2", prices.get(4));
				    for (int i = 0; i < keys.size(); i++) {
				        priceMap.put(keys.get(i), prices.get(i+5));
				    }
				    
					priceListManager.addPriceList(startDate, endDate, priceMap);
					this.priceListsModel.fireTableDataChanged();
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
		
		JScrollPane scrollPane = new JScrollPane(contentPanel);
	    getContentPane().add(scrollPane, BorderLayout.CENTER);
	    
	    setLocationRelativeTo(null);
		
		setVisible(true);
	}

}
