package nl.rug.aoop.model;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.actions.Order;
import nl.rug.aoop.messagequeue.serverside.ConsumerObserver;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.OrderedQueue;
import nl.rug.aoop.messagequeue.serverside.NetConsumer;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.server.ClientHandler;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.uimodel.StockDataModel;
import nl.rug.aoop.uimodel.StockExchangeDataModel;
import nl.rug.aoop.uimodel.TraderDataModel;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * The Exchange class represents the central component of the trading system. It keeps track of all the stocks, traders,
 * and resolves orders.
 */
@Slf4j
public class Exchange implements Runnable, StockExchangeDataModel, ConsumerObserver {
    private Server server;
    private List<Client> connectedClients; // Or should it be clienthandlers? //This is stored in server
    @Getter
    private List<Stock> stocks;
    @Getter
    private List<Trader> traders;
    private MessageQueue messageQueue;
    private MessageQueue buyQueue;
    private MessageQueue sellQueue;
    private NetConsumer consumer; // Replace this with an interface? Throws an error. MQConsumer is not runnable.
    private Map<String, PriorityQueue<Order>> buyOrders;
    private Map<String, PriorityQueue<Order>> sellOrders;

    /**
     * Constructs an Exchange with the specified message queue.
     *
     * @param messageQueue The message queue used for communication.
     */
    public Exchange(MessageQueue messageQueue, Server server) {
        this.server = server;

        connectedClients = new ArrayList<>();

        stocks = initializeStocks();
        traders = initializeTraders();

        this.messageQueue = messageQueue;
        consumer = new NetConsumer(messageQueue, this); //This thread will continuously poll messages from queu
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        this.buyQueue = new OrderedQueue();
        this.sellQueue = new OrderedQueue();

        updateClients(); //necessary?
    }

    /**
     * This scans for and executes each order as required. Should this be here? Move it out into a consumer?
     */
    @Override
    public void run() { //This does not run in parallel, unfortunately. It should work nonetheless
        while (!Thread.currentThread().isInterrupted()) { //Should we create threads for both queues?
            if (buyQueue.getSize() != 0) { //Or maybe use TSMessageQueue? I don't know...
                Message m = buyQueue.dequeue();
                //Execute command

            }
            if (sellQueue.getSize() != 0) {
                Message m = sellQueue.dequeue();
                //Execute command
            }
        }
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
     * Updates all connected clients at regular intervals.
     */
    public void updateClients() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                log.info("Updating " + server.getClientHandlers().size() + " clients");
                for (ClientHandler handler : server.getClientHandlers().values()) {
                    //sendStockInformation(handler);
                    sendTraderInformation(handler);
                }
            }
        }, 0, 4000);
    }

    /**
     * Send stock information to connected clients.
     */
    private void sendStockInformation(ClientHandler handler) {
        String stockInfo = generateStockInformation(handler);
        handler.sendMessage(stockInfo);
    }

    /**
     * Send trader information to connected clients.
     */
    private void sendTraderInformation(ClientHandler handler) {
        String handlerId = handler.getId();

        if(handlerId != null) {
            String traderInfo = generateTraderInformation(handlerId);
            handler.sendMessage(traderInfo); //this should be via clienthandler somehow
        } else {
            log.warn("No client connected with ID: " + handlerId);
        }
    }

    /**
     * Generate stock information for a specific client.
     *
     * @param handler The client to generate information for.
     * @return A string containing stock information for the client.
     */
    private String generateStockInformation(ClientHandler handler) {
        StringBuilder stockInfo = new StringBuilder();
        for (Stock stock : stocks) {
            stockInfo.append(stock.getSymbol()).append(": ").append(stock.getPrice()).append("\n");
        }
        return stockInfo.toString();
    }

    /**
     * Generate trader information for a specific client.
     *
     * @param id The client id to generate information for.
     * @return A string containing trader information for the client.
     */
    private String generateTraderInformation(String id) {
        StringBuilder traderInfo = new StringBuilder();
        Trader trader = findTraderById(id);

        traderInfo.append("Trader ID: ").append(trader.getId()).append("~");
        traderInfo.append("Name: ").append(trader.getName()).append("~");
        traderInfo.append("Funds: ").append(trader.getFunds()).append("~");

        Map<String, Integer> ownedStocks = trader.getOwnedStocks();
        traderInfo.append("Owned Stocks:~");
        for (Map.Entry<String, Integer> entry : ownedStocks.entrySet()) {
            traderInfo.append(entry.getKey()).append(": ").append(entry.getValue()).append("~");
        }

        return traderInfo.toString();
    }

    @Override
    public StockDataModel getStockByIndex(int index) {
        if (index >= 0 && index < stocks.size()) {
            return (StockDataModel) stocks.get(index);
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
            return (TraderDataModel) traders.get(index);
        } else {
            return null;
        }
    }

    @Override
    public int getNumberOfTraders() {
        return traders.size();
    }

    public void placeOrder(Order order) {
        log.info("Received order from client " + order.getClientId() + ": " + order);

        String stockSymbol = order.getSymbol();
        if (order.getType() == Order.Type.BUY) {
            matchOrder(order, sellOrders.get(stockSymbol), buyOrders);
        } else {
            matchOrder(order, buyOrders.get(stockSymbol), sellOrders);
        }
    }

    private void matchOrder(Order newOrder, PriorityQueue<Order> oppositeOrders, Map<String, PriorityQueue<Order>> sameTypeOrderBooks) {
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
            sameTypeOrderBooks.computeIfAbsent(newOrder.getSymbol(), k -> new PriorityQueue<>()).add(newOrder);
        }
    }

    private void executeTrade(Order newOrder, Order headOrder, PriorityQueue<Order> oppositeOrders) {
        int tradedQuantity = (int) Math.min(newOrder.getQuantity(), headOrder.getQuantity());
        newOrder.setQuantity(newOrder.getQuantity() - tradedQuantity);
        headOrder.setQuantity(headOrder.getQuantity() - tradedQuantity);

        System.out.println("Executed trade for " + tradedQuantity + " shares of " + newOrder.getSymbol() + " at price " + headOrder.getPrice());

        if (headOrder.getQuantity() == 0) {
            oppositeOrders.poll();
        }
    }

    @Override
    public void update(Message msg) { //Gets order from consumer
        String body = msg.getBody();
        Order order = new Order(body);
        //placeOrder(order);
    }

    private Trader findTraderById(String id) {
        for (Trader trader : traders) {
            if (trader.getId().equals(id)) {
                return trader;
            }
        }
        return null;
    }
}
