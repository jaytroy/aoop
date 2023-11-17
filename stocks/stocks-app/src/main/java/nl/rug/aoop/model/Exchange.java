package nl.rug.aoop.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.actions.Order;
import nl.rug.aoop.messagequeue.serverside.ConsumerObserver;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.OrderedQueue;
import nl.rug.aoop.messagequeue.serverside.NetConsumer;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
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
    private MessageQueue buyQueue;
    private MessageQueue sellQueue;
    private NetConsumer consumer;
    private Map<String, PriorityQueue<Order>> buyOrders;
    private Map<String, PriorityQueue<Order>> sellOrders;

    /**
     * Constructs an Exchange with the specified message queue.
     *
     * @param messageQueue The message queue used for communication.
     * @param server The server to which this exchange is associated.
     */
    public Exchange(MessageQueue messageQueue, Server server) {
        this.server = server;
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();

        this.connectedClients =  server.getClientHandlers();

        stocks = initializeStocks();
        traders = initializeTraders();

        this.messageQueue = messageQueue;
        consumer = new NetConsumer(messageQueue, this);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        this.buyQueue = new OrderedQueue();
        this.sellQueue = new OrderedQueue();

        updateClients();
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
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * Updates all connected clients at regular intervals.
     */
    public void updateClients() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                log.info("Sending update to " + server.getClientHandlers().size() + " clients");
                for (ClientHandler handler : server.getClientHandlers().values()) {
                    sendTraderInformation(handler);
                }
            }
        }, 0, 4000);
    }

    /**
     * Send trader information to connected clients.
     *
     * @param handler the clienthandler.
     */
    private void sendTraderInformation(ClientHandler handler) {
        String handlerId = handler.getId();

        if(handlerId != null) {
            String traderInfo = generateTraderInformation(handlerId);
            handler.sendMessage(traderInfo);
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

        if (trader != null) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Trader.class, new TraderTypeAdapter())
                    .create();
            return gson.toJson(trader);
        } else {
            return "Trader not found";
        }
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

    /**
     * Method to place order.
     *
     * @param order the order.
     */
    public void placeOrder(Order order) {
        log.info("Received order from client " + order.getClientId() + ": " + order);

        String stockSymbol = order.getSymbol();
        if (order.getType() == Order.Type.BUY) {
            matchOrder(order, sellOrders.get(stockSymbol), buyOrders);
        } else {
            matchOrder(order, buyOrders.get(stockSymbol), sellOrders);
        }
    }

    private void matchOrder(Order newOrder, PriorityQueue<Order> oppositeOrders, Map<String, PriorityQueue<Order>>
            sameTypeOrder) {
        log.info("Matching order " + newOrder);
        if (oppositeOrders != null && !oppositeOrders.isEmpty()) {
            while (!oppositeOrders.isEmpty() && newOrder.getQuantity() > 0) {
                Order headOrder = oppositeOrders.peek();
                if ((newOrder.getType() == Order.Type.BUY && newOrder.getPrice() >= headOrder.getPrice()) ||
                        (newOrder.getType() == Order.Type.SELL && newOrder.getPrice() <= headOrder.getPrice())) {
                    executeTrade(newOrder, headOrder, oppositeOrders);
                } else {
                    break;
                }
            }
        }

        if (newOrder.getQuantity() > 0) {
            sameTypeOrder.computeIfAbsent(newOrder.getSymbol(), k -> new PriorityQueue<>()).add(newOrder);
        }
    }

    private void executeTrade(Order newOrder, Order headOrder, PriorityQueue<Order> oppositeOrders) {
        int tradedQuantity = (int) Math.min(newOrder.getQuantity(), headOrder.getQuantity());
        double tradePrice = headOrder.getPrice();

        newOrder.setQuantity(newOrder.getQuantity() - tradedQuantity);
        headOrder.setQuantity(headOrder.getQuantity() - tradedQuantity);

        Trader buyer = findTraderById(newOrder.getType() == Order.Type.BUY ? newOrder.getClientId() : headOrder.
                getClientId());
        Trader seller = findTraderById(newOrder.getType() == Order.Type.SELL ? newOrder.getClientId() : headOrder.
                getClientId());
        if (buyer != null && seller != null) {
            buyer.setFunds(buyer.getFunds() - tradedQuantity * tradePrice);
            buyer.addOwnedStock(newOrder.getSymbol(), tradedQuantity);

            seller.setFunds(seller.getFunds() + tradedQuantity * tradePrice);
            seller.removeOwnedStock(newOrder.getSymbol(), tradedQuantity);

            System.out.println("Executed trade for " + tradedQuantity + " shares of " + newOrder.getSymbol() + " " +
                    "at price " + tradePrice);

            updateStockPrice(newOrder.getSymbol(), headOrder.getPrice());
        } else {
            log.error("Buyer or Seller not found for the trade");
        }

        if (headOrder.getQuantity() == 0) {
            oppositeOrders.poll();
        }
    }

    private void updateStockPrice(String stockSymbol, double tradePrice) {
        Stock tradedStock = findStockBySymbol(stockSymbol);
        if (tradedStock != null) {
            tradedStock.setPrice(tradePrice);
        } else {
            log.error("Stock not found for symbol: " + stockSymbol);
        }
    }

    @Override
    public void update(Message msg) {
        String body = msg.getBody();
        Order order = Order.fromJson(body);
        placeOrder(order);
    }

    private Trader findTraderById(String id) {
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
