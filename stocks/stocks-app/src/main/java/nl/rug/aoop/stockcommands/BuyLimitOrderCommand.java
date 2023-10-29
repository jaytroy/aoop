package nl.rug.aoop.stockcommands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.model.Stock;
import nl.rug.aoop.model.Trader;

import java.util.Map;

@Slf4j
public class BuyLimitOrderCommand implements Command {
    private Trader trader;
    private Stock stock;
    private double limitPrice;
    private int quantity;

    public BuyLimitOrderCommand(Trader trader, Stock stock, double limitPrice, int quantity) {
        this.trader = trader;
        this.stock = stock;
        this.limitPrice = limitPrice;
        this.quantity = quantity;
    }

    @Override
    public void execute(Map<String, Object> params) {
        if (stock != null && stock.getPrice() <= limitPrice) {
            double totalCost = stock.getPrice() * quantity;

            if (trader.getFunds() >= totalCost) {
                trader.setFunds(trader.getFunds() - totalCost);
                trader.addOwnedStock(stock.getSymbol(), quantity);
                log.info(trader.getName() + " has bought " + quantity + " shares of " + stock.getSymbol() + " at or below the specified limit price of " + (int) limitPrice);
            } else {
                log.info(trader.getName() + " does not have enough funds to buy " + quantity + " shares of " + stock.getSymbol());
            }
        } else {
            log.info("Stock not found or the stock's price is above the specified limit price.");
        }
    }
}
