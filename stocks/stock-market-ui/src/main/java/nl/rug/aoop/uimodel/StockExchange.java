package nl.rug.aoop.uimodel;

import nl.rug.aoop.model.Stock;
import nl.rug.aoop.model.Trader;

import java.util.List;

public class StockExchange implements StockExchangeDataModel {
    private List<Stock> stocks;
    private List<Trader> traderUIS;

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
