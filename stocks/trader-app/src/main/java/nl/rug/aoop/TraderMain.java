package nl.rug.aoop;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.client.Client;
import java.io.IOException;
import java.net.InetSocketAddress;
import lombok.extern.slf4j.Slf4j;

/**
 * The `JayMain` class represents the entry point for the Jay Trader application in the stock exchange system.
 */
@Slf4j
public class TraderMain {
    /**
     * The main method that initializes the Stock App view and components.
     *
     * @param args the main args.
     */
    public static void main( String[] args ) {
        TraderApp app = new TraderApp();
        app.initialize();
    }
}
