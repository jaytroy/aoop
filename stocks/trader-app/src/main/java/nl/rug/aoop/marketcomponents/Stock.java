package nl.rug.aoop.marketcomponents;

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

    /**
     * Constructor for stock.
     *
     * @param symbol the symbol for the stock
     * @param price the price of the stock.
     */
    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }

}
