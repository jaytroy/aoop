package nl.rug.aoop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTraderHandler {

    private Trader trader;
    private TraderHandler traderHandler;

    @BeforeEach
    public void setUp() {
        trader = mock(Trader.class);
        traderHandler = new TraderHandler(trader);
    }

    @Test
    public void testHandleTraderInfo() {
        String traderMessage = "{\"name\":\"John\",\"funds\":1000,\"ownedStocks\":{\"AAPL\":10,\"GOOGL\":5}}";

        when(trader.getName()).thenReturn("InitialName");
        when(trader.getAvailableFunds()).thenReturn(500.0);
        when(trader.getOwnedStocks()).thenReturn(new HashMap<>());

        traderHandler.handleTraderInfo(traderMessage);

        verify(trader).updateInfo(1000, "John", Map.of("AAPL", 10, "GOOGL", 5));
    }

}
