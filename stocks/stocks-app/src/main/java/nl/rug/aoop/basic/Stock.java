package nl.rug.aoop.basic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;


public class Stock {
    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double marketCap;
    private double price;
    private double initialPrice;

    public Stock() {

    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public long getSharesOutstanding() {
        return sharesOutstanding;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public double getPrice() {
        return price;
    }
}
