package model;

import manage.ManagerFactory;
import manage.PriceListManager;
import manage.RoomManager;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Reservation {
    protected int id;
    protected int roomNumber;
    protected String guestUsername;
    protected LocalDate checkIn;
    protected LocalDate checkOut;
    protected double price;
    protected ReservationStatus status;
    protected List<String> services;
    protected LocalDate creationDate;
    protected LocalDate actionDate;
    
    // Ako prvi put pravimo rezervaciju moramo izracunati kolika je cena
    public Reservation(int id, int roomNumber, String guestUsername, LocalDate checkIn, LocalDate checkOut, double price, ReservationStatus status, List<String> services, LocalDate creationDate, LocalDate actionDate) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.guestUsername = guestUsername;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
        this.services = services;
        this.creationDate = creationDate;
        this.actionDate = actionDate;
        
        if (price == 0){
        	this.calculatePrice();
        } else {
            this.price = price;
        }

       
    }

    public String toReadableString(){
        return "Broj sobe: " + roomNumber + "\n" +
                "Check in: " + checkIn.toString() + "\n" +
                "Check out: " + checkOut.toString() + "\n" +
                "Cena: " + price + "\n" +
                "Status: " + status + "\n" +
                "Usluge: " + services.toString();
    }

    @Override
    public String toString() {
    	if (actionDate == null) {
    		return id + "," + roomNumber + "," + guestUsername + "," + checkIn.toString() + "," + checkOut.toString() + "," + price + "," + status.toStringFile() + "," + creationDate.toString() + ",null," + services.toString();
    	}
        return id + "," + roomNumber + "," + guestUsername + "," + checkIn.toString() + "," + checkOut.toString() + "," + price + "," + status.toStringFile() + "," + creationDate.toString() + ',' + actionDate.toString() + ',' + services.toString();
    }

    public int getId() {
        return id;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public double getPrice() {
        return price;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public List<String> getServices() {
        return services;
    }
    
	public LocalDate getCreationDate() {
		return creationDate;
	}
	
	public LocalDate getActionDate() {
		return actionDate;
	}

	public void setId(int id) {
		this.id = id;
	}

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setServices(List<String> services) {
        this.services = services;
        this.calculatePrice();
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setGuestUsername(String guestUsername) {
        this.guestUsername = guestUsername;
    }
    
    public void setCreationDate(LocalDate creationDate) {
    	this.creationDate = creationDate;
    }
    
	public void setActionDate(LocalDate actionDate) {
		this.actionDate = actionDate;
	}
    
    private void calculatePrice() {
    	this.price = 0;
    	
    	 PriceListManager priceListManager = ManagerFactory.getInstance().getPriceListManager();
         RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
         PriceList priceList = priceListManager.getPriceList(checkIn);
         Room room = roomManager.getRoom(roomNumber);

         AtomicReference<PriceList> priceListRef = new AtomicReference<>(priceList);
    	
    	LocalDate startDate = checkIn;
    	while (startDate.isBefore(checkOut)){
    		this.price += priceList.getPrices().get(room.getType().toStringFile());
    		this.price += services.stream().mapToDouble(service -> priceListRef.get().getPrices().get(service)).sum();
    		startDate = startDate.plusDays(1);
    		if (startDate.isAfter(priceList.getExpirationDate())) {
    			priceListRef.set(priceListManager.getPriceList(startDate));
    		}
    	} 
    }
}
