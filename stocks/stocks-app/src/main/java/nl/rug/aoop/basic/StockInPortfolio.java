package nl.rug.aoop.basic;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class StockInPortfolio {
    private String stockSymbol;
    private int quantity;

    @JsonCreator
    public StockInPortfolio(
            @JsonProperty("stockSymbol") String stockSymbol,
            @JsonProperty("quantity") int quantity) {
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
