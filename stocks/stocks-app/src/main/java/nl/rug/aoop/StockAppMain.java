package nl.rug.aoop;

import nl.rug.aoop.basic.StockExchange;
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

public class StockAppMain {
    public static void main(String[] args) {
<<<<<<< HEAD
        MessageQueue queue = new TSMessageQueue();
        //int port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        int port = 8080;
=======
         MessageQueue queue = new TSMessageQueue(); //Does this have to be started up on a thread or just initailized

        int port;
        int BACKUP_PORT = 8080;
        if(System.getenv("MESSAGE_QUEUE_PORT") != null) {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } else {
            port = BACKUP_PORT;
        }

>>>>>>> refs/remotes/origin/stonks
        MessageHandler messageHandler = new OrderHandler();
        StockApplication app = new StockApplication(queue, messageHandler, port);
        app.startMessageQueue();
        app.trackConnectedClients();

        List<StockDataModel> stocks = app.initializeStocks();
        List<TraderDataModel> traders = app.initializeTraders();

        StockExchangeDataModel stockExchange = new StockExchange(stocks, traders);

        SimpleViewFactory viewFactory = new SimpleViewFactory();
        //viewFactory.createView(stockExchange);
    }
}