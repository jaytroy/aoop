package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;



@Slf4j
public class TraderApp {
    public void initialize() {
        Trader trader1 = new Trader("bot1",getAdd());
        Trader trader2 = new Trader("bot2",getAdd());
        Trader trader3 = new Trader("bot3",getAdd());
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
