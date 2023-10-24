package nl.rug.aoop;

import nl.rug.aoop.networking.MessageHandler;

import java.net.InetSocketAddress;

/**
 * Hello world!
 *
 */
public class JayMain {
    public static void main( String[] args ) {
        //I'm assuming most of this logic should be able to be moved out into the actual trader classes, or some utility classes
        MessageHandler handler = new TraderHandler(); //Should this be done in the trader / client itself? Should each have its own?
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

        Trader jay = new Trader("Jay", 0, 1000, handler, address);

        jay.sendMessage("Hello");
    }
}
