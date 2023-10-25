package nl.rug.aoop;

public class Stock {
    package nl.rug.aoop;

import java.util.Objects;

    public class Stock {
        private String symbol;
        private double currentPrice;

        public Stock(String symbol, double currentPrice) {
            this.symbol = symbol;
            this.currentPrice = currentPrice;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public double getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(double currentPrice) {
            this.currentPrice = currentPrice;
        }
    }

}
