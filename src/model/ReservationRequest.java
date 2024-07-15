package model;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import manage.ManagerFactory;
import manage.PriceListManager;

public class ReservationRequest {
	private LocalDate checkIn;
	private LocalDate checkOut;
	private RoomType roomType;
	private List<String> services;
	private String guest;
	private List<RoomQuality> qualities;
	private LocalDate creationDate;
	private double price;

	public ReservationRequest( RoomType roomType, LocalDate checkIn, LocalDate checkOut, List<RoomQuality> qualities, List<String> services,
			double price, LocalDate creationDate, String username) {
		this.checkIn = checkIn;
		this.checkOut = checkOut;
		this.roomType = roomType;
		this.services = services;
		this.guest = username;
		this.qualities = qualities;
		this.creationDate = creationDate;
		if (price == 0) {
			this.calculatePrice();
		} else {
			this.price = price;
		}
	}
	
	@Override
	public String toString() {
		StringBuilder qualities = new StringBuilder();
		for (RoomQuality quality : this.qualities) {
            qualities.append(quality.toStringFile() + ",");
        }
		if (qualities.length() > 0) {
		qualities.deleteCharAt(qualities.length() - 1);
		}
		return roomType.toStringFile() + "," + checkIn.toString() + "," + checkOut.toString() + "," + price +  "," + creationDate.toString()
			+ "," + guest + "," + services.toString() + "," + qualities.toString(); 
	}

	public LocalDate getCheckIn() {
		return checkIn;
	}
	
	public void setCheckIn(LocalDate checkIn) {
		this.checkIn = checkIn;
	}
	
	public LocalDate getCheckOut() {
		return checkOut;
	}
	
	public void setCheckOut(LocalDate checkOut) {
		this.checkOut = checkOut;
	}
	
	public RoomType getType() {
		return roomType;
	}
	
	public void setType(RoomType roomType) {
		this.roomType = roomType;
	}
	
	public List<String> getServices() {
		return services;
	}
	
	public void setServices(List<String> services) {
		this.services = services;
	}
	
	public String getGuest() {
		return guest;
	}
	
	public void setGuest(String username) {
		this.guest = username;
	}
	
	public List<RoomQuality> getQualities() {
		return qualities;
	}
	
	public void setQualities(List<RoomQuality> qualities) {
		this.qualities = qualities;
	}
	
	public LocalDate getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(LocalDate creationDate) {
		this.creationDate = creationDate;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
    private void calculatePrice() {
    	this.price = 0;
    	
    	 PriceListManager priceListManager = ManagerFactory.getInstance().getPriceListManager();
         PriceList priceList = priceListManager.getPriceList(checkIn);

         AtomicReference<PriceList> priceListRef = new AtomicReference<>(priceList);
    	
    	LocalDate startDate = checkIn;
    	while (startDate.isBefore(checkOut)){
    		this.price += priceList.getPrices().get(roomType.toStringFile());
    		this.price += services.stream().mapToDouble(service -> priceListRef.get().getPrices().get(service)).sum();
    		startDate = startDate.plusDays(1);
    		if (startDate.isAfter(priceList.getExpirationDate())) {
    			priceListRef.set(priceListManager.getPriceList(startDate));
    		}
    	} 
    }
	
	
	
}
