package nl.rug.aoop.basic;

import nl.rug.aoop.model.*;

import java.util.List;

public class StockExchange implements StockExchangeDataModel {
    private List<StockDataModel> stocks;
    private List<TraderDataModel> traders;

    public StockExchange(List<StockDataModel> stocks, List<TraderDataModel> traders) {
        this.stocks = stocks;
        this.traders = traders;
    }

    @Override
    public StockDataModel getStockByIndex(int index) {
        return stocks.get(index);
    }

    @Override
    public int getNumberOfStocks() {
        return stocks.size();
    }

    @Override
    public TraderDataModel getTraderByIndex(int index) {
        return traders.get(index);
    }

    @Override
    public int getNumberOfTraders() {
        return traders.size();
    }
}
