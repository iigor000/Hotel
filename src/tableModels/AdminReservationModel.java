package tableModels;

import javax.swing.table.AbstractTableModel;

import manage.ManagerFactory;
import manage.ReservationManager;
import manage.RoomManager;
import manage.UserManager;
import model.Reservation;
import model.ReservationStatus;
import model.Room;
import model.RoomStatus;
import model.User;

public class AdminReservationModel extends AbstractTableModel{
	
	private static final long serialVersionUID = 1L;
	private ReservationManager reservationManager;
	private UserManager userManager;
	private String[] columnNames = {"ID", "Ime", "Prezime", "Korisnicko ime", "Broj sobe", "Tip sobe", "Ulaz", "Izlaz", "Status", "Cena", "Usluge"};

	
	public AdminReservationModel() {
		this.reservationManager = ManagerFactory.getInstance().getReservationManager();
		this.userManager = ManagerFactory.getInstance().getUserManager();
	}
	
	@Override
	public int getRowCount() {
		return reservationManager.getReservations().size();
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Reservation reservation = reservationManager.getReservations().get(rowIndex);
		User user = userManager.getUser(reservation.getGuestUsername());
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		switch (columnIndex) {
		case 0:
			return reservation.getId();
		case 1:
			return user.getName();
		case 2:
			return user.getLastName();
		case 3:
			return user.getUsername();
		case 4:
			return reservation.getRoomNumber();
		case 5:
			return roomManager.getRoom(reservation.getRoomNumber()).getType();
		case 6:
			return reservation.getCheckIn();
		case 7:
			return reservation.getCheckOut();
		case 8:
            return reservation.getStatus();
		case 9:
			return reservation.getPrice();
	    case 10:
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
	
	public boolean rejectReservation(int rowIndex) {
		Reservation reservation = reservationManager.getReservations().get(rowIndex);
		if (reservation.getStatus() == ReservationStatus.REJECTED || reservation.getStatus() == ReservationStatus.CANCELED) {
			return false;
		}
		reservationManager.rejectReservation(reservation.getId());
		fireTableDataChanged();
		return true;
	}
	
	public boolean approveReservation(int rowIndex) {
		Reservation reservation = reservationManager.getReservations().get(rowIndex);
		if (reservation.getStatus() == ReservationStatus.CONFIRMED || reservation.getStatus() == ReservationStatus.CANCELED || reservation.getStatus() == ReservationStatus.REJECTED) {
			return false;
		}
		reservation.setStatus(ReservationStatus.CONFIRMED);
		fireTableDataChanged();
		return true;
	}

	
	public boolean CheckOut(int rowIndex) {
		Reservation reservation = reservationManager.getReservations().get(rowIndex);
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		Room room = roomManager.getRoom(reservation.getRoomNumber());
		if (room.getStatus() == RoomStatus.AVAILABLE || room.getStatus() == RoomStatus.CLEANING) {
			return false;
		}
		roomManager.checkOut(reservation.getRoomNumber(), reservation);
		fireTableDataChanged();
		return true;
	}
	
	public Reservation getReservation(int rowIndex) {
		return reservationManager.getReservations().get(rowIndex);
	}
	
	public boolean deleteReservation(int rowIndex) {
		Reservation reservation = reservationManager.getReservations().get(rowIndex);
		if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
			return false;
		}
		reservationManager.removeReservation(reservation.getId());
		fireTableDataChanged();
		return true;
	}
}
