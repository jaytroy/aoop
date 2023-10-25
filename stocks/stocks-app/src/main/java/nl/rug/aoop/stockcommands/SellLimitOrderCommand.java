package nl.rug.aoop.stockcommands;

import nl.rug.aoop.Stock;
import nl.rug.aoop.ui.TraderUI;
import nl.rug.aoop.model.StockDataModel;

public class SellLimitOrderCommand {
    private TraderUI traderUI;
    private Stock stock;
    private double limitPrice;
    private int quantity;

    public SellLimitOrderCommand(TraderUI traderUI, Stock stock, double limitPrice, int quantity) {
        this.traderUI = traderUI;
        this.stock = stock;
        this.limitPrice = limitPrice;
        this.quantity = quantity;
    }

    public void execute() {
        if (stock != null && stock.getPrice() >= limitPrice && traderUI.hasEnoughStock(stock.getSymbol(), quantity)) {
            double totalRevenue = stock.getPrice() * quantity;
            traderUI.setFunds(traderUI.getFunds() + totalRevenue);
            traderUI.removeOwnedStock(stock.getSymbol(), quantity);

            System.out.println(traderUI.getName() + " has sold " + quantity + " shares of " + stock.getSymbol() + " at or above the specified limit price of " + limitPrice);
        } else {
            System.out.println("Stock not found, the stock's price is below the specified limit price, or the trader does not own enough shares to sell.");
        }
    }
}
