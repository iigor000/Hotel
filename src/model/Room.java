package model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class Room {
    protected RoomType type;
    protected int number;
    protected List<LocalDate> datesOccupied;
    protected String cleanerUsername;
    protected RoomStatus status;
    protected List<RoomQuality> qualities;

    public Room(int number, RoomType type, String cleanerUsername, List<RoomQuality> qualities) {
        this.type = type;
        this.number = number;
        this.datesOccupied = new ArrayList<LocalDate>();
        this.cleanerUsername = cleanerUsername;
        this.status = RoomStatus.AVAILABLE;
        this.qualities = qualities;
    }

    public Room(int number, RoomType type, List<LocalDate> datesOccupied, String cleanerUsername, RoomStatus status, List<RoomQuality> qualities) {
        this.type = type;
        this.number = number;
        this.datesOccupied = datesOccupied;
        this.cleanerUsername = cleanerUsername;
        this.status = status;
        this.qualities = qualities;
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("[");
    	for (int i = 0; i < this.qualities.size(); i++) {
    		sb.append(this.qualities.get(i).toStringFile());
    		if (i != this.qualities.size() - 1) {
    			sb.append(", ");
    		}
    	}
    	sb.append("]");
    	
        return this.number + "," + this.type.toStringFile() + "," + this.cleanerUsername + "," + this.status.toStringFile() + "," + this.datesOccupied.toString() + "," + sb.toString();
    }

    public int getNumber() {
        return this.number;
    }
    
	public void setNumber(int number) {
		this.number = number;
	}
    
	public List<LocalDate> getDatesOccupied() {
		return this.datesOccupied;
	}
	
	public void setDatesOccupied(List<LocalDate> datesOccupied) {
		this.datesOccupied = datesOccupied;
	}
	
	public String getCleanerUsername() {
		return this.cleanerUsername;
	}
	
	public void setCleanerUsername(String cleanerUsername) {
		this.cleanerUsername = cleanerUsername;
	}
	
	public RoomStatus getStatus() {
		return this.status;
	}
	
	public void setStatus(RoomStatus status) {
		this.status = status;
	}
	
    public RoomType getType() {
        return this.type;
    }
    
	public void setType(RoomType type) {
		this.type = type;
	}
	
	public List<RoomQuality> getQualities() {
		return this.qualities;
	}
	
	public void setQualities(List<RoomQuality> qualities) {
		this.qualities = qualities;
	}

    public void addDate(LocalDate date) {
        this.datesOccupied.add(date);
    }

    public void removeDate(LocalDate date) {
        this.datesOccupied.remove(date);
    }

    public boolean checkIfAvailable(LocalDate startDate, LocalDate endDate) {
        for (LocalDate occupiedDate : datesOccupied) {
            if ((occupiedDate.isEqual(startDate) || occupiedDate.isAfter(startDate)) && occupiedDate.isBefore(endDate)) {
                return false;
            }
        }
        return true;
    }
    
    public void removeReservation(LocalDate date) {
        if (datesOccupied.contains(date)) {
            datesOccupied.remove(date);
        }
    }
    
    public void addReservation(LocalDate start, LocalDate end) {
    	while(start.isBefore(end)) {
    		this.datesOccupied.add(start);
    		start = start.plusDays(1);
    	}
    }
}