package nl.rug.aoop.basic;

import nl.rug.aoop.model.StockDataModel;

public class Stock implements StockDataModel { //Should this be here? Stocks-app should have no knowledge of stock-market-ui
    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double marketCap;
    private double price;

    public Stock(String symbol, String name, long sharesOutstanding, double price) {
        this.symbol = symbol;
        this.name = name;
        this.sharesOutstanding = sharesOutstanding;
        this.marketCap = sharesOutstanding * price;
        this.price = price;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSharesOutstanding() {
        return sharesOutstanding;
    }

    @Override
    public double getMarketCap() {
        return marketCap;
    }

    @Override
    public double getPrice() {
        return price;
    }
}
