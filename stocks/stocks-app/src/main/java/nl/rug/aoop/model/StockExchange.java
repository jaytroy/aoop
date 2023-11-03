package nl.rug.aoop.model;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.actions.Order;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.TSMessageQueue;

import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The `StockExchange` class represents a stock exchange where orders are matched and trades are executed.
 * It maintains order books for buy and sell orders and processes incoming orders from clients.
 */
@Slf4j
public class StockExchange {
    private MessageQueue orderQueue;
    private Map<String, PriorityQueue<Order>> buyOrders;
    private Map<String, PriorityQueue<Order>> sellOrders;
    private final Map<String, Trader> connectedTraders = new ConcurrentHashMap<>();
    private final Map<String, Stock> stocks = new ConcurrentHashMap<>();

    /**
     * Initializes a new instance of the `StockExchange` class.
     */
    public StockExchange() {
        this.orderQueue = new TSMessageQueue();
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();
    }

    /**
     * Places an order in the stock exchange.
     *
     * @param order The order to be placed.
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
            sameTypeOrderBooks) {
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

        System.out.println("Executed trade for " + tradedQuantity + " shares of " + newOrder.getSymbol() + " at price "
                + headOrder.getPrice());

        if (headOrder.getQuantity() == 0) {
            oppositeOrders.poll();
        }
    }
}
