package nl.rug.aoop.stockcommands;

import nl.rug.aoop.Stock;
import nl.rug.aoop.ui.TraderUI;
import nl.rug.aoop.ui.StockUI;
import nl.rug.aoop.model.StockDataModel;

public class BuyLimitOrderCommand {
    private TraderUI trader;
    private StockUI stock;
    private double limitPrice;
    private int quantity;

    public BuyLimitOrderCommand(TraderUI trader, StockUI stock, double limitPrice, int quantity) {
        this.trader = trader;
        this.stock = stock;
        this.limitPrice = limitPrice;
        this.quantity = quantity;
    }

    public void execute() {
        if (stock != null && stock.getPrice() <= limitPrice) {
            double totalCost = stock.getPrice() * quantity;

            if (trader.getFunds() >= totalCost) {
                trader.setFunds(trader.getFunds() - totalCost);
                trader.addOwnedStock(stock.getSymbol(), quantity);

                System.out.println(trader.getName() + " has bought " + quantity + " shares of " + stock.getSymbol() + " at or below the specified limit price of " + limitPrice);
            } else {
                System.out.println(trader.getName() + " does not have enough funds to buy " + quantity + " shares of " + stock.getSymbol());
            }
        } else {
            System.out.println("Stock not found or the stock's price is above the specified limit price.");
        }
    }
}
