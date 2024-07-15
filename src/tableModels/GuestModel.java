package tableModels;

import javax.swing.table.AbstractTableModel;

import manage.ManagerFactory;
import manage.UserManager;
import model.Guest;

public class GuestModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Ime", "Prezime", "Korisnicko ime", "Pol", "Datum rodjenja", "Telefon", "Adresa"};
	private UserManager userManager;
	
	public GuestModel() {
		this.userManager = ManagerFactory.getInstance().getUserManager();
	}
	
	@Override
	public int getRowCount() {
		return userManager.getGuests().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Guest guest = userManager.getGuests().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return guest.getName();
		case 1:
			return guest.getLastName();
		case 2:
			return guest.getUsername();
		case 3:
			return guest.getGender() ? "Muski" : "Zenski";
		case 4:
			return guest.getBirthDate();
		case 5:
			return guest.getPhone();
		case 6:
			return guest.getAdress();
		default:
			return null;
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
	
	public boolean deleteGuest(int index) {
		boolean deleted = this.userManager.removeUser(this.userManager.getGuests().get(index).getUsername());
		fireTableDataChanged();
		return deleted;
	}
	
	public Guest getGuestAt(int index) {
		return this.userManager.getGuests().get(index);
	}
}
