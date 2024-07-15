package tableModels;

import javax.swing.table.AbstractTableModel;

import manage.ManagerFactory;
import manage.ReservationRequestManager;
import model.Guest;
import model.ReservationRequest;
import model.ReservationStatus;
import model.RoomQuality;

public class GuestRequestsModel extends AbstractTableModel{
	private static final long serialVersionUID = 1L;
	private ReservationRequestManager reservationRequestManager;
	private Guest guest;
	private String[] columnNames = {"Tip sobe", "Ulaz", "Izlaz", "Status", "Cena", "Usluge", "Dodatni zahtevi"};
	
	public GuestRequestsModel(Guest guest) {
		this.reservationRequestManager = ManagerFactory.getInstance().getReservationRequestManager();
		this.guest = guest;
	}

	@Override
	public int getRowCount() {
		return this.reservationRequestManager.getRequestsByUsername(guest.getUsername()).size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (this.reservationRequestManager
				.getRequestsByUsername(guest.getUsername()).isEmpty()) {
	        return null;
	    }
		ReservationRequest reservationRequest = this.reservationRequestManager
				.getRequestsByUsername(guest.getUsername()).get(rowIndex);
		switch (columnIndex) {
		case 0:
			return reservationRequest.getType();
		case 1:
			return reservationRequest.getCheckIn();
		case 2:
			return reservationRequest.getCheckOut();
		case 3:
			return ReservationStatus.WAITING;
		case 4:
			return reservationRequest.getPrice();
		case 5:
			StringBuilder services = new StringBuilder();
			for (String service : reservationRequest.getServices()) {
				services.append(service + ", ");
			}
			if (services.length() > 0) {
				services.deleteCharAt(services.length() - 2);
			}
			return services.toString();
		case 6:
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
		if (this.reservationRequestManager
				.getRequestsByUsername(guest.getUsername()).isEmpty()) {
	        return null;
	    }
		return this.getValueAt(0, columnIndex).getClass();
	}
	
	public boolean cancelReservation(int rowIndex) {
		ReservationRequest request = reservationRequestManager.getCurrentRequests().get(rowIndex);
		reservationRequestManager.cancelRequest(request);
		fireTableDataChanged();
		return true;
	}

}
