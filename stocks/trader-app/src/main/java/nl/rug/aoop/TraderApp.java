package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.marketcomponents.Trader;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The TraderApp class represents the entry point for initializing traders in the stock exchange system.
 */
@Slf4j
public class TraderApp {
    private static ExecutorService threadpool;

    /**
     * The main method that initializes the trader App bots.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        int numberOfTraders = 9;
        threadpool = Executors.newFixedThreadPool(numberOfTraders);

        for (int i = 1; i <= numberOfTraders; i++) {
            Trader trader = new Trader("bot" + i, getSocketAddress());
            threadpool.submit(trader);
        }
    }

    /**
     * Get the network address for connecting to the message queue. It uses the environment variable
     * "MESSAGE_QUEUE_PORT" if available, otherwise, it uses a backup port.
     *
     * @return The network address (InetSocketAddress) for connecting to the message queue.
     */
    public static InetSocketAddress getSocketAddress() {
        int port;
        int BACKUP_PORT = 8080;
        if (System.getenv("MESSAGE_QUEUE_PORT") != null) {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } else {
            port = BACKUP_PORT;
            log.info("Using backup port at TraderAppMain");
        }
        return new InetSocketAddress("localhost", port);
    }
}
