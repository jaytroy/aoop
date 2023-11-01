package nl.rug.aoop.model;

import nl.rug.aoop.uimodel.StockDataModel;
import nl.rug.aoop.uimodel.StockExchangeDataModel;
import nl.rug.aoop.uimodel.TraderDataModel;

import java.util.List;

/**
 * The StockExchange class represents the core data model for the stock exchange, providing information about stocks
 * and traders.
 */
public class StockExchange implements StockExchangeDataModel {
    private List<Stock> stocks;
    private List<Trader> traderUIS;

    /**
     * Constructs a StockExchange with the given list of stocks and trader user interfaces (UIs).
     *
     * @param stocks     The list of stocks in the stock exchange.
     * @param traderUIS  The list of trader user interfaces in the stock exchange.
     */
    public StockExchange(List<Stock> stocks, List<Trader> traderUIS) {
        this.stocks = stocks;
        this.traderUIS = traderUIS;
    }

    @Override
    public StockDataModel getStockByIndex(int index) {
        if (index >= 0 && index < stocks.size()) {
            return (StockDataModel) stocks.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getNumberOfStocks() {
        return stocks.size();
    }

    @Override
    public TraderDataModel getTraderByIndex(int index) {
        if (index >= 0 && index < traderUIS.size()) {
            return (TraderDataModel) traderUIS.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getNumberOfTraders() {
        return traderUIS.size();
    }
}