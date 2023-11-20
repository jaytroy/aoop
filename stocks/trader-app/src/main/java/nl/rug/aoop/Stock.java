package nl.rug.aoop;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

/**
 * The Stock class represents a single stock in the trading system.
 */
public class Stock {
    @Setter
    @Getter
    private String symbol;
    @Setter
    @Getter
    private double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

}
