package view.admin;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import manage.ManagerFactory;
import model.PriceList;
import net.miginfocom.swing.MigLayout;
import tableModels.PriceListsModel;

public class PriceListsPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	public PriceListsPanel(JTable priceListTable) {
		Dimension buttonSize = new Dimension(150, 50);
		
		setLayout(new MigLayout("", "[grow]", "[][grow][]"));
		
		JScrollPane scrollPane_2 = new JScrollPane(priceListTable);
		
		JLabel titlelbl = new JLabel("Cenovnici");
		titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(titlelbl, "cell 0 0,alignx center");
		add(scrollPane_2, "cell 0 1,grow");
		
		JPanel buttonPanel2 = new JPanel();
		buttonPanel2.setLayout(new BoxLayout(buttonPanel2, BoxLayout.X_AXIS));

		JButton addbtn1 = new JButton("Dodaj cenovnik");
		addbtn1.setMinimumSize(buttonSize);
		addbtn1.setMaximumSize(buttonSize);
		addbtn1.setPreferredSize(buttonSize);
		JButton deletebtn1 = new JButton("Obrisi cenovnik");
		deletebtn1.setMinimumSize(buttonSize);
		deletebtn1.setMaximumSize(buttonSize);
		deletebtn1.setPreferredSize(buttonSize);
		JButton changebtn2 = new JButton("Izmeni cenovnik");
		changebtn2.setMinimumSize(buttonSize);
		changebtn2.setMaximumSize(buttonSize);
		changebtn2.setPreferredSize(buttonSize);
		JButton addService = new JButton("Dodaj uslugu");
		addService.setMinimumSize(buttonSize);
		addService.setMaximumSize(buttonSize);
		addService.setPreferredSize(buttonSize);
		JButton deleteService = new JButton("Obrisi uslugu");
		deleteService.setMinimumSize(buttonSize);
		deleteService.setMaximumSize(buttonSize);
		deleteService.setPreferredSize(buttonSize);
		
		addbtn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddPriceListDialog((PriceListsModel) priceListTable.getModel());
			}
		});
		
		deletebtn1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = priceListTable.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati cenovnik", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				PriceList priceList = ((PriceListsModel) priceListTable.getModel()).getPriceListAt(row);

				ManagerFactory.getInstance().getPriceListManager().removePriceList(priceList);
				
				((PriceListsModel) priceListTable.getModel()).fireTableDataChanged();

				JOptionPane.showMessageDialog(null, "Cenovnik je obrisan", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		changebtn2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int row = priceListTable.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(null, "Morate selektovati cenovnik", "Greska",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				PriceList priceList = ((PriceListsModel) priceListTable.getModel()).getPriceListAt(row);
				new EditPriceListDialog(priceList, (PriceListsModel) priceListTable.getModel());
			}
		});
		
		addService.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new AddServiceDialog((PriceListsModel) priceListTable.getModel(), priceListTable);
			}
		});
		
		deleteService.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new DeleteServiceDialog((PriceListsModel) priceListTable.getModel(), priceListTable);
			}
		});
		
		buttonPanel2.add(addbtn1);
		buttonPanel2.add(Box.createHorizontalGlue());
		buttonPanel2.add(changebtn2);
		buttonPanel2.add(Box.createHorizontalGlue());
		buttonPanel2.add(addService);
		buttonPanel2.add(Box.createHorizontalGlue());
		buttonPanel2.add(deleteService);
		buttonPanel2.add(Box.createHorizontalGlue());
		buttonPanel2.add(deletebtn1);
		
		add(buttonPanel2, "cell 0 2,growx");
	}

}
