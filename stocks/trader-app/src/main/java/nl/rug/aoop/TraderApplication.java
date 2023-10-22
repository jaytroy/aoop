package nl.rug.aoop;

import java.util.ArrayList;
import java.util.List;

public class TraderApplication {
    private List<TraderInfo> traders;
    // Add other necessary fields like message queue, etc. (will do that later)

    public TraderApplication() {
        traders = new ArrayList<>();
        // Other fields will be here
    }

    public void addTrader(TraderInfo trader) {
        traders.add(trader);
    }

    public void listenToStockApplication() {
        // Not implemented yet
    }

    public void generateTradeOrders() {
        // Not implemented yet
    }

    public void sendTradeOrdersToStockApplication() {
        // Not implemented yet
    }
}
