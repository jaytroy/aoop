package nl.rug.aoop;

import nl.rug.aoop.ui.Stock;
import nl.rug.aoop.ui.StockExchange;
import nl.rug.aoop.ui.Trader;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.network.OrderHandler;
import nl.rug.aoop.network.StockApp;
import nl.rug.aoop.networking.MessageHandler;

import java.util.List;

public class StockAppMain {
    public static void main(String[] args) {
        int port;
        int BACKUP_PORT = 8080;
        if(System.getenv("MESSAGE_QUEUE_PORT") != null) {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } else {
            port = BACKUP_PORT;
            System.out.println("Using backup port at StockAppMain");
        }

        MessageHandler messageHandler = new OrderHandler();
        StockApp stockApp = new StockApp(messageHandler, port);

        //Should this be here?
        List<Stock> stocks = stockApp.initializeStocks();
        List<Trader> traders = stockApp.initializeTraders();

        StockExchange stockExchange = new StockExchange(stocks, traders);

        //Start up the view
        SimpleViewFactory viewFactory = new SimpleViewFactory();
        viewFactory.createView(stockExchange);


        //Start the server
        stockApp.trackConnectedClients();
        stockApp.runStockApp();
    }
}