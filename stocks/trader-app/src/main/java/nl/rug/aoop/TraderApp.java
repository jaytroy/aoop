package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.client.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Random;

import static nl.rug.aoop.actions.Order.Type.BUY;
import static nl.rug.aoop.actions.Order.Type.SELL;


@Slf4j
public class TraderApp {
    public void initialize() {
        Trader trader1 = new Trader("bot1",getAdd());
        Trader trader2 = new Trader("bot2",getAdd());
        Trader trader3 = new Trader("bot3",getAdd());

        Random random = new Random();
        String[] stockSymbols = {"AAPL", "TSLA", "AMZN", "MSFT", "NVDA", "AMD", "ADBE", "FB", "INTC", "AOOP", "MRNA"};

        int numOrders = 10;
        for (int i = 0; i < numOrders; i++) {
            String randomStockSymbol = stockSymbols[random.nextInt(stockSymbols.length)];
            int randomQuantityBuy = random.nextInt(100) + 1;;
            double priceFactor = 1.0 + (0.01 * random.nextDouble());
            double limitPriceBuy = stock.getPrice() * priceFactor;
            double limitPriceSell = stock.getPrice() / priceFactor;
            int buyOrSell = random.nextInt(2);

            if (buyOrSell == 1) {
                trader1.placeOrder(BUY, randomStockSymbol, randomQuantityBuy, limitPriceBuy);
            } else {
                trader1.placeOrder(SELL, randomStockSymbol,randomQuantityBuy, limitPriceSell);
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                log.error("Thread failed to sleep");
            }
        }

/*
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
        Message msg = new Message("PUT","BUY 10 AMD");
        jay.putMessage(msg);

        //jay.putMessage(null); Does not work
        */
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
