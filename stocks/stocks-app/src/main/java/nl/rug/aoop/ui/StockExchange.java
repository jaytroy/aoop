package nl.rug.aoop.ui;

import nl.rug.aoop.model.*;
import java.util.List;

public class StockExchange implements StockExchangeDataModel {
    private List<Stock> stocks;
    private List<Trader> traders;

    public StockExchange(List<Stock> stocks, List<Trader> traders) {
        this.stocks = stocks;
        this.traders = traders;
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
        if (index >= 0 && index < traders.size()) {
            return (TraderDataModel) traders.get(index);
        } else {
            return null;
        }
    }


    @Override
    public int getNumberOfTraders() {
        return traders.size();
    }
}
