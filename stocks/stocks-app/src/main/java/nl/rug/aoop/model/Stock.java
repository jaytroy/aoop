package nl.rug.aoop.model;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.uimodel.StockDataModel;

import java.util.List;

/**
 * The Stock class represents a single stock in the trading system.
 */
public class Stock implements StockDataModel {
    @Getter
    private String symbol;
    @Getter
    private String name;
    @Getter
    private long sharesOutstanding;
    private double marketCap;
    @Getter
    @Setter
    private double price;
    private double initialPrice;

    /**
     * Default constructor for a Stock. Initializes the price with the initial price.
     */
    public Stock() {
        this.price = this.initialPrice;
    }

    @Override
    public String getId() {
        return null;
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
     * Get the total market capitalization of the company.
     *
     * @return The market capitalization of the company.
     */
    public double getMarketCap() {
        return price * sharesOutstanding;
    }

}
