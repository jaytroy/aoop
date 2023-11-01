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
public class JayMain {
    /**
     * The main method that initializes the Stock App view and components.
     *
     * @param args the main args.
     */
    public static void main( String[] args ) {
        //I'm assuming most of this logic should be able to be moved out into the actual trader classes, or some
        // utility classes
        MessageHandler handler = new TraderHandler(); //Should this be done in the trader / client itself? Should each
        // have its own?
        int port;
        int BACKUP_PORT = 8080;
        InetSocketAddress address;
        if(System.getenv("MESSAGE_QUEUE_PORT") != null) {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } else {
            port = BACKUP_PORT;
            System.out.println("Using backup port at TraderAppMain");
        }
        address = new InetSocketAddress("localhost", port);
        Client client = new Client(handler, address);
        try {
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread clientThread = new Thread(client);
        clientThread.start();

        Trader jay = new Trader(client,"Jay", 0, 1000);
        Message msg = new Message("PUT","test");
        jay.putMessage(msg);
        for(int i = 0; i < 10; i++) {
            Message buy = new Message("BUY " + i,"test");
            Message sell = new Message("SELL " + i,"test");
            jay.putMessage(buy);
            jay.putMessage(sell);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                log.error("Thread failed to sleep");
            }
        }
        //jay.putMessage(null); Does not work
    }
}
