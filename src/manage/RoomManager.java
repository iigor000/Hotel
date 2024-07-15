package manage;

import model.Cleaner;
import model.PriceList;
import model.Reservation;
import model.ReservationStatus;
import model.Room;
import model.RoomQuality;
import model.RoomStatus;
import model.RoomType;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomManager {
    private List<Room> rooms;
    private String roomFile;

    public RoomManager(String roomFile) {
        this.roomFile = roomFile;
        this.rooms = new ArrayList<Room>();
    }

    public boolean loadData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.roomFile));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String data = line;
                int commaIndex = data.indexOf(',');

                String number = data.substring(0, commaIndex);
                String rest = data.substring(commaIndex + 1);

                commaIndex = rest.indexOf(',');

                String type = rest.substring(0, commaIndex);
                rest = rest.substring(commaIndex + 1);

                commaIndex = rest.indexOf(',');

                String CleanerUsername = rest.substring(0, commaIndex);
                rest = rest.substring(commaIndex + 1);
                
                String status = rest.substring(0, rest.indexOf(','));
                String listString = rest.substring(rest.indexOf(',') + 2, rest.indexOf(']') + 1);
                
                rest = rest.substring(rest.indexOf(']') + 2);

                String qualitiesString = rest.substring(rest.indexOf('[') + 1, rest.indexOf(']'));
                
                List<LocalDate> list = new ArrayList<>();

                String[] elements = listString.substring(0, listString.length() - 1).split(", ");

                for (int i = 0; i < elements.length; i++) {
                	if (!elements[i].equals("")) {
                		list.add(LocalDate.parse(elements[i]));
                	}
                }
                
                String[] qualities = qualitiesString.split(", ");
                
                List<RoomQuality> roomQualities = new ArrayList<RoomQuality>();
                
                
				for (int i = 0; i < qualities.length; i++) {
					if (!qualities[i].equals("")) {
						roomQualities.add(RoomQuality.valueOf(qualities[i]));
					}
				}

                Room room = null;

                if (CleanerUsername.equals("null"))
                    room = new Room(Integer.parseInt(number), RoomType.valueOf(type), list, null, RoomStatus.valueOf(status), roomQualities);
                else
                    room = new Room(Integer.parseInt(number), RoomType.valueOf(type), list, CleanerUsername, RoomStatus.valueOf(status), roomQualities);
                this.rooms.add(room);
            }
            reader.close();
        }catch (IOException e){
            return false;
        }

        return true;
    }

    public boolean saveData() {
        try {
            StringBuilder file = new StringBuilder();
            FileWriter writer = new FileWriter(this.roomFile);
            for (Room room : this.rooms) {
                file.append(room.toString() + "\n");
            }
            writer.write(file.toString());
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Room getRoom(int number) {
        for (Room room : this.rooms) {
            if (room.getNumber() == number) {
                return room;
            }
        }
        return null;
    }
    
	public List<Room> getRooms(RoomType type) {
		List<Room> rooms = new ArrayList<>();
		for (Room room : this.rooms) {
			if (room.getType() == type) {
				rooms.add(room);
			}
		}
		return rooms;
	}

	public List<Room> getRooms() {
		return this.rooms;
	}

    public void addRoom(int number, RoomType type, String cleanerUsername, List<RoomQuality> qualities) {
        this.rooms.add(new Room(number, type, cleanerUsername, qualities));
    }
    
	public void addRoom(int number, RoomType type, List<LocalDate> datesOccupied, String cleanerUsername, RoomStatus status, List<RoomQuality> qualities) {
		this.rooms.add(new Room(number, type, datesOccupied, cleanerUsername, status, qualities));
	}

	// Trazi slobodne sobe za odredjeni period
    public List<Room> getAvailableRooms(LocalDate startDate, LocalDate endDate) {
        List<Room> availableRooms = new ArrayList<>();
        for (Room room : this.rooms) {
            if (room.checkIfAvailable(startDate, endDate)) {
                availableRooms.add(room);
            }
        }
        return availableRooms;
    }
    
	// Trazi slobodnu sobu za odredjeni tip sobe
	public int checkAvailable(LocalDate start, LocalDate end, RoomType type) {
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		for (Room room : roomManager.getRooms(type)) {
				if (room.checkIfAvailable(start, end)) {
					return room.getNumber();
			}
		}
		return 0;
	}
	
	// Trazi slobodnu sobu za odredjeni tip sobe i kvalitete sobe
	public int checkAvailable(LocalDate start, LocalDate end, RoomType type, List<RoomQuality> qualities) {
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		for (Room room : roomManager.getRooms(type)) {
				if (room.checkIfAvailable(start, end)) {
					if (room.getQualities().containsAll(qualities)) {
						return room.getNumber();
					}
			}
		}
		return 0;
	}
	
	public boolean checkAvailable(int number, LocalDate start, LocalDate end) {
		Room room = getRoom(number);
		return room.checkIfAvailable(start, end);
	}
    
	// Pronalazi cistaca sa najmanje soba
    public String findCleanerWithLeastRooms() {
    	UserManager userManager = ManagerFactory.getInstance().getUserManager();
    	List<Cleaner> cleaners = userManager.getCleaners();
    	
        Map<String, Integer> cleanerRoomCount = new HashMap<>();
        
		for (Cleaner cleaner : cleaners) {
			cleanerRoomCount.put(cleaner.getUsername(), 0);
		}
        
        for (Room room : this.rooms) {
            String cleanerUsername = room.getCleanerUsername();
            if (cleanerUsername != null) {
                cleanerRoomCount.put(cleanerUsername, cleanerRoomCount.get(cleanerUsername) + 1);
            }
        }

        String cleanerWithLeastRooms = null;
        int minRoomCount = Integer.MAX_VALUE;
        for (Map.Entry<String, Integer> entry : cleanerRoomCount.entrySet()) {
            if (entry.getValue() < minRoomCount) {
                cleanerWithLeastRooms = entry.getKey();
                minRoomCount = entry.getValue();
            }
        }

        return cleanerWithLeastRooms;
    }
    
    // Odjavljivanje gosta iz sobe, postavljanje sobe na ciscenje
    public void checkOut(int roomNumber, Reservation reservation) {
		Room room = getRoom(roomNumber);
		room.setStatus(RoomStatus.CLEANING);
		room.setCleanerUsername(findCleanerWithLeastRooms());
		for (LocalDate date = reservation.getCheckIn(); date
				.isBefore(reservation.getCheckOut()); date = date.plusDays(1)) {
			room.removeReservation(date);
		}
    }
    
    public List<Room> getRoomsByCleaner(String cleanerUsername) {
    	List<Room> rooms = new ArrayList<Room>();
		for (Room room : this.rooms) {
			if (room.getCleanerUsername() != null && room.getCleanerUsername().equals(cleanerUsername) && room.getStatus() == RoomStatus.CLEANING) {
				rooms.add(room);
			}
		}
		return rooms;
    }
    
    public int getRoomOfType(RoomType type) {
		for (Room room : this.rooms) {
			if (room.getType() == type) {
				return room.getNumber();
			}
		}
		return 0;
    }
    
    public boolean reserveRoom(int number, LocalDate start, LocalDate end) {
    	Room room = getRoom(number);
		if (room != null) {
			room.addReservation(start, end);
			return true;
		}
		return false;
    }
    
	public boolean cleanRoom(int roomNumber) {
		Room room = getRoom(roomNumber);
		if (room.getStatus() != RoomStatus.CLEANING || room.getCleanerUsername() == null) {
			return false;
		}
		room.setStatus(RoomStatus.AVAILABLE);
		String cleaner = room.getCleanerUsername();
		room.setCleanerUsername(null);
		
		CleanersManager cleanersManager = ManagerFactory.getInstance().getCleanersManager();
		cleanersManager.addCleanedRoom(cleaner, LocalDate.now());
		return true;
	}
	
	public boolean checkNumberAvailable(int number) {
		for (Room room : this.rooms) {
			if (room.getNumber() == number) {
				return false;
			}
		}
		return true;
	}
	
	public boolean removeRoom(int number) {
		for (Room room : this.rooms) {
			if (room.getNumber() == number && room.getStatus() == RoomStatus.AVAILABLE && room.getDatesOccupied().size() == 0) {
				this.rooms.remove(room);
				return true;
			}
		}
		return false;
	}

	public boolean isAvailable(int number) {
		for (Room room : this.rooms) {
			if (room.getNumber() == number && room.getStatus() == RoomStatus.AVAILABLE && room.getDatesOccupied().size() == 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean checkRoomAvailable(int number, LocalDate startDate, LocalDate endDate) {
		for (Room room : this.rooms) {
			if (room.getNumber() == number) {
				return room.checkIfAvailable(startDate, endDate);
			}
		}
		return false;
	}
	
	// Prihod za sobu u odredjenom periodu
	public int getEarnings(int number, LocalDate start, LocalDate end) {
		int earnings = 0;
		ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
		PriceListManager priceListManager = ManagerFactory.getInstance().getPriceListManager();
		Room room = getRoom(number);
		List<Reservation> reservations = reservationManager.getReservationsByRoom(number);
		for (Reservation reservation : reservations) {
			if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
				continue;
			}
			LocalDate checkIn = reservation.getCheckIn();
			LocalDate checkOut = reservation.getCheckOut();
			while (checkIn.isBefore(checkOut)) {
				if (checkIn.isAfter(start) && checkIn.isBefore(end)) {
					PriceList priceList = priceListManager.getPriceList(checkIn);
					earnings += priceList.getPrices().get(room.getType().toStringFile());
				}
				checkIn = checkIn.plusDays(1);
			}
		}
		return earnings;
	}
	
	// Broj nocenja za sobu u odredjenom periodu
	public int getNights(int number, LocalDate start, LocalDate end) {
		int nights = 0;
		ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
		List<Reservation> reservations = reservationManager.getReservationsByRoom(number);
		for (Reservation reservation : reservations) {
			if (reservation.getStatus() != ReservationStatus.CONFIRMED) {
				continue;
			}
			LocalDate checkIn = reservation.getCheckIn();
			LocalDate checkOut = reservation.getCheckOut();
			while (checkIn.isBefore(checkOut)) {
				if (checkIn.isAfter(start) && checkIn.isBefore(end)) {
					nights++;
				}
				checkIn = checkIn.plusDays(1);
			}
		}
		return nights;
	}
}
