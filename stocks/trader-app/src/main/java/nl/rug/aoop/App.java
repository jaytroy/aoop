package nl.rug.aoop;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        TraderApplication traderApp = new TraderApplication();

        traderApp.addTrader(new TraderInfo("Trader1", "ID1", 1000.0));
        traderApp.addTrader(new TraderInfo("Trader2", "ID2", 1500.0));
        // Add more traders here (later) as needed

        // Start the Trader Application processes (listening to Stock App, generating trade orders, and sending them)
        traderApp.listenToStockApplication();
        traderApp.generateTradeOrders();
        traderApp.sendTradeOrdersToStockApplication();
    }
}
