package nl.rug.aoop.uimodel;

public class StockUI implements StockDataModel {
    private String symbol;
    private String name;
    private long sharesOutstanding;
    private double marketCap;
    private double price;
    private double initialPrice;

    public StockUI() {
        this.price = this.initialPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public long getSharesOutstanding() {
        return sharesOutstanding;
    }

    public double getMarketCap() {
        return price * sharesOutstanding;
    }
    public double getPrice() {
        return price;
    }
}
