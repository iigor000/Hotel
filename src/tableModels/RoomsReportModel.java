package tableModels;

import java.time.LocalDate;

import javax.swing.table.AbstractTableModel;

import manage.ManagerFactory;
import manage.RoomManager;
import model.Room;

public class RoomsReportModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Broj sobe", "Tip sobe", "Broj nocenja", "Prihod"};
	private LocalDate startDate;
	private LocalDate endDate;
	private RoomManager roomManager;
	
	public RoomsReportModel(LocalDate startDate, LocalDate endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.roomManager = ManagerFactory.getInstance().getRoomManager();
	}

	@Override
	public int getRowCount() {
		return this.roomManager.getRooms().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Room room = this.roomManager.getRooms().get(rowIndex);
		switch (columnIndex) {
		case 0:
			return room.getNumber();
		case 1:
			return room.getType();
		case 2:
			return roomManager.getNights(room.getNumber(), startDate, endDate);
		case 3:
			return roomManager.getEarnings(room.getNumber(), startDate, endDate);
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
