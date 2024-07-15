package tableModels;

import java.time.LocalDate;

import javax.swing.table.AbstractTableModel;

import manage.CleanersManager;
import manage.ManagerFactory;
import manage.UserManager;
import model.CleanerRooms;
import model.User;

public class CleanersModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Korisnicko ime", "Ime", "Prezime", "Broj soba"};
	private CleanersManager cleanersManager;
	private LocalDate start;
	private LocalDate end;
	
	public CleanersModel(LocalDate start, LocalDate end) {
		this.start = start;
		this.end = end;
		this.cleanersManager = ManagerFactory.getInstance().getCleanersManager();
	}

	@Override
	public int getRowCount() {
		return cleanersManager.getCleaners().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CleanerRooms cleaner = cleanersManager.getCleaners().get(rowIndex);
		UserManager userManager = ManagerFactory.getInstance().getUserManager();
		
		User cleanerUser = userManager.getUser(cleaner.getCleanerUsername());
		switch (columnIndex) {
        case 0:
            return cleanerUser.getUsername();
        case 1:
            return cleanerUser.getName();
        case 2:
            return cleanerUser.getLastName();
        case 3:
            return cleaner.getCleanedRoomsByDate(start, end);
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

}
