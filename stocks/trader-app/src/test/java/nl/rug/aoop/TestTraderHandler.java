package nl.rug.aoop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestTraderHandler {
    private TraderHandler traderHandler;
    private TraderFacade traderFacade;

    @BeforeEach
    public void setUp() {
        traderFacade = mock(TraderFacade.class);
        traderHandler = new TraderHandler(traderFacade);
    }

    @Test
    public void testHandleTraderInfo() {
        String traderMessage = "{\"name\":\"John\",\"funds\":1000,\"ownedStocks\":{\"AAPL\":10,\"GOOGL\":5}}";

        when(traderFacade.getTrader().getName()).thenReturn("InitialName");
        when(traderFacade.getTrader().getAvailableFunds()).thenReturn(500.0);
        when(traderFacade.getTrader().getOwnedStocks()).thenReturn(new HashMap<>());

        traderHandler.handleTraderInfo(traderMessage);

        verify(traderFacade.getTrader()).updateInfo(1000, "John", Map.of("AAPL", 10, "GOOGL", 5));
    }

}
