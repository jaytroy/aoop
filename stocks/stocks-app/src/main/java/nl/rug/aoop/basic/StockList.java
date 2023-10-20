package nl.rug.aoop.basic;

import nl.rug.aoop.model.StockDataModel;

import java.util.List;
import java.util.Map;

public class StockList {
    private List<Stock> stocks;

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }
}