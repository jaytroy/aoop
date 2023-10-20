package nl.rug.aoop;

import nl.rug.aoop.basic.Stock;
import nl.rug.aoop.basic.StockExchange;
import nl.rug.aoop.basic.Trader;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.serverside.TSMessageQueue;
import nl.rug.aoop.network.OrderHandler;
import nl.rug.aoop.network.StockApplication;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.model.StockExchangeDataModel;

import java.util.List;
import java.util.Map;

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