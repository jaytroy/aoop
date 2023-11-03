package nl.rug.aoop;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.actions.Order;

import static nl.rug.aoop.actions.Order.Type.BUY;
import static nl.rug.aoop.actions.Order.Type.SELL;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.serverside.NetProducer;
import nl.rug.aoop.network.ExchangeListener;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.NetworkMessage;
import nl.rug.aoop.networking.client.Client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * The Trader class represents a participant in the stock exchange, including their name, available funds, and
 * owned stocks.
 */
public class Trader implements ExchangeListener {
    @Getter
    @Setter
    private String name;
    @Getter
    private String id;
    @Getter
    private double availableFunds;
    @Getter
    @Setter
    private Map<String, Integer> ownedStocks; // Map to track owned stocks (stock symbol, quantity)
    private NetProducer producer;
    private Client client;
    private MessageHandler handler;
    private InetSocketAddress address;

    /**
     * Constructs a Trader with the given client, name, ID, and available funds.
     *
     * @param id              The ID of the trader.
     */
    public Trader(String id, InetSocketAddress address) {
        this.id = id;
        this.address = address;

        handler = new TraderHandler(this);
        client = new Client(handler,address,id);
        producer = new NetProducer(client);

        try {
            client.connect();
            Thread clientThread = new Thread(client);
            clientThread.start();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the available funds for trading.
     *
     * @param funds The new available funds.
     */
    public void setAvailableFunds(double funds) {
        availableFunds = funds;
    }

    /**
     * Add owned stocks to the trader's portfolio. This method is used for buying stocks.
     *
     * @param stockSymbol The symbol of the stock to be added.
     * @param quantity    The quantity of the stock to be added.
     */
    public void addOwnedStock(String stockSymbol, int quantity) {
        ownedStocks.put(stockSymbol, ownedStocks.getOrDefault(stockSymbol, 0) + quantity);
    }

    /**
     * Subtract owned stocks from the trader's portfolio. This method is used for selling stocks.
     *
     * @param stockSymbol The symbol of the stock to be subtracted.
     * @param quantity    The quantity of the stock to be subtracted.
     */
    public void subtractOwnedStock(String stockSymbol, int quantity) {
        int currentQuantity = ownedStocks.getOrDefault(stockSymbol, 0);
        if (currentQuantity >= quantity) {
            ownedStocks.put(stockSymbol, currentQuantity - quantity);
        }
    }

    @Override
    public void update(Map<String,Integer> ownedStocks, int availableFunds) {
        this.ownedStocks = ownedStocks;
        this.availableFunds = availableFunds;
    }

    public void placeOrder(Order.Type type, String symbol, long quantity, double price) {
        Order order = new Order(type, this.id, symbol, quantity, price, LocalDateTime.now());
        Message msg = new Message("PUT",order.toJson());
        NetworkMessage nmsg = new NetworkMessage(msg.getHeader(),msg.getBody());
        client.sendMessage(nmsg.toJson());
    }

    public void traderStrat() {
        Random random1 = new Random();
        long quantity = random1.nextInt(10);

        Random random2 = new Random();
        int price = random2.nextInt(100);

        String ticker = "AMD"; //Ideally this would be loaded from the exchange. We don't have time to implement that.

        Random random3 = new Random();g
        int num = random3.nextInt(10);
        if(num < 5) {
            placeOrder(BUY,ticker,quantity,price);
        } else {
            placeOrder(SELL,ticker,quantity,price);
        }
    }
}
