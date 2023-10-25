package nl.rug.aoop.ui;

import nl.rug.aoop.model.*;
import java.util.List;

public class StockExchange implements StockExchangeDataModel {
    private List<StockUI> stocks;
    private List<TraderUI> traderUIS;

    public StockExchange(List<StockUI> stocks, List<TraderUI> traderUIS) {
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
