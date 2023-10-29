package nl.rug.aoop.stockcommands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.model.Stock;
import nl.rug.aoop.model.Trader;

import java.util.Map;

@Slf4j
public class SellLimitOrderCommand implements Command {
    private Trader trader;
    private Stock stock;
    private double limitPrice;
    private int quantity;

    public SellLimitOrderCommand(Trader trader, Stock stock, double limitPrice, int quantity) {
        this.trader = trader;
        this.stock = stock;
        this.limitPrice = limitPrice;
        this.quantity = quantity;
    }

    @Override
    public void execute(Map<String, Object> params) {
        if (stock != null && stock.getPrice() >= limitPrice && trader.hasEnoughStock(stock.getSymbol(), quantity)) {
            double totalRevenue = stock.getPrice() * quantity;
            trader.setFunds(trader.getFunds() + totalRevenue);
            trader.removeOwnedStock(stock.getSymbol(), quantity);

            log.info(trader.getName() + " has sold " + quantity + " shares of " + stock.getSymbol() + " at or above the specified limit price of " + (int) limitPrice);
        } else {
            log.info("Failed, " + trader.getName() + " does not own enough shares to sell.");
        }
    }
}
