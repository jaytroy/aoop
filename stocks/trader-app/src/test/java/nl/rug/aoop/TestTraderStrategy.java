package nl.rug.aoop;

import nl.rug.aoop.actions.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class TestTraderStrategy {

    private TraderFacade traderFacade;
    private Trader trader;
    private TraderStrategy traderStrategy;

    @BeforeEach
    public void setUp() {
        traderFacade = mock(TraderFacade.class);
        trader = mock(Trader.class);

        // Ensure that the TraderFacade mock returns the mock Trader object
        when(traderFacade.getTrader()).thenReturn(trader);

        traderStrategy = new TraderStrategy(traderFacade);
    }

    @Test
    public void testExecuteStrategy() {
        when(trader.getAvailableStocks()).thenReturn(createStockList());
        when(trader.getOwnedStocks()).thenReturn(createOwnedStocksMap());
        when(trader.getAvailableFunds()).thenReturn(1000.0);
        doNothing().when(traderFacade).placeOrder(any(Order.Type.class), anyString(), anyLong(), anyDouble());
        traderStrategy.executeStrategy();
        verify(traderFacade, times(1)).placeOrder(any(Order.Type.class), anyString(), anyLong(), anyDouble());
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
