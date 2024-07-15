package manage;

import model.*;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private List<User> users;

    private String userFile;

    public UserManager(String userFile) {
        this.userFile = userFile;
        this.users = new ArrayList<User>();
    }

    public boolean loadData() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(this.userFile));
            String line = "";
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length > 8) {
                    switch (data[11]) {
                        case "ADMINISTRATOR":
                            this.users.add( new Administrator(data[0], data[1], Boolean.parseBoolean(data[2]), LocalDate.parse(data[3]), data[4], data[5], data[6], data[7], Double.parseDouble(data[8]), Integer.parseInt(data[9]), Integer.parseInt(data[10]), EmployeeType.valueOf(data[11])));
                            break;
                        case "RECEPTIONIST":
                            this.users.add(new Receptionist(data[0], data[1], Boolean.parseBoolean(data[2]), LocalDate.parse(data[3]), data[4], data[5], data[6], data[7], Double.parseDouble(data[8]), Integer.parseInt(data[9]), Integer.parseInt(data[10]), EmployeeType.valueOf(data[11])));
                            break;
                        case "CLEANER":
                            this.users.add(new Cleaner(data[0], data[1], Boolean.parseBoolean(data[2]), LocalDate.parse(data[3]), data[4], data[5], data[6], data[7], Double.parseDouble(data[8]), Integer.parseInt(data[9]), Integer.parseInt(data[10]), EmployeeType.valueOf(data[11])));
                            break;
                    }
                }else{
                    this.users.add(new Guest(data[0], data[1], Boolean.parseBoolean(data[2]), LocalDate.parse(data[3]), data[4], data[5], data[6], data[7]));
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean saveData() {
        try{
            StringBuilder file = new StringBuilder();
            FileWriter writer = new FileWriter(this.userFile);
            for (User user : this.users) {
                file.append(user.toString() + "\n");
            }
            writer.write(file.toString());
            writer.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean addUser(String name, String lastName, boolean gender, LocalDate birthDate, String phone, String adress,
            String username, String password) {
    	Guest user = new Guest(name, lastName, gender, birthDate, phone, adress, username, hashPassword(password));
        return this.users.add(user);
    }
    
	public boolean addUser(String name, String lastName, boolean gender, LocalDate birthDate, String phone, String adress,
            String username, String password, double salary, int qualification, int yearsWorking, EmployeeType type) {
        switch (type) {
            case ADMINISTRATOR:
                Administrator admin = new Administrator(name, lastName, gender, birthDate, phone, adress, username, hashPassword(password), salary, qualification, yearsWorking, type);
                return this.users.add(admin);
            case RECEPTIONIST:
        	    Receptionist receptionist = new Receptionist(name, lastName, gender, birthDate, phone, adress, username, hashPassword(password), salary, qualification, yearsWorking, type);
        	    return this.users.add(receptionist);
            case CLEANER:
            	Cleaner cleaner = new Cleaner(name, lastName, gender, birthDate, phone, adress, username, hashPassword(password), salary, qualification, yearsWorking, type);
            	CleanersManager cleanerManager = ManagerFactory.getInstance().getCleanersManager();
            	cleanerManager.addCleaner(cleaner.getUsername());
            	return this.users.add(cleaner);
			default:
				return false;
			}
		}

    public boolean removeUser(String username) {
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
            	if(user instanceof Cleaner) {
            		CleanersManager cleanerManager = ManagerFactory.getInstance().getCleanersManager();
            		cleanerManager.removeCleaner(user.getUsername());
            	}
                this.users.remove(user);
                return true;
            }
        }
        return false;
    }
    
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public User getUser(String username) {
        for (User user : this.users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public List<User> getUsers() {
        return this.users;
    }
    
	public boolean checkIfUserExists(String username) {
		for (User user : this.users) {
			if (user.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}
	
	public List<Employee> getEmployees() {
		List<Employee> employees = new ArrayList<>();
		for (User user : this.users) {
			if (user instanceof Cleaner || user instanceof Receptionist) {
				employees.add((Employee) user);
			}
		}
		return employees;
	}
	
	public List<Cleaner> getCleaners() {
		List<Cleaner> cleaners = new ArrayList<>();
		for (User user : this.users) {
			if (user instanceof Cleaner) {
				cleaners.add((Cleaner) user);
			}
		}
		return cleaners;
	}
	
	public User login(String username, String password) {
        User user = getUser(username);
        if (user != null && user.getPassword().equals(hashPassword(password))) {
    	  return user;
        }
        return null;
    }
	
	public List<Guest> getGuests(){
		List<Guest> guests = new ArrayList<>();
        for (User user : this.users) {
            if (user instanceof Guest) {
                guests.add((Guest) user);
            }
        }
        return guests;
    }
	
	public int calculateSpendings(LocalDate start, LocalDate end) {
		int total = 0;
        start = start.withDayOfMonth(1);
        while (start.isBefore(end)) {
        	for (User user : this.users) {
        		if (user instanceof Employee) {
        			Employee employee = (Employee) user;
        			total += employee.getSalary();
        		}
        	}
        	start = start.plusMonths(1);
        }
        return total;
	}
}
