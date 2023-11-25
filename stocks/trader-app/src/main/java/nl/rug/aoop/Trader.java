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

import static nl.rug.aoop.actions.Order.Action.BUY;
import static nl.rug.aoop.actions.Order.Type.MARKET;

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
    @Getter
    private NetProducer producer;
    private Client client;
    private MessageHandler handler;
    @Getter
    private InetSocketAddress address;
    @Setter
    @Getter
    private TraderFacade traderFacade;

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
        traderFacade = new TraderFacade(this);
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
        while (true) {
            traderFacade.executeStrategy();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                log.error("Thread sleep interrupted", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Place an order with the specified type, stock symbol, quantity, and price.
     *
     * @param action    The action of the order (BUY or SELL).
     * @param symbol    The symbol of the stock.
     * @param quantity  The quantity of the stock to be traded.
     * @param price     The price per unit of the stock.
     */
    public void placeOrder(Order.Action action, Order.Type type, String symbol, long quantity, double price) {
        //Is this duplicated?
        if(action == BUY) {
            setAvailableFunds(getAvailableFunds() - price * quantity);
        } else {
            Integer q = getOwnedStocks().get(symbol);
            getOwnedStocks().put(symbol, q - (int) quantity);
        }

        Order order;
        if(type == MARKET) {
            order = new Order(action, getId(), symbol, quantity, LocalDateTime.now());
        } else {
            order = new Order(action, getId(), symbol, quantity, price, LocalDateTime.now());
        }
        Message msg = new Message("PUT", order.toJson());
        getProducer().putMessage(msg);
        log.info("Placed order: {}", order.toJson());
    }

    /**
     * Updates the info of the trader.
     *
     * @param funds the funds of the trader.
     * @param name the name of the trader.
     * @param ownedStocks the owned stocks of the trader.
     */
    public void updateInfo(double funds, String name, Map<String,Integer> ownedStocks) {
        setAvailableFunds(funds);
        setName(name);
        setOwnedStocks(ownedStocks);
        log.info("Updated information of " + id);
    }

    /**
     * Updates the stock info.
     *
     * @param stocks the stocks.
     */
    public void updateStocks(List<Stock> stocks) {
        availableStocks = stocks;
        log.info("Updated stocks information");
    }
}
