package tableModels;

import javax.swing.table.AbstractTableModel;

import manage.ManagerFactory;
import manage.ReservationManager;
import manage.RoomManager;
import manage.UserManager;
import model.Reservation;
import model.User;

public class CheckInModel extends AbstractTableModel{
	
	private static final long serialVersionUID = 1L;
	private String[] columnNames = {"ID", "Ime", "Prezime", "Korisnicko ime", "Broj sobe", "Tip sobe", "Ulaz", "Izlaz", "Status", "Cena", "Usluge"};
	private ReservationManager reservationManager;
	
	public CheckInModel() {
		this.reservationManager = ManagerFactory.getInstance().getReservationManager();
	}

	@Override
	public int getRowCount() {
	    return reservationManager.getTodaysCheckIn().size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Reservation reservation = reservationManager.getCurrentReservations().get(rowIndex);
		UserManager userManager = ManagerFactory.getInstance().getUserManager();
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
	
	public Reservation getReservation(int rowIndex) {
		return reservationManager.getTodaysCheckIn().get(rowIndex);
	}
}
