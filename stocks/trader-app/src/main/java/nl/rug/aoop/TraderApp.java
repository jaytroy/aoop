package nl.rug.aoop;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.client.Client;

import java.io.IOException;
import java.net.InetSocketAddress;

import static nl.rug.aoop.actions.Order.Type.BUY;
import static nl.rug.aoop.actions.Order.Type.SELL;

public class TraderApp {
    public void initialize() {
        Trader trader1 = new Trader("bot1",getAdd());
        Trader trader2 = new Trader("bot2",getAdd());
        Trader trader3 = new Trader("bot3",getAdd());


        //trader1.placeOrder(BUY, "AMD", 100, 1000);
        //trader2.placeOrder(SELL, "AMD", 10, 900);
    }

    public InetSocketAddress getAdd() {
        int port;
        int BACKUP_PORT = 8080;
        InetSocketAddress address;
        if(System.getenv("MESSAGE_QUEUE_PORT") != null) {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } else {
            port = BACKUP_PORT;
            System.out.println("Using backup port at TraderAppMain");
        }
        return new InetSocketAddress("localhost", port);
    }
}
