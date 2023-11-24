package nl.rug.aoop;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.actions.Order;

/**
 * The TraderFacade class serves as a facade for the stock exchange system.
 * It provides a simplified interface for traders to interact with the system.
 *
 */
@Slf4j
public class TraderFacade {
    /**
     * The Trader associated with this facade.
     */
    @Getter
    @Setter
    private Trader trader;

    /**
     * The strategy employed by the trader for making trading decisions.
     */
    private TraderStrategy strategy;

    /**
     * Constructs a new TraderFacade with the specified Trader.
     *
     * @param trader The Trader to be associated with this facade.
     */
    public TraderFacade(Trader trader) {
        this.trader = trader;
        strategy = new TraderStrategy(this);
    }

    /**
     * Places an order with the stock exchange through the associated Trader.
     *
     * @param type     The type of the order (BUY or SELL).
     * @param symbol   The symbol of the financial instrument.
     * @param quantity The quantity of the financial instrument to be traded.
     * @param price    The price at which the order should be executed.
     */
    public void placeOrder(Order.Type type, String symbol, long quantity, double price) {
        trader.placeOrder(type, symbol, quantity, price);
    }

    /**
     * Executes the trading strategy associated with this TraderFacade.
     * The specific strategy is defined by the TraderStrategy instance.
     */
    public void executeStrategy() {
        strategy.executeStrategy();
    }
}
