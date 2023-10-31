package nl.rug.aoop.model;

import java.util.List;

/**
 * The StockList class represents a list of stocks in the trading system.
 */
public class StockList {
    private List<Stock> stocks;

    /**
     * Get the list of stocks.
     *
     * @return The list of stocks.
     */
    public List<Stock> getStocks() {
        return stocks;
    }

    /**
     * Set the list of stocks.
     *
     * @param stocks The list of stocks to set.
     */
    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}
