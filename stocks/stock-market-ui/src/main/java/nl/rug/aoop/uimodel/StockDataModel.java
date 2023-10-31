package nl.rug.aoop.uimodel;

import java.util.List;

/**
 * Data model of a single stock.
 * Note that a stock may have more properties; these are just the ones needed for the view.
 */
public interface StockDataModel {
    /**
     * Retrieves the symbol of a stock. Usually a 3-letter string of all upper-case letters.
     *
     * @return The symbol of the stock.
     */
    String getSymbol();

    /**
     * Retrieves the unique identifier of a stock.
     *
     * @return The unique identifier of the stock.
     */
    String getId();

    /**
     * Retrieves the name of the company associated with the stock.
     *
     * @return Name of the company.
     */
    String getName();

    /**
     * Retrieves the available funds associated with the stock.
     *
     * @return Available funds for the stock.
     */
    double getFunds();

    /**
     * Retrieves a list of the stocks owned by the entity.
     *
     * @return List of owned stocks.
     */
    List<String> getOwnedStocks();

    /**
     * Retrieves the number of shares available for trading.
     *
     * @return Total number of shares outstanding.
     */
    long getSharesOutstanding();

    /**
     * Retrieves the total market capitalization of the company. Calculated as sharesOutstanding * price.
     *
     * @return Market capitalization of the company.
     */
    double getMarketCap();

    /**
     * Retrieves the price of a single share. Represents the latest price at which a share was traded.
     *
     * @return The price of a single share.
     */
    double getPrice();
}
