package manage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.CleanerRooms;

public class CleanersManager {
	private String cleanersFile;
	private List<CleanerRooms> cleaners;
	
	// Menadzer cuva podakte za sobarice, kada je koja sobarica cistila sobu
	public CleanersManager(String CleanersFile) {
		this.cleanersFile = CleanersFile;
		this.cleaners = new ArrayList<CleanerRooms>();
	}
	
	
	 public boolean loadData() {
	        try {
	            BufferedReader reader = new BufferedReader(new FileReader(this.cleanersFile));
	            String line = "";
	            while ((line = reader.readLine()) != null) {
	            	String data = line;
	                int commaIndex = data.indexOf(',');
	                
	            	// Ime je do prvog zareza, a ostatak su datumi
	            	String cleaner = data.substring(0, commaIndex);
	                String rest = data.substring(commaIndex + 1);
	                
	                List<LocalDate> list = new ArrayList<>();
	
	                // Sklanjamo zagrade i pravimo listu sa stringovima datuma
	                String[] elements = rest.substring(1, rest.length() - 1).split(", ");
	                
					for (int i = 0; i < elements.length; i++) {
						if (!elements[i].equals("")) {
						list.add(LocalDate.parse(elements[i]));
						}
					}
	                
					CleanerRooms cleanerRooms = new CleanerRooms(cleaner, list);
	                cleaners.add(cleanerRooms);
	                }
	            reader.close();
	        } catch (FileNotFoundException e) {
	            return false;
	        } catch (IOException e) {
	            return false;
	        }
	        return true;
	 }
	 
	// Cuvanje podataka o sobaricama 
	public boolean saveData() {
		 try {
	            StringBuilder file = new StringBuilder();
	            FileWriter writer = new FileWriter(this.cleanersFile);
				for (CleanerRooms rooms: this.cleaners) {
					file.append(rooms.toString() + "\n");
				}
	            writer.write(file.toString());
	            writer.close();
	        } catch (IOException e) {
	            return false;
	        }
	        return true;
	}

	
	// Dodaje sobaricu u listu sobarica
	public boolean addCleaner(String cleaner) {
		cleaners.add(new CleanerRooms(cleaner, new ArrayList<LocalDate>()));
		return true;
	}
	
	// Dodaje sobu koju je sobarica ocistila
	public boolean addCleanedRoom(String cleaner, LocalDate date) {
		for (CleanerRooms cleanerRooms : cleaners) {
            if (cleanerRooms.getCleanerUsername().equals(cleaner)) {
                cleanerRooms.addRoom(date);
                return true;
            }
        }
		return false;
	}
	
	// Vraca broj soba koje je sobarica ocistila
	public int getCleanedRoomsByDate(String cleaner, LocalDate start, LocalDate end) {
		int count = 0;
		for (CleanerRooms cleanerRooms : cleaners) {
			if (cleanerRooms.getCleanerUsername().equals(cleaner)) {
				count = cleanerRooms.getCleanedRoomsByDate(start, end);
			}
		}
        return count;
    }

	// Vraca listu sobarica i njihovih soba
	public List<CleanerRooms> getCleaners() {
		return cleaners;
    }
	
	public boolean removeCleaner(String cleaner) {
		for (CleanerRooms cleanerRooms : cleaners) {
			if (cleanerRooms.getCleanerUsername().equals(cleaner)) {
				cleaners.remove(cleanerRooms);
				return true;
			}
		}
		return false;
	}
}
