package tableModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import manage.ManagerFactory;
import manage.PriceListManager;
import model.PriceList;

public class PriceListsModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private String[] roomNames = {"Jednokrevetna", "Dvokrevetna sa dva odvojena kreveta", "Dvokrevetna sa bracnim krevetom", "Trokrevetna sa 3 kreveta", "Trokrevetna sa jednim bracnim i jednim odvojenim krevetom"};
	private String[] otherNames = {"Datum pocetka vazenja", "Datum kraja vazenja"};
	private PriceListManager priceListManager;
	private String[] columnNames;

	public PriceListsModel() {
		this.priceListManager = ManagerFactory.getInstance().getPriceListManager();
		List<String> services = priceListManager.getServices();
		List<String> rooms = new ArrayList<>(Arrays.asList(roomNames));
		List<String> other = new ArrayList<>(Arrays.asList(otherNames));
		other.addAll(rooms);
		other.addAll(services);
		this.columnNames = other.toArray(new String[0]);
	}

	@Override
	public int getRowCount() {
		return priceListManager.getPriceLists().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		PriceList priceList = priceListManager.getPriceLists().get(rowIndex);
		HashMap<String, Double> prices = priceList.getPrices();
		List<String> services = priceListManager.getServices();
		int predefinedColumns = 7;
		
		
		if (columnIndex < predefinedColumns) {
			switch (columnIndex) {
			case 0:
				return priceList.getStartDate();
			case 1:
				return priceList.getExpirationDate();
			case 2:
				return prices.get("SINGLE");
			case 3:
				return prices.get("DOUBLE1_1");
			case 4:
				return prices.get("DOUBLE2");
			case 5:
				return prices.get("TRIPLE1_1_1");
			case 6:
				return prices.get("TRIPLE1_2");
			
			default:
				return null;
			}
		} else {
			String service = services.get(columnIndex - predefinedColumns);
	        return prices.get(service);
		}
	}

	@Override
	public String getColumnName(int column) {
		return this.columnNames[column];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return this.getValueAt(0, columnIndex).getClass();
	}

	public PriceList getPriceListAt(int rowIndex) {
		return priceListManager.getPriceLists().get(rowIndex);
	}


	public List<PriceList> getPriceLists() {
		return priceListManager.getPriceLists();
	}
	
	public void addService(String serviceName, double price) {
        this.priceListManager.addService(serviceName, price);
        fireTableStructureChanged();
    }
}
