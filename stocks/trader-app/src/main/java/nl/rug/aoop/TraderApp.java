package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static nl.rug.aoop.actions.Order.Type.BUY;
import static nl.rug.aoop.actions.Order.Type.SELL;

/**
 * The TraderApp class represents the entry point for initializing traders in the stock exchange system.
 */
@Slf4j
public class TraderApp {
    ExecutorService threadpool;

    /**
     * Initializes trader instances and connects them to the stock exchange.
     */
    public void initialize() {
        Trader trader1 = new Trader("bot1", getSocketAddress());
        Trader trader2 = new Trader("bot2", getSocketAddress());
        Trader trader3 = new Trader("bot3", getSocketAddress());
        Trader trader4 = new Trader("bot4", getSocketAddress());
        Trader trader5 = new Trader("bot5", getSocketAddress());
        Trader trader6 = new Trader("bot6", getSocketAddress());
        Trader trader7 = new Trader("bot7", getSocketAddress());
        Trader trader8 = new Trader("bot8", getSocketAddress());

        threadpool = Executors.newFixedThreadPool(8);

        threadpool.submit(trader1);
        threadpool.submit(trader2);
        threadpool.submit(trader3);
        threadpool.submit(trader4);
        threadpool.submit(trader5);
        threadpool.submit(trader6);
        threadpool.submit(trader7);
        threadpool.submit(trader8);


        //trader1.placeOrder(BUY,"AMD",200,1000);
        //trader2.placeOrder(SELL, "AMD", 200, 1000);
    }

    /**
     * Get the network address for connecting to the message queue. It uses the environment variable
     * "MESSAGE_QUEUE_PORT" if available, otherwise, it uses a backup port.
     *
     * @return The network address (InetSocketAddress) for connecting to the message queue.
     */
    public InetSocketAddress getSocketAddress() {
        int port;
        int BACKUP_PORT = 8080;
        InetSocketAddress address;
        if (System.getenv("MESSAGE_QUEUE_PORT") != null) {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } else {
            port = BACKUP_PORT;
            log.info("Using backup port at TraderAppMain");
        }
        return new InetSocketAddress("localhost", port);
    }
}
