package nl.rug.aoop.stockcommands;

import nl.rug.aoop.ui.Trader;
import nl.rug.aoop.model.StockDataModel;

public class SellLimitOrderCommand {
    private Trader trader;
    private StockDataModel stock;
    private double limitPrice;
    private int quantity;

    public SellLimitOrderCommand(Trader trader, StockDataModel stock, double limitPrice, int quantity) {
        this.trader = trader;
        this.stock = stock;
        this.limitPrice = limitPrice;
        this.quantity = quantity;
    }

    public void execute() {
        if (stock != null && stock.getPrice() >= limitPrice && trader.hasEnoughStock(stock.getSymbol(), quantity)) {
            double totalRevenue = stock.getPrice() * quantity;
            trader.setFunds(trader.getFunds() + totalRevenue);
            trader.removeOwnedStock(stock.getSymbol(), quantity);

            System.out.println(trader.getName() + " has sold " + quantity + " shares of " + stock.getSymbol() + " at or above the specified limit price of " + limitPrice);
        } else {
            System.out.println("Stock not found, the stock's price is below the specified limit price, or the trader does not own enough shares to sell.");
        }
    }
}
