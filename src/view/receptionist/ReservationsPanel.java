package view.receptionist;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import manage.ManagerFactory;
import manage.PriceListManager;
import manage.RoomManager;
import model.Reservation;
import model.Room;
import model.RoomType;
import net.miginfocom.swing.MigLayout;
import tableModels.ReservationReceptionistModel;

public class ReservationsPanel extends JPanel {

	private static final long serialVersionUID = 1L;
    private JTextField numberField;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    
	public ReservationsPanel(JTable table, JTable checkInTable, JTable checkOutTable) {
		setLayout(new MigLayout("", "[grow][grow][]", "[][][][][][][][][][grow][]"));
		
		JLabel titlelbl = new JLabel("Rezervacije");
		titlelbl.setFont(new Font("Tahoma", Font.PLAIN, 30));
		add(titlelbl, "cell 0 0 3 1,alignx center");
		
		JLabel typelbl1 = new JLabel("Tip sobe");
		add(typelbl1, "cell 0 1");
		
		JLabel serviceslbl = new JLabel("Usluge");
		add(serviceslbl, "cell 1 1");
		
		RoomType[] types = RoomType.values();
		
		DefaultComboBoxModel<RoomType> roomTypeModelEmpty = new DefaultComboBoxModel<RoomType>(types);
		roomTypeModelEmpty.insertElementAt(null, 0);
		JComboBox<RoomType> typebox = new JComboBox<RoomType>(roomTypeModelEmpty);
		typebox.setSelectedIndex(0);
		add(typebox, "cell 0 2,growx,hmin 30");
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		PriceListManager priceListManager = ManagerFactory.getInstance().getPriceListManager();
		List<String> services = priceListManager.getAllServices();
		
		JPanel checkBoxPanel = new JPanel();
		checkBoxPanel.setLayout(new BoxLayout(checkBoxPanel, BoxLayout.Y_AXIS));
		
		for (String service : services) {
		    JCheckBox checkBox = new JCheckBox(service.toString());
		    checkBoxPanel.add(checkBox);
		}
		
		scrollPane_2.setViewportView(checkBoxPanel);
		
		add(scrollPane_2, "cell 1 2 1 7,growx,aligny top");
		
		JLabel numberlbl1 = new JLabel("Broj sobe");
		add(numberlbl1, "cell 0 3");
		
		numberField = new JTextField();
		add(numberField, "cell 0 4,growx,hmin 30");
		numberField.setColumns(10);
		
		JButton filterbtn = new JButton("Filtriraj");
		
		JLabel minPricelbl = new JLabel("Minimalna cena");
		add(minPricelbl, "cell 0 5");
		
		minPriceField = new JTextField();
		add(minPriceField, "cell 0 6,growx,hmin 30");
		minPriceField.setColumns(10);
		
		JLabel maxPricelbl = new JLabel("Maksimalna cena");
		add(maxPricelbl, "cell 0 7");
		
		maxPriceField = new JTextField();
		add(maxPriceField, "cell 0 8,growx,hmin 30");
		maxPriceField.setColumns(10);
		
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, "cell 0 9 3 1,grow");
		

		TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rowSorter);

		
		filterbtn.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        String num = numberField.getText();
		        String min = minPriceField.getText();
		        String max = maxPriceField.getText();
		        RoomType selectedValue = (RoomType) typebox.getSelectedItem();
		        List<String> selectedServices = new ArrayList<>();
		        for (Component component : checkBoxPanel.getComponents()) {
		            if (component instanceof JCheckBox) {
		                JCheckBox checkBox = (JCheckBox) component;
		                if (checkBox.isSelected()) {
		                    selectedServices.add(checkBox.getText());
		                }
		            }
		        }

		        List<RowFilter<TableModel,Integer>> filters = new ArrayList<RowFilter<TableModel, Integer>>(2);
		        if (num.trim().length() != 0) {
		            filters.add(new RowFilter<TableModel, Integer>() {
		                @Override
		                public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
		                	int number = (int) entry.getValue(4);
		                    return number == Integer.parseInt(num);
		                }
		            });
		        }
		        
				if (min.trim().length() != 0 ) {
                    filters.add(new RowFilter<TableModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                            double price = (double) entry.getValue(8);
                            return price >= Double.parseDouble(min);
                        }
                    });
                }
				
				if (max.trim().length() != 0 ) {
                    filters.add(new RowFilter<TableModel, Integer>() {
                        @Override
                        public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
                            double price = (double) entry.getValue(8);
                            return price >= Double.parseDouble(max);
                        }
                    });
                }
				
				if (selectedValue != null) {
					filters.add(new RowFilter<TableModel, Integer>() {
						@Override
						public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
							int number = (int) entry.getValue(4);
							RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
							Room room = roomManager.getRoom(number);
							return room.getType() == selectedValue;
						}
					});
				}
				
				if (selectedServices.size() > 0) {
					filters.add(new RowFilter<TableModel, Integer>() {
						@Override
						public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
							Reservation res = ((ReservationReceptionistModel) entry.getModel()).getReservation(entry.getIdentifier());
							List<String> services = res.getServices();
							for (String service : selectedServices) {
								if (!services.contains(service)) {
									return false;
								}
							}
							return true;
						}
					});
				}
				
				RowFilter<TableModel, Integer> rf = RowFilter.andFilter(filters);
		        rowSorter.setRowFilter(rf);
		    }
		});
		
		add(filterbtn, "cell 2 4 1 3,wmin 100,hmin 40");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

		Dimension buttonSize = new Dimension(150, 50);
		
		JButton editbtn = new JButton("Izmeni");
		editbtn.setMinimumSize(buttonSize);
		editbtn.setMaximumSize(buttonSize);
		editbtn.setPreferredSize(buttonSize);
		
		
		editbtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow != -1) {
					int modelRow = table.convertRowIndexToModel(selectedRow);
					ReservationReceptionistModel model = (ReservationReceptionistModel) table.getModel();
					new EditReservationDialog(model.getReservation(modelRow), table);
				}
			}
		});

		buttonPanel.add(editbtn);

		add(buttonPanel, "cell 0 10 3 1,growx");
	}

}
