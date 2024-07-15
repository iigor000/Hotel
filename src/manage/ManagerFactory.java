package manage;

import util.AppSettings;

public class ManagerFactory {
    private AppSettings appSettings;
    private RoomManager roomManager;
    private PriceListManager priceListManager;
    private ReservationManager reservationManager;
    private UserManager userManager;
    private CleanersManager cleanersManager;
    private ReservationRequestManager reservationRequestManager;

    // Klasa je singleton, kako bi mogli pristupiti listama odakle nam budu trebale
    private static ManagerFactory instance;

    public static ManagerFactory getInstance() {
        if (instance == null) {
            return null;
        }
        return instance;
    }

    public static ManagerFactory getInstance(AppSettings appSettings) {
        if (instance == null) {
            instance = new ManagerFactory(appSettings);
        }
        return instance;
    }

    // Konstruktor uzima appsettings i pravi nove managere
    private ManagerFactory(AppSettings appSettings) {
        this.appSettings = appSettings;
        this.priceListManager = new PriceListManager(this.appSettings.getPriceListsFilename());
        this.reservationManager = new ReservationManager(this.appSettings.getReservationsFilename());
        this.roomManager = new RoomManager(this.appSettings.getRoomsFilename());
        this.userManager = new UserManager(this.appSettings.getUsersFilename());
        this.cleanersManager = new CleanersManager(this.appSettings.getCleanersFilename());
        this.reservationRequestManager = new ReservationRequestManager(this.appSettings.getReservationRequestsFilename());
    }

    public RoomManager getRoomManager() {
        return this.roomManager;
    }

    public PriceListManager getPriceListManager() {
        return this.priceListManager;
    }

    public ReservationManager getReservationManager() {
        return this.reservationManager;
    }

    public UserManager getUserManager() {
        return this.userManager;
    }
    
	public CleanersManager getCleanersManager() {
		return this.cleanersManager;
	}
	
	public ReservationRequestManager getReservationRequestManager() {
		return this.reservationRequestManager;
	}

    // Ucitavamo sve podatke iz fajlova
    public void loadData() {
        this.roomManager.loadData();
        this.priceListManager.loadData();
        this.reservationManager.loadData();
        this.userManager.loadData();
        this.cleanersManager.loadData();
        this.reservationRequestManager.loadData();
    }

    // Cuvamo sve podatke u fajlove
    public void saveData() {
        this.roomManager.saveData();
        this.priceListManager.saveData();
        this.reservationManager.saveData();
        this.userManager.saveData();
        this.cleanersManager.saveData();
        this.reservationRequestManager.saveData();
    }
}
