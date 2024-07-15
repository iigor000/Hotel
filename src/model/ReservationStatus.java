package model;

// Enum koji se koristi za status rezervacije
public enum ReservationStatus {
    WAITING(1), CONFIRMED(2), REJECTED(3) ,CANCELED(4);
    int status;

    private ReservationStatus() {
    }
    private ReservationStatus(int i) {
        this.status = i;
    }

    @Override
    public String toString() {
        switch (status) {
            case 1:
                return "NA CEKANJU";
            case 2:
                return "POTVRDJENO";
            case 3:
                return "ODBIJENO";
            case 4:
                return "OTKAZANO";
            default:
                return "Invalid";
        }
    }
    
	public String toStringFile() {
		switch (status) {
		case 1:
			return "WAITING";
		case 2:
			return "CONFIRMED";
		case 3:
			return "REJECTED";
		case 4:
			return "CANCELED";
		default:
			return "Invalid";
		}
	}
}
