package manage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.ReservationRequest;
import model.ReservationStatus;
import model.RoomQuality;
import model.RoomType;

public class ReservationRequestManager {
	private List<ReservationRequest> requests;
	private String file;
	
	public ReservationRequestManager(String file) {
		this.file = file;
		this.requests = new ArrayList<ReservationRequest>();
	}
	
	public boolean loadData() {
		 try {
	            BufferedReader reader = new BufferedReader(new FileReader(this.file));
	            String line = "";
	            while ((line = reader.readLine()) != null) {
	            	String data = line;
	                int commaIndex = data.indexOf(',');
	                
	            	// Ime je do prvog zareza, a ostatak su datumi
	            	String type = data.substring(0, commaIndex);
	                String rest = data.substring(commaIndex + 1);
	                
	                String checkIn = rest.substring(0, rest.indexOf(','));
	                rest = rest.substring(rest.indexOf(',') + 1);
	                
	                String checkOut = rest.substring(0, rest.indexOf(','));
	                rest = rest.substring(rest.indexOf(',') + 1);
	                
	                String price = rest.substring(0, rest.indexOf(','));
	                rest = rest.substring(rest.indexOf(',') + 1);
	                
	                String creation = rest.substring(0, rest.indexOf(','));
	                rest = rest.substring(rest.indexOf(',') + 1);
	                
	                String guest = rest.substring(0, rest.indexOf(','));
	                rest = rest.substring(rest.indexOf(',') + 1);
	                
	                String servicesstr = rest.substring(1, rest.indexOf(']'));
	                rest = rest.substring(rest.indexOf(']') + 2);
	                String[] services = servicesstr.split(", ");
	                List<RoomQuality> qualitiesList = new ArrayList<>();
	                
	                List<String> servicesList = new ArrayList<>();
	                String[] elements = rest.split(","); 
	                
	                if (!services[0].equals("")) {
						for (int i = 0; i < services.length; i++) {
							servicesList.add(services[i]);
						}
	                }
	                
					if (!elements[0].equals("")) {
						for (int i = 0; i < elements.length; i++) {
							qualitiesList.add(RoomQuality.valueOf(elements[i]));
						}
					}
	                
					ReservationRequest request = new ReservationRequest(RoomType.valueOf(type), LocalDate.parse(checkIn), LocalDate.parse(checkOut), qualitiesList, servicesList, Double.parseDouble(price), LocalDate.parse(creation), guest);
	                requests.add(request);
	                }
	            reader.close();
	        } catch (FileNotFoundException e) {
	            return false;
	        } catch (IOException e) {
	            return false;
	        }
	        return true;
	}
	
	public boolean saveData() {
		try {
			StringBuilder file = new StringBuilder();
			FileWriter writer = new FileWriter(this.file);
			for (ReservationRequest request : this.requests) {
				file.append(request.toString() + "\n");
			}
			writer.write(file.toString());
			writer.close();
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public int getNumberOfRequestsByDate(LocalDate start, LocalDate end) {
		int count = 0;
		for (ReservationRequest request : requests) {
			if (request.getCheckIn().isAfter(start) && request.getCheckOut().isBefore(end)) {
				count++;
			}
		}
		return count;
	}

	public boolean addRequest(LocalDate checkIn, LocalDate checkOut, RoomType type, List<RoomQuality> qualities, List<String> services, double price, LocalDate creationDate, String guest) {
		requests.add(new ReservationRequest(type, checkIn, checkOut, qualities, services, price, creationDate, guest));
		return true;
	}
	
	public List<ReservationRequest> getRequests() {
		return requests;
	}
	
	public List<ReservationRequest> getCurrentRequests(){
		List<ReservationRequest> currentRequests = new ArrayList<ReservationRequest>();
        for (ReservationRequest request : requests) {
            if (request.getCheckIn().isAfter(LocalDate.now())) {
                currentRequests.add(request);
            }
        }
        return currentRequests;
    }
	
	public List<ReservationRequest> getRequestsByUsername(String username){
		List<ReservationRequest> userRequests = new ArrayList<ReservationRequest>();
		for (ReservationRequest request : requests) {
			if (request.getGuest().equals(username)) {
				userRequests.add(request);
			}
		}
		return userRequests;
	}
	
	public boolean acceptRequest(ReservationRequest request) {
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
		int number = roomManager.checkAvailable(request.getCheckIn(), request.getCheckOut(), request.getType() ,request.getQualities());
		if (number == 0) {
			return false;
		}
		reservationManager.addReservation(number, request.getGuest(), request.getCheckIn(), request.getCheckOut(), request.getPrice(), ReservationStatus.CONFIRMED, request.getServices(), request.getCreationDate(), LocalDate.now());
		requests.remove(request);
		return true;
	}
	
	public boolean rejectRequest(ReservationRequest request) {
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
		int number = roomManager.getRoomOfType(request.getType());
		reservationManager.addReservation(number, request.getGuest(), request.getCheckIn(), request.getCheckOut(), request.getPrice(), ReservationStatus.REJECTED, request.getServices(), request.getCreationDate(), LocalDate.now());
		requests.remove(request);
		return true;
	}
	
	public boolean cancelRequest(ReservationRequest request) {
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
		int number = roomManager.getRoomOfType(request.getType());
		reservationManager.addReservation(number, request.getGuest(), request.getCheckIn(), request.getCheckOut(), request.getPrice(), ReservationStatus.CANCELED, request.getServices(), request.getCreationDate(), LocalDate.now());
		requests.remove(request);
		return true;
	}
}
