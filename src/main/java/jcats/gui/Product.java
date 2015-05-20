package jcats.gui;

import javafx.beans.property.SimpleStringProperty;

public class Product {
    private final SimpleStringProperty ticker;
    private final SimpleStringProperty timeString;
    private final SimpleStringProperty bidString;
    private final SimpleStringProperty askString;
    private final SimpleStringProperty bidSizeString;
    private final SimpleStringProperty askSizeString;
    
    public Product(String ticker) {
        this.ticker = new SimpleStringProperty(ticker);
        this.timeString = new SimpleStringProperty();
        this.bidString = new SimpleStringProperty();
        this.askString = new SimpleStringProperty();
        this.bidSizeString = new SimpleStringProperty();
        this.askSizeString = new SimpleStringProperty();
    }

    public String getTicker() {
        return ticker.get();
    }

    public void setTicker(String t) {
    	ticker.set(t);
    }

    public SimpleStringProperty tickerProperty() {
        return ticker;
    }

    public String getBidString() {
        return bidString.get();
    }

    public void setBidString(String b) {
    	bidString.set(b);
    }
    
    public SimpleStringProperty bidStringProperty() {
    	return bidString;
    }

    public String getAskString() {
        return askString.get();
    }

    public void setAskString(String a) {
    	askString.set(a);
    }

    public SimpleStringProperty askStringProperty() {
    	return askString;
    }

    public String getBidSizeString() {
        return bidSizeString.get();
    }

    public void setBidSizeString(String q) {
    	bidSizeString.set(q);
    }

    public SimpleStringProperty bidSizeStringProperty() {
        return bidSizeString;
    }
    
    public String getAskSizeString() {
        return askSizeString.get();
    }

    public void setAskSizeString(String q) {
    	askSizeString.set(q);
    }

    public SimpleStringProperty askSizeStringProperty() {
        return askSizeString;
    }

    public String getTimeString() {
        return timeString.get();
    }

    public void setTimeString(String t) {
    	timeString.set(t);
    }

    public SimpleStringProperty timeStringProperty() {
        return timeString;
    }

}
