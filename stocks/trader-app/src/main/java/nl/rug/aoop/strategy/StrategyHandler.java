package nl.rug.aoop.strategy;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.TraderFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The StrategyHandler class represents the strategy for generating random orders for a trader.
 */
@Slf4j
public class StrategyHandler {
    private List<TradeStrategy> strategies;

    /**
     * Implements a trader strategy on a traderFacade which will then implement it on a trader.
     *
     * @param traderFacade the traderFacade that will have a strategy implemented on them.
     */
    public StrategyHandler(TraderFacade traderFacade) {
        this.strategies = new ArrayList<>();

        //Initialize as many strategies as you need
        strategies.add(new RandomMarketStrategy(traderFacade));
        strategies.add(new RandomLimitStrategy(traderFacade));
    }

    /**
     * Execute a trader strategy for generating random orders.
     */
    public void executeStrategy() {
        //Random execution of strategy
        Random random = new Random();
        int stratChoice = random.nextInt(strategies.size());

        strategies.get(stratChoice).execute();
    }

    public void addStrategy(TradeStrategy strategy) {
        strategies.add(strategy);
    }
}
