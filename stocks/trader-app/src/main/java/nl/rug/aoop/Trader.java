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
    private List<Stock> availableStocks;
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
            log.error("Failed to start trader client");
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
    }

    /**
     * Implement a trader strategy for generating random orders.
     *
     */

    public void traderStrategy() {
        Random random = new Random();

        // For buying, get all possible stock symbols from available stocks
        List<String> stockSymbolsToBuy = availableStocks.stream().map(Stock::getSymbol).toList();

        // For selling, get stock symbols from the symbols of owned stocks
        List<String> stockSymbolsToSell = ownedStocks.keySet().stream().toList();

        int randomQuantityBuy = random.nextInt(100) + 1;
        double priceFactor = 1.0 + (0.01 * random.nextDouble());

        // For buying, randomly choose a stock symbol from available stocks
        String randomStockSymbolBuy = stockSymbolsToBuy.get(random.nextInt(stockSymbolsToBuy.size()));

        // For selling, randomly choose a stock symbol from owned stocks
        String randomStockSymbolSell = stockSymbolsToSell.get(random.nextInt(stockSymbolsToSell.size()));

        // Choose the correct stock price from available stocks
        Stock chosenStock = availableStocks.stream()
                .filter(stock -> stock.getSymbol().equals(randomStockSymbolBuy))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Stock not found for symbol: " + randomStockSymbolBuy));

        double price = chosenStock.getPrice();

        double limitPriceBuy = price * priceFactor;
        double limitPriceSell = price / priceFactor;

        int buyOrSell = random.nextInt(2);

        if (buyOrSell == 1) {
            placeOrder(BUY, randomStockSymbolBuy, randomQuantityBuy, limitPriceBuy);
        } else {
            placeOrder(SELL, randomStockSymbolSell, randomQuantityBuy, limitPriceSell);
        }
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
