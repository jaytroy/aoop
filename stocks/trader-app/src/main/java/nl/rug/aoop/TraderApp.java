package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * The TraderApp class represents the entry point for initializing traders in the stock exchange system.
 */
@Slf4j
public class TraderApp {
    private List<Trader> traders = new ArrayList<>();

    /**
     * Initializes trader instances and connects them to the stock exchange.
     */
    public void initialize() {
        int numberOfTraders = 9;
        for (int i = 1; i <= numberOfTraders; i++) {
            String traderId = "bot" + i;
            Trader trader = new Trader(traderId, getSocketAddress());
            traders.add(trader);
            traders.get(i-1).traderStrategy();
        }
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
