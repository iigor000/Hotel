package manage;

import model.PriceList;
import model.Reservation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PriceListManager {
    private String priceListFile;
    private List<PriceList> priceListList;

    // Pravimo klasu koja ce vrsiti funkcije sa listama cena
    public PriceListManager(String priceListFile) {
        this.priceListFile = priceListFile;
        this.priceListList = new ArrayList<PriceList>();
    }

    // Ucitavamo cenovnike iz fajla
    public boolean loadData() {
        try {
            // Otvaramo fajl i citamo podatke iz njega
            BufferedReader reader = new BufferedReader(new FileReader(this.priceListFile));
            String line = "";
            int counter = 0;
            List<String> services = new ArrayList<String>();
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // U prvoj liniji se nalaze usluge, a u ostalim cenovnici
                if (counter == 0) {
                    for (int i = 0; i < data.length; i++) {
                        services.add(data[i]);
                    }

                }
                // Cenovnike pravimo u hashmape po uslugama
                else {
                    HashMap<String, Double> prices = new HashMap<>();
                    for (int i = 2; i < data.length; i++) {
                        prices.put(services.get(i - 2), Double.parseDouble(data[i]));
                    }
                    this.priceListList.add(new PriceList(LocalDate.parse(data[0]), LocalDate.parse(data[1]), prices));
                }
                counter++;
            }
            reader.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    // Cuvamo podatke u fajl
    public boolean saveData() {
        try {
            StringBuilder file = new StringBuilder();
            int counter = 0;
            String services = "";
            for (PriceList priceList : this.priceListList) {
                if(priceList.getPrices().size() > counter){
                    StringBuilder serviceList = new StringBuilder();
                    for (String key : priceList.getPrices().keySet()) {
                        serviceList.append(key + ",");
                    }
                    serviceList.deleteCharAt(serviceList.length() - 1);
                    services = serviceList.toString();
                }
                file.append(priceList.toString() + "\n");
            }
            file.insert(0, services + "\n");
            FileWriter writer = new FileWriter(this.priceListFile);
            writer.write(file.toString());
            writer.close();
        } catch (
                IOException e) {
            return false;
        }
        return true;
    }

    // Vraca cenovnik za odredjeni datum
    public PriceList getPriceList(LocalDate date) {
        for (PriceList priceList : this.priceListList) {
            if (priceList.getStartDate().isBefore(date) && priceList.getExpirationDate().isAfter(date)) {
                return priceList;
            }
        }
        return null;
    }

    // Dodaje uslugu u sve cenovnike
    public boolean addService(String name, double price){
        for (PriceList priceList : this.priceListList) {
            priceList.getPrices().put(name, price);
        }
        return true;
    }

    // Dodaje cenovnik
    public boolean addPriceList(LocalDate startDate, LocalDate expirationDate, HashMap<String, Double> prices){
		return this.priceListList.add(new PriceList(startDate, expirationDate, prices));
	}

    // Azurira cenu u cenovniku
    public boolean updatePrice(String name, Double price, PriceList priceList){
    	if (!priceList.getPrices().containsKey(name)) {
    		return false;
    	}
        for (PriceList priceList1 : this.priceListList) {
            if (priceList1.equals(priceList)) {
                priceList1.getPrices().replace(name, price);
            }
        }
        return true;
    }
    
    // Vraca sve usluge
	public List<String> getAllServices() {
		List<String> services = new ArrayList<String>();
		PriceList priceList = this.priceListList.get(0);
		for (String key : priceList.getPrices().keySet()) {
			if (!key.equals("SINGLE") && !key.equals("DOUBLE1_1") && !key.equals("DOUBLE2") && !key.equals("TRIPLE1_1_1") && !key.equals("TRIPLE1_2")) {
				services.add(key);
			}
		}
		return services;
	}
	
	// Vraca sve cenovnike
	public List<PriceList> getPriceLists() {
		return this.priceListList;
	}
	
	// Vraca sve usluge
	public List<String> getServices(){
		List<String> services = new ArrayList<String>();
        PriceList priceList = this.priceListList.get(0);
        for (String key : priceList.getPrices().keySet()) {
            if (!key.equals("SINGLE") && !key.equals("DOUBLE1_1") && !key.equals("DOUBLE2") && !key.equals("TRIPLE1_1_1") && !key.equals("TRIPLE1_2")) {
                services.add(key);
            }
        }
        return services;
    }
	
	// Vraca sve kljuceve (usluge i sobe)
	public List<String> getKeys() {
		List<String> headers = new ArrayList<String>();
		PriceList priceList = this.priceListList.get(0);
		for (String key : priceList.getPrices().keySet()) {
			headers.add(key);
		}
		return headers;
	}
	
	// Vraca sledeci datum za pocetak cenovnika
	public LocalDate getNextStartDate() {
		LocalDate date = LocalDate.now();
        for (PriceList priceList : this.priceListList) {
            if (priceList.getExpirationDate().isAfter(date)) {
                date = priceList.getExpirationDate();
            }
        }
        return date.plusDays(1);
	}
	
	// Vraca sledeci datum za kraj cenovnika
	public void removePriceList(PriceList priceList) {
		this.priceListList.remove(priceList);
	}
	
	// Brise uslugu iz svih cenovnika i brise je iz svih buducih rezervacija
	public boolean deleteService(String serviceName) {
		if (!priceListList.get(0).getPrices().containsKey(serviceName)) {
			return false;
		}
		for (PriceList priceList : this.priceListList) {
			priceList.getPrices().remove(serviceName);
		}
		
		ReservationManager reservationManager = ManagerFactory.getInstance().getReservationManager();
		List<Reservation> reservations = reservationManager.getCurrentReservations();
		
		for (Reservation reservation : reservations) {
			if (reservation.getServices().contains(serviceName)) {
				reservation.getServices().remove(serviceName);
			}
		}
		return true;
		
	}
}
