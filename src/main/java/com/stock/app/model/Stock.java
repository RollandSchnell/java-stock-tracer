package com.stock.app.model;

public class Stock {

    private String symbol;

    private double low;

    private double high;

    private double open;

    private double change;

    private double price;

    public Stock() {}

    public Stock(String symbol, double low, double high, double open, double change, double price) {
        this.symbol = symbol;
        this.low = low;
        this.high = high;
        this.open = open;
        this.change = change;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    @Override
    public String toString() {
        return "symbol "  + this.symbol + " low " + this.low + " high " + this.high + " open " + this.open + " price "
                + this.price + " change " +  this.change;
    }
}
