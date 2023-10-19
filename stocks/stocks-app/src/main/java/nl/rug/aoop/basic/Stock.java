package nl.rug.aoop.basic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import nl.rug.aoop.model.StockDataModel;

public class Stock implements StockDataModel { //Should this be here? Stocks-app should have no knowledge of stock-market-ui
    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double marketCap;
    private double price;

    @JsonCreator
    public Stock(@JsonProperty("symbol") String symbol,
                 @JsonProperty("name") String name,
                 @JsonProperty("sharesOutstanding") long sharesOutstanding,
                 @JsonProperty("initialPrice") double initialPrice) {
        this.symbol = symbol;
        this.name = name;
        this.sharesOutstanding = sharesOutstanding;
        this.price = initialPrice;
        marketCap = sharesOutstanding * initialPrice;
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
