package nl.rug.aoop.stockcommands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.Command;
import nl.rug.aoop.model.Stock;
import nl.rug.aoop.model.Trader;

import java.util.Map;

/**
 * This class represents a command to execute a buy limit order, which allows a trader to buy a specified quantity of
 * shares of a stock if the stock's price is at or below a specified limit price.
 */
@Slf4j
public class BuyLimitOrderCommand implements Command {
    private Trader trader;
    private Stock stock;
    private double limitPrice;
    private int quantity;

    /**
     * Constructs a BuyLimitOrderCommand with the specified trader, stock, limit price, and quantity.
     *
     * @param trader     The trader placing the buy limit order.
     * @param stock      The stock to be bought if the conditions are met.
     * @param limitPrice The maximum price at which the trader is willing to buy the stock.
     * @param quantity   The quantity of shares to be bought if the conditions are met.
     */
    public BuyLimitOrderCommand(Trader trader, Stock stock, double limitPrice, int quantity) {
        this.trader = trader;
        this.stock = stock;
        this.limitPrice = limitPrice;
        this.quantity = quantity;
    }

    /**
     * Executes the buy limit order command, allowing the trader to buy the specified quantity of shares of a stock
     * if the stock's price is at or below the specified limit price.
     *
     * @param params Additional parameters (not used).
     */
    @Override
    public void execute(Map<String, Object> params) {
        if (stock != null && stock.getPrice() <= limitPrice) {
            double totalCost = stock.getPrice() * quantity;

            if (trader.getFunds() >= totalCost) {
                trader.setFunds(trader.getFunds() - totalCost);
                trader.addOwnedStock(stock.getSymbol(), quantity);
                log.info(trader.getName() + " has bought " + quantity + " shares of " + stock.getSymbol() +
                        " at or below the specified limit price of " + (int) limitPrice);
            } else {
                log.info(trader.getName() + " does not have enough funds to buy " + quantity + " shares of " +
                        stock.getSymbol());
            }
        } else {
            log.info("Stock not found or the stock's price is above the specified limit price.");
        }
    }
}
