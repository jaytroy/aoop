package nl.rug.aoop;

import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.serverside.TSMessageQueue;

public class StockAppMain {
    public static void main(String[] args) {
        MessageQueue queue = new TSMessageQueue();
        StockApplication app = new StockApplication(queue);
        app.startMessageQueue();

        //Logic that calls the UI
    }
}