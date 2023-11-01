package nl.rug.aoop.stockcommands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.model.Stock;
import nl.rug.aoop.model.Trader;

import java.util.Map;

/**
 * This class represents a command to execute a sell limit order, which allows a trader to sell a specified quantity
 * of shares of a stock if the stock's price is at or above a specified limit price.
 */
@Slf4j
public class SellLimitOrderCommand implements Command {
    private Trader trader;
    private Stock stock;
    private double limitPrice;
    private int quantity;

    /**
     * Constructs a SellLimitOrderCommand with the specified trader, stock, limit price, and quantity.
     *
     * @param trader     The trader placing the sell limit order.
     * @param stock      The stock to be sold if the conditions are met.
     * @param limitPrice The minimum price at which the trader is willing to sell the stock.
     * @param quantity   The quantity of shares to be sold if the conditions are met.
     */
    public SellLimitOrderCommand(Trader trader, Stock stock, double limitPrice, int quantity) {
        this.trader = trader;
        this.stock = stock;
        this.limitPrice = limitPrice;
        this.quantity = quantity;
    }

    /**
     * Executes the sell limit order command, allowing the trader to sell the specified quantity of shares of a stock
     * if the stock's price is at or above the specified limit price and if the trader owns enough of the stock.
     *
     * @param params Additional parameters (not used).
     */
    @Override
    public void execute(Map<String, Object> params) {
        if (stock != null && stock.getPrice() >= limitPrice && trader.hasEnoughStock(stock.getSymbol(), quantity)) {
            double totalRevenue = stock.getPrice() * quantity;
            trader.setFunds(trader.getFunds() + totalRevenue);
            trader.removeOwnedStock(stock.getSymbol(), quantity);

            log.info(trader.getName() + " has sold " + quantity + " shares of " + stock.getSymbol() +
                    " at or above the specified limit price of " + (int) limitPrice);
        } else {
            log.info("Failed, " + trader.getName() + " does not own enough shares to sell.");
        }
    }
}
