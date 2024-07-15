package model;

import java.time.LocalDate;
import java.util.HashMap;

public class PriceList {
    private HashMap<String, Double> prices;
    private LocalDate startDate;
    private LocalDate expirationDate;

    public PriceList(LocalDate startDate, LocalDate expirationDate, HashMap<String, Double> prices) {
        this.prices = prices;
        this.expirationDate = expirationDate;
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : this.prices.keySet()) {
            sb.append( this.prices.get(key) + ",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return this.startDate.toString() + "," + this.expirationDate.toString() + "," + sb.toString();
    }

    public HashMap<String, Double> getPrices() {
        return prices;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setPrices(HashMap<String, Double> prices) {
    	this.prices = prices;
    }
    
	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}
}
