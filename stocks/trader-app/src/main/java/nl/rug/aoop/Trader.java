package nl.rug.aoop;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.actions.Order;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.serverside.NetProducer;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.client.Client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static nl.rug.aoop.actions.Order.Type.BUY;
import static nl.rug.aoop.actions.Order.Type.SELL;

/**
 * The Trader class represents a participant in the stock exchange, including their name, available funds, and
 * owned stocks.
 */
@Slf4j
public class Trader implements Runnable {
    @Getter
    @Setter
    private String name;
    @Getter
    private String id;
    @Getter
    @Setter
    private double availableFunds;
    @Getter
    @Setter
    private Map<String, Integer> ownedStocks; // Map to track owned stocks (stock symbol, quantity)
    @Getter
    @Setter
    private List<Stock> availableStocks;
    private NetProducer producer;
    private Client client;
    private MessageHandler handler;
    private InetSocketAddress address;
    private TraderStrategy strategy;

    /**
     * Constructs a Trader with the given ID and network address.
     *
     * @param id      The ID of the trader.
     * @param address The network address of the trader.
     */
    public Trader(String id, InetSocketAddress address) {
        this.id = id;
        this.address = address;

        handler = new TraderHandler(this);
        client = new Client(handler, address, id);
        producer = new NetProducer(client);
        strategy = new TraderStrategy(this);

        try {
            client.connect();
            Thread clientThread = new Thread(client);
            clientThread.start();
        } catch (IOException e) {
            log.error("Failed to start trader client");
        }


    }

    @Override
    public void run() {
        // Delay execution by 5 seconds to ensure we don't load null data
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("Thread sleep interrupted", e);
            Thread.currentThread().interrupt();
        }

        // Execute the strategy periodically
        while (true) {
            Random random = new Random();
            long randomDelay = 1 + random.nextLong(100);  // Replace numbers with constants
            System.out.println(randomDelay);

            strategy.executeStrategy();
            try {
                Thread.sleep(randomDelay);
            } catch (InterruptedException e) {
                log.error("Thread sleep interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Place an order with the specified type, stock symbol, quantity, and price.
     *
     * @param type      The type of the order (BUY or SELL).
     * @param symbol    The symbol of the stock.
     * @param quantity  The quantity of the stock to be traded.
     * @param price     The price per unit of the stock.
     */
    public void placeOrder(Order.Type type, String symbol, long quantity, double price) {
        Order order = new Order(type, this.id, symbol, quantity, price, LocalDateTime.now());
        Message msg = new Message("PUT", order.toJson());
        producer.putMessage(msg);
        log.info("Placed order: {}", order.toJson());
    }


    public void updateInfo(double funds, String name, Map<String,Integer> ownedStocks) {
        setAvailableFunds(funds);
        setName(name);
        setOwnedStocks(ownedStocks);
        log.info("Updated information of " + id);
    }

    public void updateStocks(List<Stock> stocks) {
        availableStocks = stocks;
        log.info("Updated stocks information");
    }
}
