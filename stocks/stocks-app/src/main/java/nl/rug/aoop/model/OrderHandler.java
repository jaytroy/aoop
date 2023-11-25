package nl.rug.aoop.model;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.actions.Order;
import nl.rug.aoop.model.components.Trader;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import static nl.rug.aoop.actions.Order.Type.MARKET;

/**
 * This is a class responsible for handling and resolving incoming orders.
 */
@Slf4j
public class OrderHandler {
    private Exchange exchange;
    private Map<String, PriorityQueue<Order>> buyOrders;
    private Map<String, PriorityQueue<Order>> sellOrders;

    public OrderHandler(Exchange exchange) {
        this.buyOrders = new HashMap<>();
        this.sellOrders = new HashMap<>();
        this.exchange = exchange;
    }

    /**
     * Method to place order.
     *
     * @param order the order.
     */
    public void placeOrder(Order order) throws NullPointerException {
        if (order.getType() == null) {
            log.error("Received order with null type from client " + order.getClientId());
            throw new NullPointerException("Order type is null");
        } else if(order.getAction() == null) {
            log.error("Received order with null action from client " + order.getClientId());
            throw new NullPointerException("Order action is null");
        }

        log.info("Received order from client " + order.getClientId() + ": " + order.getAction());

        String stockSymbol = order.getSymbol();
        if(order.getType() == MARKET) {
            //Order is of type market
            if (order.getAction() == Order.Action.BUY) {
                matchMarketOrder(order, sellOrders.get(stockSymbol), buyOrders);
            } else {
                matchMarketOrder(order, buyOrders.get(stockSymbol), sellOrders);
            }
        } else {
            //Order is of type limit
            if (order.getAction() == Order.Action.BUY) {
                matchLimitOrder(order, sellOrders.get(stockSymbol), buyOrders);
            } else {
                matchLimitOrder(order, buyOrders.get(stockSymbol), sellOrders);
            }
        }
    }

    private void matchLimitOrder(Order newOrder, PriorityQueue<Order> oppositeOrders, Map<String, PriorityQueue<Order>>
            sameTypeOrder) {
        log.info("Matching limit order " + newOrder);

        if (oppositeOrders != null && !oppositeOrders.isEmpty()) {
            while (!oppositeOrders.isEmpty() && newOrder.getQuantity() > 0) {
                Order headOrder = oppositeOrders.peek();
                if ((newOrder.getAction() == Order.Action.BUY && newOrder.getPrice() >= headOrder.getPrice()) ||
                        (newOrder.getAction() == Order.Action.SELL && newOrder.getPrice() <= headOrder.getPrice())) {
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

    private void matchMarketOrder(Order newOrder, PriorityQueue<Order> oppositeOrders, Map<String, PriorityQueue<Order>>
                                  sameTypeOrder) {
        log.info("Matching market order " + newOrder.getAction() + newOrder.getType());

        while(!oppositeOrders.isEmpty() && newOrder.getQuantity() > 0) {
            Order headOrder = oppositeOrders.peek();
            executeTrade(newOrder, headOrder, oppositeOrders);

            if(headOrder.getQuantity() == 0) {
                oppositeOrders.poll();
            }
        }

        //If market orders don't resolve immediately, they do not get executed.
        if(newOrder.getQuantity() > 0) {
            log.warn("Market order not fully executed, remaining quantity: " + newOrder.getQuantity());
        }
    }

    private void executeTrade(Order newOrder, Order headOrder, PriorityQueue<Order> oppositeOrders) {
        log.info("Executing order " + newOrder.getAction() + newOrder.getType());

        int tradedQuantity = (int) Math.min(newOrder.getQuantity(), headOrder.getQuantity());
        double tradePrice = headOrder.getPrice();
        newOrder.setQuantity(newOrder.getQuantity() - tradedQuantity);
        headOrder.setQuantity(headOrder.getQuantity() - tradedQuantity);

        //Get pointer to buyer and seller
        Trader buyer = exchange.findTraderById(newOrder.getAction() == Order.Action.BUY ? newOrder.getClientId() : headOrder.
                getClientId());
        Trader seller = exchange.findTraderById(newOrder.getAction() == Order.Action.SELL ? newOrder.getClientId() : headOrder.
                getClientId());

        //If buyer has enough money to buy a stock,
        if(buyer.getFunds() >= tradedQuantity * tradePrice) {
            //If buyer and seller exist
            if (buyer != null && seller != null) {
                //Execute order arithmetic and exchange stocks
                buyer.setFunds(buyer.getFunds() - tradedQuantity * tradePrice);
                buyer.addOwnedStock(newOrder.getSymbol(), tradedQuantity);
                seller.setFunds(seller.getFunds() + tradedQuantity * tradePrice);
                seller.removeOwnedStock(newOrder.getSymbol(), tradedQuantity);
                log.info("Executed trade for " + tradedQuantity + " shares of " + newOrder.getSymbol() + " " +
                        "at price " + tradePrice);

                //Update the exchange stock price
                exchange.updateStockPrice(newOrder.getSymbol(), headOrder.getPrice());
                //Update the traders after order completed
                exchange.updateTraders();
            } else {
                log.error("Buyer or Seller not found for the trade");
            }
            if (headOrder.getQuantity() == 0) {
                oppositeOrders.poll();
            }
        } else {
            log.error("Buyer does not have enough money");
        }
    }
}
