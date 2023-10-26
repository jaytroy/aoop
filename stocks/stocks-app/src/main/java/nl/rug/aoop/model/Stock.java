package nl.rug.aoop.model;

public class Stock {
    private String symbol;
    private double currentPrice;

    public Stock(String symbol, double currentPrice) {
        this.symbol = symbol;
        this.currentPrice = currentPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return currentPrice;
    }

    public void setPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
}
