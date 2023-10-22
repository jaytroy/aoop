package nl.rug.aoop;

import nl.rug.aoop.ui.Stock;
import nl.rug.aoop.ui.StockExchange;
import nl.rug.aoop.ui.Trader;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.serverside.TSMessageQueue;
import nl.rug.aoop.network.OrderHandler;
import nl.rug.aoop.network.StockApplication;
import nl.rug.aoop.networking.MessageHandler;

import java.util.List;

public class StockAppMain {
    public static void main(String[] args) {
        MessageQueue queue = new TSMessageQueue();
        int port;
        int BACKUP_PORT = 8080;
        if(System.getenv("MESSAGE_QUEUE_PORT") != null) {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } else {
            port = BACKUP_PORT;
        }
        MessageHandler messageHandler = new OrderHandler();
        StockApplication app = new StockApplication(queue, messageHandler, port);
        app.startMessageQueue();
        app.trackConnectedClients();

        List<Stock> stocks = app.initializeStocks();
        List<Trader> traders = app.initializeTraders();

        StockExchange stockExchange = new StockExchange(stocks, traders);

        SimpleViewFactory viewFactory = new SimpleViewFactory();
        viewFactory.createView(stockExchange);
    }
}