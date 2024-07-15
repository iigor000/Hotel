package manage;

import model.Reservation;
import model.ReservationStatus;
import model.Room;
import model.RoomStatus;
import model.RoomType;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationManager {
    private List<Reservation> reservations;
    private String reservationFile;

    public ReservationManager(String reservationFile) {
        this.reservationFile = reservationFile;
        this.reservations = new ArrayList<Reservation>();
    }

    // Ucitavamo rezervacije iz fajla
    public boolean loadData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.reservationFile));
            String line = "";
            while ((line = reader.readLine()) != null) {
                	String[] data = line.split(",");
                    List<String> services = new ArrayList<>();
                    for (int i = 9; i < data.length; i++) {
                        if (i == 9) {
                        	data[i] = data[i].replace("]", "");
                            services.add(data[i].substring(1));
                        } else if (i == data.length - 1) {
                            services.add(data[i].substring(1, data[i].length() - 1));
                        } else {
                            services.add(data[i].substring(1, data[i].length()));
                        }
                    }
                    Reservation reservation = null;
                    if (data[8].equals("null")) {
                    	reservation = new Reservation(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], LocalDate.parse(data[3]), LocalDate.parse(data[4]), Double.parseDouble(data[5]), ReservationStatus.valueOf(data[6]), services, LocalDate.parse(data[7]), null);
                    }else {
                    	reservation = new Reservation(Integer.parseInt(data[0]), Integer.parseInt(data[1]), data[2], LocalDate.parse(data[3]), LocalDate.parse(data[4]), Double.parseDouble(data[5]), ReservationStatus.valueOf(data[6]), services, LocalDate.parse(data[7]), LocalDate.parse(data[8]));
                    }
                    this.reservations.add(reservation);
            }
            reader.close();
        }catch (IOException e) {
            return false;
        }
        return true;
    }

    // Cuvamo podatke o rezervacijama
    public boolean saveData() {
        try{
            StringBuilder file = new StringBuilder();
            FileWriter writer = new FileWriter(this.reservationFile);
            for (Reservation reservation : this.reservations) {
                file.append(reservation.toString() + "\n");
            }
            writer.write(file.toString());
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    // Dodajemo rezervaciju u listu rezervacija
    public boolean addReservation(int roomNumber, String guestUsername, LocalDate checkIn, LocalDate checkOut, double price, ReservationStatus status, List<String> services, LocalDate creationDate, LocalDate actionDate) {
        this.reservations.add(new Reservation(this.getNextId(), roomNumber, guestUsername, checkIn, checkOut, price, status, services, creationDate, actionDate));
		if (status == ReservationStatus.CONFIRMED) {
			Room room = ManagerFactory.getInstance().getRoomManager().getRoom(roomNumber);
			for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {
				room.addDate(date);
			}
		}
		return true;
    }
    
	public int getTotalCostByGuest(String username) {
		int total = 0;
			for (Reservation reservation : getReservationsByUsername(username)) {
				if (reservation.getStatus() != ReservationStatus.CANCELED && reservation.getStatus() != ReservationStatus.REJECTED) {
				total += reservation.getPrice();
				}
			}
		return total;
	}

    // Vraca sledeci id
    public int getNextId(){
        int max = 0;
        for (Reservation reservation : this.reservations) {
            if (reservation.getId() > max) {
                max = reservation.getId();
            }
        }
        return max + 1;
    }

    // Vraca listu svih rezervacija 
    public List<Reservation> getReservations() {
        return this.reservations;
    }

    // Vraca rezervaciju po imenu gosta
    public List<Reservation> getReservationsByUsername(String username) {
        List<Reservation> reservations = new ArrayList<Reservation>();
        for (Reservation reservation : this.reservations) {
            if (reservation.getGuestUsername().equals(username) && (reservation.getCheckIn().isAfter(LocalDate.now()) || reservation.getCheckIn().isEqual(LocalDate.now())) ) {
                reservations.add(reservation);
            }
        }
        return reservations;
    }
    
    // Odbijanje rezervacije od strane hotela
	public void rejectReservation(int id) {
		for (Reservation reservation : this.reservations) {
			if (reservation.getId() == id) {
				reservation.setStatus(ReservationStatus.REJECTED);
				Room room = ManagerFactory.getInstance().getRoomManager().getRoom(reservation.getRoomNumber());
				for (LocalDate date = reservation.getCheckIn(); date
						.isBefore(reservation.getCheckOut()); date = date.plusDays(1)) {
					room.removeReservation(date);
				}
			}
		}
	}
	
	// Otkazivanje rezervacije od strane gosta
	public void cancelReservation(int id) {
		for (Reservation reservation : this.reservations) {
			if (reservation.getId() == id) {
				reservation.setStatus(ReservationStatus.CANCELED);
				Room room = ManagerFactory.getInstance().getRoomManager().getRoom(reservation.getRoomNumber());
				for (LocalDate date = reservation.getCheckIn(); date
						.isBefore(reservation.getCheckOut()); date = date.plusDays(1)) {
					room.removeReservation(date);
				}
			}
		}
	}
	
	public boolean ChangeRoom(Reservation reservation, int number) {
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		Room room = roomManager.getRoom(reservation.getRoomNumber());
		Room newRoom = roomManager.getRoom(number);
		LocalDate start = reservation.getCheckIn();
		LocalDate end = reservation.getCheckOut();
		while (start.isBefore(end)) {
			room.removeDate(start);
			if (reservation.getStatus() == ReservationStatus.CONFIRMED || reservation.getStatus() == ReservationStatus.WAITING) {
				newRoom.addDate(start);
			}
			start = start.plusDays(1);
		}
		reservation.setRoomNumber(number);
		return true;
	}
	
	// Vraca sve rezervacije koje ce biti u buducniosti
	public List<Reservation> getCurrentReservations(){
		List<Reservation> reservations = new ArrayList<Reservation>();
		for (Reservation reservation : this.reservations) {
			if ((reservation.getCheckOut().isAfter(LocalDate.now()) || reservation.getCheckOut().isEqual(LocalDate.now())) && reservation.getStatus() == ReservationStatus.CONFIRMED) {
				reservations.add(reservation);
			}
		}
		return reservations;
	}
	
	// Prijava gosta u sobu
	public boolean checkIn(Reservation reservation) {
		 RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
	        Room room = roomManager.getRoom(reservation.getRoomNumber());
	        if (room.getStatus() == RoomStatus.OCCUPIED || room.getStatus() == RoomStatus.CLEANING || reservation.getStatus() != ReservationStatus.CONFIRMED || !reservation.getCheckIn().equals(LocalDate.now())) {
	            return false;
	        }
	        room.setStatus(RoomStatus.OCCUPIED);
	        return true;
	}
	
	// Danasnje prijave
	public List<Reservation> getTodaysCheckIn(){
		List<Reservation> reservations = new ArrayList<Reservation>();
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
        for (Reservation reservation : this.reservations) {
            if (reservation.getCheckIn().isEqual(LocalDate.now()) && reservation.getStatus() == ReservationStatus.CONFIRMED) {
            	Room room = roomManager.getRoom(reservation.getRoomNumber());
            	if (room.getStatus() == RoomStatus.AVAILABLE) {
            		reservations.add(reservation);
            	}
            }
        }
        return reservations;
    }
	
	// Danasnje odjave
	public List<Reservation> getTodaysCheckOut(){
		List<Reservation> reservations = new ArrayList<Reservation>();
        RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
        for (Reservation reservation : this.reservations) {
            if (reservation.getCheckOut().isEqual(LocalDate.now()) && reservation.getStatus() == ReservationStatus.CONFIRMED) {
            	Room room = roomManager.getRoom(reservation.getRoomNumber());
            	if (room.getStatus() == RoomStatus.OCCUPIED) {
            		reservations.add(reservation);
            	}
            }
        }
        return reservations;
    }
	
	// Ukupna zarada hotela za odredjeni period
	public double getEarnings(LocalDate start, LocalDate end) {
		double earnings = 0;
		for (Reservation reservation : this.reservations) {
			if (reservation.getCheckIn().isAfter(start) && reservation.getCheckOut().isBefore(end)
					&& reservation.getStatus() == ReservationStatus.CONFIRMED) {
				earnings += reservation.getPrice();
			}
		}
		return earnings;
	}
	
	// Sve rezervacije za odredjenu sobu
	public List<Reservation> getReservationsByRoom(int number) {
		List<Reservation> reservations = new ArrayList<Reservation>();
		for (Reservation reservation : this.reservations) {
			if (reservation.getRoomNumber() == number) {
				reservations.add(reservation);
			}
		}
		return reservations;
	}
	
	// Zarada za odredjeni tip sobe
	public double getEarningsRoomType(LocalDate start, LocalDate end, RoomType type) {
		double earnings = 0;
		RoomManager roomManager = ManagerFactory.getInstance().getRoomManager();
		for (Reservation reservation : this.reservations) {
			if (roomManager.getRoom(reservation.getRoomNumber()).getType() == type) {
				if (reservation.getCheckIn().isAfter(start) && reservation.getCheckOut().isBefore(end)
						&& reservation.getStatus() == ReservationStatus.CONFIRMED) {
					earnings += reservation.getPrice();
				}
			}
		}
		return earnings;
	}
	
	// Broj rezervacija po akcijama uradjenim (odobrenje, odbijanje, otkazivanje)
	public int getNumberOfReservationsByAction(LocalDate start, LocalDate end, ReservationStatus status) {
		int number = 0;
		for (Reservation reservation : this.reservations) {
			if (reservation.getActionDate() != null) {
				if (reservation.getActionDate().isAfter(start) && reservation.getActionDate().isBefore(end) && reservation.getStatus().equals(status)) {
					number++;
				}
			}
		}
		return number;
	}
	
	// Broj rezervacija po statusu
	public int getNumberOfReservationsByStatus(LocalDate start, LocalDate end, ReservationStatus status) {
		int number = 0;
		for (Reservation reservation : this.reservations) {
			if (reservation.getCreationDate().isAfter(start) && reservation.getCreationDate().isBefore(end) && reservation.getStatus().equals(status)) {
				number++;
			}
		}
		return number;
	}
	
	// Brisanje rezervacije
	public boolean removeReservation(int id) {
		for (Reservation reservation : this.reservations) {
			if (reservation.getId() == id) {
				if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
					return false;
				}
				this.reservations.remove(reservation);
				return true;
			}
		}
		return false;
	}
}
