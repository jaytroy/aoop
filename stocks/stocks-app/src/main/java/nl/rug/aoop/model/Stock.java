package nl.rug.aoop.model;

import nl.rug.aoop.uimodel.StockDataModel;

import java.util.List;

/**
 * The Stock class represents a single stock in the trading system.
 */
public class Stock implements StockDataModel {
    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double marketCap;
    private double price;
    private double initialPrice;

    /**
     * Default constructor for a Stock. Initializes the price with the initial price.
     */
    public Stock() {
        this.price = this.initialPrice;
    }

    /**
     * Get the symbol of the stock.
     *
     * @return The stock's symbol.
     */
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getId() {
        return null;
    }

    /**
     * Get the name of the company associated with the stock.
     *
     * @return The name of the company.
     */
    public String getName() {
        return name;
    }

    @Override
    public double getFunds() {
        return 0;
    }

    @Override
    public List<String> getOwnedStocks() {
        return null;
    }

    /**
     * Get the number of shares available for trading.
     *
     * @return The total number of shares outstanding.
     */
    public long getSharesOutstanding() {
        return sharesOutstanding;
    }

    /**
     * Get the total market capitalization of the company.
     *
     * @return The market capitalization of the company.
     */
    public double getMarketCap() {
        return price * sharesOutstanding;
    }

    /**
     * Get the price of a single share.
     *
     * @return The price of a single share.
     */
    public double getPrice() {
        return price;
    }
}
