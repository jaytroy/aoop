package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Timer;
import java.util.TimerTask;

import static nl.rug.aoop.actions.Order.Type.BUY;
import static nl.rug.aoop.actions.Order.Type.SELL;

/**
 * The TraderApp class represents the entry point for initializing traders in the stock exchange system.
 */
@Slf4j
public class TraderApp {

    /**
     * Initializes trader instances and connects them to the stock exchange.
     */
    public void initialize() {
        Trader trader1 = new Trader("bot1", getSocketAddress());
        Trader trader2 = new Trader("bot2", getSocketAddress());
        Trader trader3 = new Trader("bot3", getSocketAddress());
        trader1.run();
        trader2.run();
        trader2.run();

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
