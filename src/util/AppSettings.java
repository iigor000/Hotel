package util;

public class AppSettings {
    private String  RoomsFilename, ReservationsFilename, PriceListsFilename, UsersFileName, CleanersFilename, ReservationRequestsFilename;

    // Appsettings sluzi da uzima imena fajlova odakle uzimamo podatke
    public AppSettings(String roomsFilename, String reservationsFilename, String priceListsFilename, String usersFileName, String cleanersFilename, String reservationRequestsFilename) {
        this.RoomsFilename = roomsFilename;
        this.ReservationsFilename = reservationsFilename;
        this.PriceListsFilename = priceListsFilename;
        this.UsersFileName = usersFileName;
        this.CleanersFilename = cleanersFilename;
        this.ReservationRequestsFilename = reservationRequestsFilename;
    }

    public String getRoomsFilename() {
        return RoomsFilename;
    }

    public String getUsersFilename() {
        return UsersFileName;
    }

    public String getReservationsFilename() {
        return ReservationsFilename;
    }

    public String getPriceListsFilename() {
        return PriceListsFilename;
    }

	public String getCleanersFilename() {
		return CleanersFilename;
	}
	
	public String getReservationRequestsFilename() {
		return ReservationRequestsFilename;
	}

}
