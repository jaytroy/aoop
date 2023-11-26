package nl.rug.aoop.strategy;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.Stock;
import nl.rug.aoop.TraderFacade;

import java.util.List;
import java.util.Random;

import static nl.rug.aoop.actions.Order.Action.BUY;
import static nl.rug.aoop.actions.Order.Action.SELL;
import static nl.rug.aoop.actions.Order.Type.LIMIT;
import static nl.rug.aoop.actions.Order.Type.MARKET;

/**
 * The TraderStrategy class represents the strategy for generating random orders for a trader.
 */
@Slf4j
public class TraderStrategy {
    private TraderFacade traderFacade;

    /**
     * Implements a trader strategy on a traderFacade which will then implement it on a trader.
     *
     * @param traderFacade the traderFacade that will have a strategy implemented on them.
     */

    public TraderStrategy(TraderFacade traderFacade) {
        this.traderFacade = traderFacade;
    }

    /**
     * Execute a trader strategy for generating random orders.
     */
    public void executeStrategy() {
        Random random = new Random();
        int marketOrLimit = random.nextInt(2);
        if (marketOrLimit == 0) {
            new MarketStrategy(traderFacade).execute();
        } else {
            new LimitStrategy(traderFacade).execute();
        }
        //more strategies here as needed (for the future if we develop this further)
    }
}
