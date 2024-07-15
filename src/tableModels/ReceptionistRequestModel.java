package tableModels;

import javax.swing.table.AbstractTableModel;

import manage.ManagerFactory;
import manage.ReservationRequestManager;
import manage.UserManager;
import model.ReservationRequest;
import model.ReservationStatus;
import model.RoomQuality;
import model.User;

public class ReceptionistRequestModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private ReservationRequestManager reservationRequestManager;
	private String[] columnNames = {"Ime", "Prezime", "Korisnicko ime", "Tip sobe", "Ulaz", "Izlaz", "Status", "Cena", "Usluge", "Dodatni zahtevi"};
	
	public ReceptionistRequestModel() {
		this.reservationRequestManager = ManagerFactory.getInstance().getReservationRequestManager();
	}
	
	@Override
	public int getRowCount() {
		return reservationRequestManager.getCurrentRequests().size();
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (reservationRequestManager.getCurrentRequests().isEmpty()) {
	        return null; 
	    }
		ReservationRequest reservationRequest = reservationRequestManager.getCurrentRequests().get(rowIndex);
		UserManager userManager = ManagerFactory.getInstance().getUserManager();
		User guest = userManager.getUser(reservationRequest.getGuest());
		switch (columnIndex) {
		case 0:
			return guest.getName();
		case 1:
			return guest.getLastName();
		case 2:
			return reservationRequest.getGuest();
		case 3:
			return reservationRequest.getType();
		case 4:
			return reservationRequest.getCheckIn();
		case 5:
			return reservationRequest.getCheckOut();
		case 6:
			return ReservationStatus.WAITING;
		case 7:
			return reservationRequest.getPrice();
		case 8:
			StringBuilder services = new StringBuilder();
			for (String service : reservationRequest.getServices()) {
				services.append(service + ", ");
			}
			if (services.length() > 0) {
				services.deleteCharAt(services.length() - 2);
			}
			return services.toString();
		case 9:
			StringBuilder qualities = new StringBuilder();
			for (RoomQuality quality : reservationRequest.getQualities()) {
				qualities.append(quality.toString() + ", ");
			}
			if (qualities.length() > 0) {
				qualities.deleteCharAt(qualities.length() - 2);
			}
			return qualities.toString();
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
		if (reservationRequestManager.getCurrentRequests().isEmpty()) {
	        return Object.class;
	    }
		return this.getValueAt(0, columnIndex).getClass();
	}
	
	public ReservationRequest getRequest(int rowIndex) {
		return reservationRequestManager.getCurrentRequests().get(rowIndex);
	}
	
	public boolean acceptRequest(int rowIndex) {
		ReservationRequest request = reservationRequestManager.getCurrentRequests().get(rowIndex);
		boolean success = reservationRequestManager.acceptRequest(request);
		if (success) {
			fireTableDataChanged();
			return true;
		}
		return false;
	}
	
	public boolean rejectRequest(int rowIndex) {
		ReservationRequest request = reservationRequestManager.getCurrentRequests().get(rowIndex);
		reservationRequestManager.rejectRequest(request);
		fireTableDataChanged();
		return true;
	}
}
