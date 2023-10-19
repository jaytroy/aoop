package nl.rug.aoop;

import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.serverside.TSMessageQueue;
import nl.rug.aoop.network.OrderHandler;
import nl.rug.aoop.network.StockApplication;
import nl.rug.aoop.networking.MessageHandler;

public class StockAppMain {
    public static void main(String[] args) {
        MessageQueue queue = new TSMessageQueue(); //Does this have to be started up on a thread or just initailized
        int port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        MessageHandler messageHandler = new OrderHandler();
        StockApplication app = new StockApplication(queue, messageHandler, port);
        app.startMessageQueue();

        //Logic that calls the UI
    }
}