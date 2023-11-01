package nl.rug.aoop.network;

import lombok.Getter;
import nl.rug.aoop.model.Stock;
import nl.rug.aoop.model.Trader;
import nl.rug.aoop.model.TraderList;
import nl.rug.aoop.messagequeue.serverside.NetConsumer;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.model.StockList;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * The Exchange class represents the central component of the trading system. It keeps track of all the stocks, traders,
 * and resolves orders.
 */
public class Exchange implements Runnable {
    private List<Client> connectedClients; // This can probably be removed to be replaced by listeners
    @Getter
    private List<Stock> stocks;
    @Getter
    private List<Trader> traders;
    private MessageQueue messageQueue;
    private NetConsumer consumer; // Replace this with an interface? Throws an error. MQConsumer is not runnable.
    private List<ExchangeListener> listeners;

    /**
     * Constructs an Exchange with the specified message queue.
     *
     * @param messageQueue The message queue used for communication.
     */
    public Exchange(MessageQueue messageQueue) {
        connectedClients = new ArrayList<>();
        listeners = new ArrayList<>();

        stocks = initializeStocks();
        traders = initializeTraders();

        this.messageQueue = messageQueue; // Initialize the message queue. Will be run "locally" in the exchange
        consumer = new NetConsumer(messageQueue); // This thread will continuously poll messages
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        updateListeners();
    }

    @Override
    public void run() {

    }

    /**
     * Initializes the list of stocks by loading stock information from a YAML file.
     *
     * @return A list of stocks.
     */
    public List<Stock> initializeStocks() {
        try {
            YamlLoader stockLoader = new YamlLoader(Path.of("./data/stocks.yaml"));
            StockList stockList = stockLoader.load(StockList.class);
            return stockList.getStocks();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list. Why?
        }
    }

    /**
     * Initializes the list of traders by loading trader information from a YAML file.
     *
     * @return A list of traders.
     */
    public List<Trader> initializeTraders() {
        try {
            YamlLoader traderLoader = new YamlLoader(Path.of("./data/traders.yaml"));
            TraderList traderList = traderLoader.load(TraderList.class);
            return traderList.getTraders();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list
        }
    }

    /**
     * Track connected clients. This method should continuously run to update listeners.
     */
    public void trackConnectedClients() {
        for (Client client : connectedClients) {
            if (client.isConnected()) {
                updateListeners();
            }
        }
    }

    /**
     * Updates all registered listeners at regular intervals.
     */
    public void updateListeners() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for (ExchangeListener listener : listeners) {
                    listener.update();
                }
            }
        }, 0, 1000);
    }

    /**
     * Adds a listener (a trader) to the exchange. Call this when a client connects.
     *
     * @param l The listener to add.
     */
    public void addListener(ExchangeListener l) {
        listeners.add(l);
    }

    /**
     * Send stock information to connected clients.
     */
    private void sendStockInformation() {
        for (Client client : connectedClients) {
            String stockInfo = generateStockInformationForClient(client);
            client.sendMessage(stockInfo);
        }
    }

    /**
     * Send trader information to connected clients.
     */
    private void sendTraderInformation() {
        for (Client client : connectedClients) {
            String traderInfo = generateTraderInformationForClient(client);
            client.sendMessage(traderInfo);
        }
    }

    /**
     * Generate stock information for a specific client.
     *
     * @param client The client to generate information for.
     * @return A string containing stock information for the client.
     */
    private String generateStockInformationForClient(Client client) {
        StringBuilder stockInfo = new StringBuilder();
        for (Stock stock : stocks) {
            stockInfo.append(stock.getSymbol()).append(": ").append(stock.getPrice()).append("\n");
        }
        return stockInfo.toString();
    }

    /**
     * Generate trader information for a specific client.
     *
     * @param client The client to generate information for.
     * @return A string containing trader information for the client.
     */
    private String generateTraderInformationForClient(Client client) {
        StringBuilder traderInfo = new StringBuilder();
        for (Trader traderUI : traders) {
            traderInfo.append(traderUI.getName()).append(": ").append(traderUI.getFunds()).append("\n");
        }
        return traderInfo.toString();
    }

    /**
     * Add a client to the list of connected clients.
     *
     * @param client The client to add.
     */
    public void addClient(Client client) {
        connectedClients.add(client);
    }
}
