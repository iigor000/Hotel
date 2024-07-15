package tableModels;

import javax.swing.table.AbstractTableModel;

import manage.ManagerFactory;
import manage.ReservationManager;
import manage.RoomManager;
import model.Guest;
import model.Reservation;
import model.ReservationStatus;

public class ReservationGuestModel extends AbstractTableModel {
	private static final long serialVersionUID = 1L;
	private ReservationManager reservationManager;
	private Guest guest;
	private String[] columnNames = {"ID", "Broj sobe", "Tip sobe", "Ulaz", "Izlaz", "Status", "Cena", "Usluge"};
	
	public ReservationGuestModel(Guest guest) {
		this.reservationManager = ManagerFactory.getInstance().getReservationManager();
		this.guest = guest;
	}
	
	@Override
	public int getRowCount() {
		return this.reservationManager.getReservationsByUsername(guest.getUsername()).size();
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Reservation reservation = this.reservationManager.getReservationsByUsername(guest.getUsername()).get(rowIndex);
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		switch (columnIndex) {
		case 0:
			return reservation.getId();
		case 1:
			return reservation.getRoomNumber();
		case 2:
			return roomManager.getRoom(reservation.getRoomNumber()).getType();
		case 3:
			return reservation.getCheckIn();
		case 4:
			return reservation.getCheckOut();
		case 5:
            return reservation.getStatus();
		case 6:
			return reservation.getPrice();
	    case 7:
	    	StringBuilder services = new StringBuilder();
	    	for (String service : reservation.getServices()) {
        		services.append(service + ", ");
        	}
	    	services.deleteCharAt(services.length() - 2);
	    	return services.toString();
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
	
	public boolean cancelReservation(int rowIndex) {
		Reservation reservation = this.reservationManager.getReservationsByUsername(guest.getUsername()).get(rowIndex);
		if (reservation.getStatus() == ReservationStatus.CANCELED) {
			return false;
		}
		reservationManager.cancelReservation(reservation.getId());
		fireTableDataChanged();
		return true;
	}

}
