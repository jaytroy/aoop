package nl.rug.aoop.network;

import nl.rug.aoop.Stock;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.stockcommands.BuyLimitOrderCommand;
import nl.rug.aoop.stockcommands.SellLimitOrderCommand;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.stockcommands.BuyLimitOrderCommand;
import nl.rug.aoop.stockcommands.SellLimitOrderCommand;

public class OrderHandler implements MessageHandler {

    @Override
    public void handleMessage(String message) {
        String[] orderInfo = message.split(" "); // Split the message into parts

        if (orderInfo.length < 4) {
            System.out.println("Invalid order message: " + message);
            return;
        }

        String orderType = orderInfo[0]; // BUY or SELL
        String traderId = orderInfo[1]; // Trader's ID
        String stockSymbol = orderInfo[2]; // Stock symbol
        double limitPrice = Double.parseDouble(orderInfo[3]); // Limit price

        // Assuming you have access to the traders and stocks, find the corresponding trader and stock objects

        // Example: Find the trader by ID
        Trader trader = findTraderById(traderId);

        // Example: Find the stock by symbol
        Stock stock = findStockBySymbol(stockSymbol);

        if (trader == null || stock == null) {
            System.out.println("Trader or stock not found for order: " + message);
            return;
        }

        if ("BUY".equalsIgnoreCase(orderType)) {
            int quantity = 1;
            BuyLimitOrderCommand buy = new BuyLimitOrderCommand(trader, stock, limitPrice, quantity);
            buy.execute();
        } else if ("SELL".equalsIgnoreCase(orderType)) {
            int quantity = 1;
            SellLimitOrderCommand sell = new SellLimitOrderCommand(trader, stock, limitPrice, quantity);
            sell.execute();
        } else {
            System.out.println("Unknown command: " + orderType);
        }
    }

    private Trader findTraderById(String traderId) {
        return null;
    }

    private Stock findStockBySymbol(String stockSymbol) {
        return null;
    }
}
