package nl.rug.aoop;

import java.util.ArrayList;
import java.util.List;

public class TraderApplication {
    private List<Trader> traders;
    // Add other necessary fields like message queue, etc. (will do that later)

    /**
     * Creates a new trader app. This also initializes one trader. More can be added via method later.
     */
    public TraderApplication() {
        traders = new ArrayList<>();
        // Other fields will be here
    }

    public void addTrader(Trader trader) {
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

    public void newTrader() {
        traders.add(new Trader)
    }
}
