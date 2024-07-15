package model;

public enum RoomStatus {
    AVAILABLE(1),
    OCCUPIED(2),
    CLEANING(3);
    
    int room;
    
	private RoomStatus() {
	}

	private RoomStatus(int i) {
		this.room = i;
	}
	
	public String toStringFile() {
		switch (room) {
		case 1:
			return "AVAILABLE";
		case 2:
			return "OCCUPIED";
		case 3:
			return "CLEANING";
		default:
			return "Invalid";
		}
	}
	
	@Override
	public String toString() {
		switch (room) {
		case 1:
			return "Slobodna";
		case 2:
			return "Zauzeta";
		case 3:
			return "Ciscenje";
		default:
			return "Invalid";
		}
	}
}
