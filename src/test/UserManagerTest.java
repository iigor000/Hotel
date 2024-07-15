package test;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import manage.CleanersManager;
import manage.ManagerFactory;
import manage.PriceListManager;
import manage.ReservationManager;
import manage.ReservationRequestManager;
import manage.RoomManager;
import manage.UserManager;
import model.EmployeeType;
import model.ReservationStatus;
import model.RoomQuality;
import model.RoomType;
import util.AppSettings;

public class UserManagerTest {
	public static AppSettings appSettings = new AppSettings("database/Rooms.csv", "database/Reservations.csv", "database/Prices.csv", "database/Users.csv", "database/Cleaners.csv", "database/Requests.csv");
    public static ManagerFactory controllers = ManagerFactory.getInstance(appSettings);
	public static CleanersManager cleaners = ManagerFactory.getInstance().getCleanersManager();
	public static PriceListManager priceList = ManagerFactory.getInstance().getPriceListManager();
	public static ReservationManager reservation = ManagerFactory.getInstance().getReservationManager();
	public static UserManager user = ManagerFactory.getInstance().getUserManager();
	public static ReservationRequestManager request = ManagerFactory.getInstance().getReservationRequestManager();
	public static RoomManager room = ManagerFactory.getInstance().getRoomManager();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("Pocetak testa za menadzera korisnika");
		
		user.addUser("Petar", "Peric", true, LocalDate.of(1999, 10, 2), "06129184921", "Bulevar Oslobodjenja 12", "pera", "pera123", 0, 4, 10, EmployeeType.ADMINISTRATOR);
		user.addUser("Milos", "Milosic", true, LocalDate.of(2001, 8, 12), "0613981239", "Bulevar Oslobodjenja 14", "mika", "mika123", 0, 2, 4, EmployeeType.RECEPTIONIST);
		user.addUser("Janoslava", "Janic", false, LocalDate.of(1998, 4, 14), "06384918347", "Bulevar Oslobodjenja 15", "jana", "jana123", 0, 1, 5, EmployeeType.CLEANER);
		user.addUser("Aleksa", "Aleksic", true, LocalDate.of(2000, 8, 12), "0613981239", "Bulevar Oslobodjenja 14", "alek", "alek123", 0, 2, 4, EmployeeType.RECEPTIONIST);
		user.addUser("Ana", "Anic", false, LocalDate.of(2004, 11, 2), "06583182391", "Bulevar Oslobodjenja 2", "ana", "ana123");
		user.addUser("Jovana", "Jovanovic", false, LocalDate.of(2003, 12, 2), "06489123891", "Bulevar Oslobodjenja 3", "jovana", "jovana123", 0, 3, 2, EmployeeType.CLEANER);
		
		HashMap<String, Double> prices = new HashMap<>();
		prices.put("SINGLE", 100.0);
		prices.put("DOUBLE1_1", 120.0);
		prices.put("DOUBLE2", 130.0);
		prices.put("TRIPLE1_1_1", 140.0);
		prices.put("TRIPLE1_2", 150.0);
		prices.put("dorucak", 10.0);
		prices.put("rucak", 20.0);
		prices.put("vecera", 15.0);
		prices.put("spa", 25.0);
		
		priceList.addPriceList(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 22), prices);
		
		List<RoomQuality> qualities = new ArrayList<RoomQuality>();
		qualities.add(RoomQuality.BALCONY);
		qualities.add(RoomQuality.TV);
		qualities.add(RoomQuality.AC);
		qualities.add(RoomQuality.SMOKER);
		room.addRoom(101, RoomType.SINGLE, null, qualities);
		List<RoomQuality> qualities1 = new ArrayList<RoomQuality>(qualities);
		qualities1.remove(RoomQuality.AC);
		room.addRoom(102, RoomType.DOUBLE1_1, null, qualities1);
		List<RoomQuality> qualities2 = new ArrayList<RoomQuality>(qualities1);
		qualities2.remove(RoomQuality.SMOKER);
		qualities2.add(RoomQuality.NONSMOKER);
		room.addRoom(103, RoomType.DOUBLE2, null, qualities2);
		List<RoomQuality> qualities3 = new ArrayList<RoomQuality>(qualities2);
		qualities3.remove(RoomQuality.TV);
		room.addRoom(104, RoomType.TRIPLE1_1_1, null, qualities3);
		List<RoomQuality> qualities4 = new ArrayList<RoomQuality>(qualities3);
		qualities4.remove(RoomQuality.BALCONY);
		qualities4.add(RoomQuality.AC);
		room.addRoom(105, RoomType.TRIPLE1_2, null, qualities4);
		
		List<String> services = new ArrayList<String>();
		services.add("dorucak");
		services.add("rucak");
		services.add("vecera");
		reservation.addReservation(101, "ana", LocalDate.of(2024, 3, 12), LocalDate.of(2024, 3, 22), 0, ReservationStatus.CONFIRMED, services, LocalDate.of(2024, 2, 15), LocalDate.of(2024, 3, 1));
		reservation.addReservation(101, "ana", LocalDate.of(2024, 7, 12), LocalDate.of(2024, 7, 22), 0, ReservationStatus.CONFIRMED, services, LocalDate.of(2024, 2, 15), LocalDate.of(2024, 3, 1));
		List<String> services1 = new ArrayList<String>(services);
		services1.add("spa");
		reservation.addReservation(102, "ana", LocalDate.of(2024, 4, 12), LocalDate.of(2024, 4, 22), 0, ReservationStatus.REJECTED, services1, LocalDate.of(2024, 3, 15), LocalDate.of(2024, 4, 1));
		List<String> services2 = new ArrayList<String>(services1);
		services.remove("spa");
		reservation.addReservation(103, "ana", LocalDate.of(2024, 5, 12), LocalDate.of(2024, 5, 22), 0, ReservationStatus.CANCELED, services2, LocalDate.of(2024, 4, 15), LocalDate.of(2024, 5, 1));
		
		request.addRequest(LocalDate.of(2024, 6, 12), LocalDate.of(2024, 6, 22), RoomType.TRIPLE1_1_1, qualities3, services2, 0.0, LocalDate.of(2024, 3, 1), "ana");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("Kraj testa za menadzera korisnika");
	}


	@Test
	public void testAddGuest() {
		boolean success = user.addUser("Mia", "Ugljesic", false, LocalDate.of(2004, 3, 1), "064892198319", "Bulevar Cara Lazara 90", "mia", "mia123");
		assertTrue(success);
	}
	
	@Test
	public void testAddEmoloyee() {
		boolean success = user.addUser("Jovan", "Jovanovic", true, LocalDate.of(2002, 4, 10), "0623418347", "Bulevar Oslobodjenja 22", "jova", "jova123", 0, 3, 2, EmployeeType.CLEANER);
        assertTrue(success);
	}
	
	@Test
	public void testRemoveUser() {
		boolean success = user.removeUser("alek");
        assertTrue(success);
    }
	
	
	@Test
	public void testGetUser() {
		assertNotNull(user.getUser("jana"));
	}
	
	@Test
	public void testCheckIfUserExists() {
		assertTrue(user.checkIfUserExists("jana"));
	}
	
	@Test
	public void testLogin() {
		assertNotNull(user.login("jana", "jana123"));
	}

	@Test
	public void testCalculateSpendings() {
		assertNotEquals(0.0, user.calculateSpendings(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 6, 1)), 0.1);
	}

}
