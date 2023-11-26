package nl.rug.aoop;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.actions.Order;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.strategy.TraderStrategy;

import java.util.List;
import java.util.Map;


/**
 * The TraderFacade class serves as a facade for the TraderApp system.
 */
@Slf4j
public class TraderFacade {
    @Getter
    @Setter
    private Trader trader;

    private TraderStrategy strategy;
    @Getter
    @Setter
    private MessageHandler handler;

    /**
     * Constructs a new TraderFacade with the specified Trader.
     *
     * @param trader The Trader to be associated with this facade.
     */
    public TraderFacade(Trader trader) {
        this.trader = trader;
        strategy = new TraderStrategy(this);
        this.handler = new TraderHandler(this);
    }

    /**
     * Places an order with the stock exchange through the associated Trader.
     *
     * @param action   The action of the order (BUY or SELL).
     * @param type     The order type.
     * @param symbol   The symbol of the stock.
     * @param quantity The quantity of the stock to be traded.
     * @param price    The price at which the order should be executed.
     */
    public void placeOrder(Order.Action action, Order.Type type, String symbol, long quantity, double price) {
        trader.placeOrder(action, type, symbol, quantity, price);
    }

    /**
     * Update trader information.
     *
     * @param funds       The updated funds.
     * @param name        The updated name.
     * @param ownedStocks The updated owned stocks.
     */
    public void updateInfo(double funds, String name, Map<String, Integer> ownedStocks) {
        trader.updateInfo(funds, name, ownedStocks);
    }

    /**
     * Update stock information.
     *
     * @param stocks The updated list of stocks.
     */
    public void updateStocks(List<Stock> stocks) {
        trader.updateStocks(stocks);
    }

    /**
     * Executes the trading strategy associated with this TraderFacade.
     */
    public void executeStrategy() {
        strategy.executeStrategy();
    }
}
