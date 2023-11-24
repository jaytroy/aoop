package nl.rug.aoop.model.components;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.uimodel.StockDataModel;

import java.util.List;

/**
 * The Stock class represents a single stock in the trading system.
 */
public class Stock implements StockDataModel {
    @Getter
    @Setter
    private String symbol;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private long sharesOutstanding;
    @Setter
    private double marketCap;
    @Getter
    @Setter
    private double price;

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
