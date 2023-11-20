package nl.rug.aoop;

import lombok.Getter;
import lombok.Setter;
import nl.rug.aoop.actions.Order;
import static nl.rug.aoop.actions.Order.Type.BUY;
import static nl.rug.aoop.actions.Order.Type.SELL;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.serverside.NetProducer;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.NetworkMessage;
import nl.rug.aoop.networking.client.Client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

/**
 * The Trader class represents a participant in the stock exchange, including their name, available funds, and
 * owned stocks.
 */
public class Trader {
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
    private NetProducer producer;
    private Client client;
    private MessageHandler handler;
    private InetSocketAddress address;

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

        try {
            client.connect();
            Thread clientThread = new Thread(client);
            clientThread.start();
        } catch (IOException e) {
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
        NetworkMessage nmsg = new NetworkMessage(msg.getHeader(), msg.getBody());
        client.sendMessage(nmsg.toJson());
    }

    /**
     * Implement a trader strategy for generating random orders.
     */
    public void traderStrategy() {
        Random random = new Random();
        String[] stockSymbols = {"AAPL", "TSLA", "AMZN", "MSFT", "NVDA", "AMD", "ADBE", "FB", "INTC", "AOOP", "MRNA"};
        String randomStockSymbol = stockSymbols[random.nextInt(stockSymbols.length)];
        int randomQuantityBuy = random.nextInt(100) + 1;
        double priceFactor = 1.0 + (0.01 * random.nextDouble());
        int price = random.nextInt(1000) + 1;
        double limitPriceBuy = price * priceFactor;
        double limitPriceSell = price / priceFactor;
        int buyOrSell = random.nextInt(2);

        if (buyOrSell == 1) {
            placeOrder(BUY, randomStockSymbol, randomQuantityBuy, limitPriceBuy);
        } else {
            placeOrder(SELL, randomStockSymbol, randomQuantityBuy, limitPriceSell);
        }
    }
}
