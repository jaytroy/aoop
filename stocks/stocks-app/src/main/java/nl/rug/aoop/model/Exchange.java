package nl.rug.aoop.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.actions.Order;
import nl.rug.aoop.messagequeue.serverside.ConsumerObserver;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.serverside.NetConsumer;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.model.components.Stock;
import nl.rug.aoop.model.components.StockList;
import nl.rug.aoop.model.components.Trader;
import nl.rug.aoop.model.components.TraderList;
import nl.rug.aoop.networking.server.ClientHandler;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.uimodel.StockDataModel;
import nl.rug.aoop.uimodel.StockExchangeDataModel;
import nl.rug.aoop.uimodel.TraderDataModel;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Exchange class represents the central component of the trading system. It keeps track of all the stocks, traders,
 * and resolves orders.
 */
@Slf4j
public class Exchange implements StockExchangeDataModel, ConsumerObserver {
    @Getter
    private Server server;
    private ConcurrentHashMap<String, ClientHandler> connectedClients;
    @Getter
    private List<Stock> stocks;
    @Getter
    private List<Trader> traders;
    @Getter
    private MessageQueue messageQueue;
    @Setter
    private NetConsumer consumer;
    private OrderHandler orderHandler;

    /**
     * Constructs an Exchange with the specified message queue.
     *
     * @param messageQueue The message queue used for communication.
     * @param server       The server to which this exchange is associated.
     */
    public Exchange(MessageQueue messageQueue, Server server) {
        this.server = server;
        this.connectedClients = server.getClientHandlers();

        stocks = initializeStocks();
        traders = initializeTraders();

        this.messageQueue = messageQueue;
        consumer = new NetConsumer(messageQueue, this);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        this.orderHandler = new OrderHandler(this);

        //Sets up periodic updates for the exchange UI
        periodicUpdateStart();
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
            log.error("Failed to initialise stocks");
            return new ArrayList<>();
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
            log.error("Failed to initialise traders");
            return new ArrayList<>();
        }
    }

    /**
     * Update traders every 4 seconds.
     */
    public void periodicUpdateStart() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                updateTraders();
            }
        }, 0, 4000);
    }

    /**
     * Updates all connected traders at regular intervals.
     */
    public void updateTraders() {
        log.info("Sending update to " + connectedClients.size() + " clients");
        for (ClientHandler handler : connectedClients.values()) {
            sendTraderInformation(handler);
            sendStockInformation(handler);
        }
    }

    /**
     * Send trader information to connected clients.
     *
     * @param handler the clienthandler.
     */
    private void sendTraderInformation(ClientHandler handler) {
        String handlerId = handler.getId();

        if (handlerId != null) {
            String traderInfo = generateTraderInformation(handlerId);
            Message msg = new Message("TRADER", traderInfo);
            String jsonmsg = msg.toJson();
            handler.sendMessage(jsonmsg);
        } else {
            log.warn("No client connected with ID: " + handlerId);
        }
    }

    /**
     * Generate trader information for a specific client.
     *
     * @param id The client id to generate information for.
     * @return A string containing trader information for the client.
     */
    private String generateTraderInformation(String id) {
        Trader trader = findTraderById(id);
        return trader.toJson();
    }

    private void sendStockInformation(ClientHandler handler) {
        String stockInfo = generateStockInformation();

        if (!stockInfo.isEmpty()) {
            Message msg = new Message("STOCK", stockInfo);
            String jsonMsg = msg.toJson();
            handler.sendMessage(jsonMsg);
        } else {
            log.warn("No stock information available");
        }
    }

    private String generateStockInformation() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(stocks);
    }

    @Override
    public StockDataModel getStockByIndex(int index) {
        if (index >= 0 && index < stocks.size()) {
            return stocks.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getNumberOfStocks() {
        return stocks.size();
    }

    @Override
    public TraderDataModel getTraderByIndex(int index) {
        if (index >= 0 && index < traders.size()) {
            return traders.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getNumberOfTraders() {
        return traders.size();
    }



    protected void updateStockPrice(String stockSymbol, double tradePrice) {
        Stock tradedStock = findStockBySymbol(stockSymbol);
        if (tradedStock != null) {
            tradedStock.setPrice(tradePrice);
        } else {
            log.error("Stock not found for symbol: " + stockSymbol);
        }
    }

    /**
     * This receives messages from the NetConsumer, which are continuously polled from the TSMessageQueue.
     * @param msg The message.
     */
    @Override
    public void update(Message msg) {
        String body = msg.getBody();

        Order order = Order.fromJson(body);
        System.out.println(body);
        try {
            orderHandler.placeOrder(order);
        } catch(NullPointerException e) {
            log.error("Tried to place a null order");
        }
    }

    protected Trader findTraderById(String id) {
        for (Trader trader : traders) {
            if (trader.getId().equals(id)) {
                return trader;
            }
        }
        return null;
    }

    private Stock findStockBySymbol(String symbol) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equals(symbol)) {
                return stock;
            }
        }
        return null;
    }
}
