package nl.rug.aoop.simpleview;

import nl.rug.aoop.model.*;

import java.util.ArrayList;
import java.util.List;

public class StockExchange implements StockExchangeDataModel {
    private List<StockDataModel> stocks = new ArrayList<>();
    private List<TraderDataModel> traders = new ArrayList<>();

    public void addStock(StockDataModel stock) {
        stocks.add(stock);
    }

    public void addTrader(TraderDataModel trader) {
        traders.add(trader);
    }

    @Override
    public StockDataModel getStockByIndex(int index) {
        if (index >= 0 && index < stocks.size()) {
            return stocks.get(index);
        }
        return null;
    }

    @Override
    public int getNumberOfStocks() {
        return stocks.size();
    }

    @Override
    public TraderDataModel getTraderByIndex(int index) {
        if (index >= 0 && index < traders.size()) {
            return traders.get(index);
        }
        return null;
    }

    @Override
    public int getNumberOfTraders() {
        return traders.size();
    }
}
