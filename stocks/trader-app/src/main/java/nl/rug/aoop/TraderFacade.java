package nl.rug.aoop;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.actions.Order;
import nl.rug.aoop.messagequeue.queues.Message;

import java.time.LocalDateTime;

import static nl.rug.aoop.actions.Order.Type.BUY;

/**
 * The StockExchangeFacade class serves as a facade for the stock exchange system.
 * It provides a simplified interface for traders to interact with the system.
 */
@Slf4j
public class TraderFacade {
    @Getter
    private Trader trader;
    private TraderStrategy strategy;

    public TraderFacade(Trader trader) {
        this.trader = trader;
        strategy = new TraderStrategy(this);
    }

    public void placeOrder(Order.Type type, String symbol, long quantity, double price) {
        trader.placeOrder(type, symbol, quantity, price);
    }

    public void executeStrategy() {
        strategy.executeStrategy();
    }

}
