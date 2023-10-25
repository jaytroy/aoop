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

        public boolean buyStock(Trader trader, int quantity) {
            double totalCost = currentPrice * quantity;
            if (trader.getAvailableFunds() >= totalCost) {
                trader.subtractFunds(totalCost);
                trader.addOwnedStock(symbol, quantity);
                return true;
            }
            return false;
        }

        public boolean sellStock(Trader trader, int quantity) {
            if (trader.getOwnedStocks().getOrDefault(symbol, 0) >= quantity) {
                trader.addFunds(currentPrice * quantity);
                trader.subtractOwnedStock(symbol, quantity);
                return true;
            }
            return false;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Stock stock = (Stock) o;
            return Objects.equals(symbol, stock.symbol);
        }

        @Override
        public int hashCode() {
            return Objects.hash(symbol);
        }
    }

}
