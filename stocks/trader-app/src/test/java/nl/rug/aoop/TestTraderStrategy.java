package nl.rug.aoop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.rug.aoop.marketcomponents.Stock;
import nl.rug.aoop.marketcomponents.Trader;
import nl.rug.aoop.strategy.StrategyHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class TestTraderStrategy {

    private TraderFacade traderFacade;
    private Trader trader;
    private StrategyHandler traderStrategy;

    @BeforeEach
    public void setUp() {
        traderFacade = mock(TraderFacade.class);
        trader = mock(Trader.class);

        // Ensure that the TraderFacade mock returns the mock Trader object
        when(traderFacade.getTrader()).thenReturn(trader);

        traderStrategy = new StrategyHandler(traderFacade);
    }

    @Test
    public void testExecuteStrategy() {
        when(traderFacade.getTrader().getAvailableStocks()).thenReturn(createStockList());
        when(traderFacade.getTrader().getOwnedStocks()).thenReturn(createOwnedStocksMap());
        when(traderFacade.getTrader().getAvailableFunds()).thenReturn(1000.0);

        traderStrategy.executeStrategy();
    }


    private List<Stock> createStockList() {
        List<Stock> stocks = new ArrayList<>();
        stocks.add(new Stock("AAPL", 150.0));
        stocks.add(new Stock("GOOGL", 1200.0));
        return stocks;
    }

    private Map<String, Integer> createOwnedStocksMap() {
        Map<String, Integer> ownedStocks = new HashMap<>();
        ownedStocks.put("AAPL", 5);
        ownedStocks.put("GOOGL", 10);
        return ownedStocks;
    }
}

