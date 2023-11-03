package nl.rug.aoop.actions;

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
public class BuyLimitOrderCommand /*implements Command */{
    private Order order;

    /**
     * Constructs a BuyLimitOrderCommand with the specified trader, stock, limit price, and quantity.
     *
     * @param order The order.
     */
    public BuyLimitOrderCommand(Order order) {
        this.order = order;
    }

    /**
     * Executes the buy limit order command, allowing the trader to buy the specified quantity of shares of a stock
     * if the stock's price is at or below the specified limit price.
     *
     * @param params Additional parameters (not used).
     */
    /*
    @Override
    public void execute(Map<String, Object> params) {
        if (stock != null && stock.getPrice() <= limitPrice) {
            double totalCost = stock.getPrice() * quantity;

            if (trader.getFunds() >= totalCost) {
                trader.setFunds(trader.getFunds() - totalCost);
                trader.addOwnedStock(stock.getSymbol(), quantity);
                log.info(trader.getName() + " has bought " + quantity + " shares of " + stock.getSymbol() +
                        " at or below the specified limit price of " + (int) limitPrice);
            } /*else {//This should be in trader
                log.info(trader.getName() + " does not have enough funds to buy " + quantity + " shares of " +
                        stock.getSymbol());
            }
        } else {
            log.info("Stock not found or the stock's price is above the specified limit price.");
        }
    }
    */
}
