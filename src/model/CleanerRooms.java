package model;

import java.time.LocalDate;
import java.util.List;

public class CleanerRooms {
	private String cleanerUsername;
	private List<LocalDate> dates;
	
	public CleanerRooms(String cleanerUsername, List<LocalDate> dates) {
		this.cleanerUsername = cleanerUsername;
		this.dates = dates;
	}
	
	public String getCleanerUsername() {
		return cleanerUsername;
	}
	
	public List<LocalDate> getDates() {
		return dates;
	}
	
	public void setCleanerUsername(String cleanerUsername) {
		this.cleanerUsername = cleanerUsername;
	}
	
	public void setDates(List<LocalDate> dates) {
		this.dates = dates;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(cleanerUsername + ",");
		sb.append(dates.toString());
		return sb.toString();
	}
	
	public void addRoom(LocalDate date) {
		dates.add(date);
	}
	
	public int getCleanedRoomsByDate(LocalDate start, LocalDate end) {
		int count = 0;
		for (LocalDate date : dates) {
			if (date.isAfter(start) && date.isBefore(end)) {
				count++;
			}
		}
		return count;
	}
}
