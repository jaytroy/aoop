package nl.rug.aoop;

import nl.rug.aoop.networking.MessageHandler;

import java.net.InetSocketAddress;

public class OmarMain {
    public static void main( String[] args ) {
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

        Trader omar = new Trader("Omar", 1, 1000, handler, address);

        omar.sendMessage("Hello");
    }
}