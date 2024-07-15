package tableModels;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import manage.ManagerFactory;
import manage.RoomManager;
import model.Room;

public class RoomsModel extends AbstractTableModel {
	
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"Broj", "Tip sobe", "Status", "Dodatne osobine"};
	private RoomManager roomManager;

	public RoomsModel() {
		this.roomManager = ManagerFactory.getInstance().getRoomManager();
	}

	@Override
	public int getRowCount() {
		return roomManager.getRooms().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Room room = roomManager.getRooms().get(rowIndex);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < room.getQualities().size(); i++) {
			sb.append(room.getQualities().get(i).toString());
			if (i != room.getQualities().size() - 1) {
				sb.append(", ");
			}
		}
		switch (columnIndex) {
		case 0:
			return room.getNumber();
		case 1:
			return room.getType();
		case 2:
			return room.getStatus();
		case 3:
			return sb.toString();
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

	public Room getRoomAt(int rowIndex) {
		return roomManager.getRooms().get(rowIndex);
	}
	
	public int getRoomNumberAt(int rowIndex) {
		return roomManager.getRooms().get(rowIndex).getNumber();
	}


	public List<Room> geRooms() {
		return roomManager.getRooms();
	}

}
