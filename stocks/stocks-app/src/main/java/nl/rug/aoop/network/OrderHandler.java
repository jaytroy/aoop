package nl.rug.aoop.network;

import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.stockcommands.BuyLimitOrderCommand;
import nl.rug.aoop.stockcommands.SellLimitOrderCommand;
import nl.rug.aoop.ui.TraderUI;
import nl.rug.aoop.ui.StockUI;

public class OrderHandler implements MessageHandler {
    @Override
    public void handleMessage(String message) {
        String[] parts = message.split("\\|"); 

        if (parts.length < 4) {
            System.out.println("Invalid message format: " + message);
            return;
        }

        String command = parts[0];
        String traderName = parts[1];
        String stockSymbol = parts[2];
        double limitPrice = Double.parseDouble(parts[3]);
        int quantity = Integer.parseInt(parts[4]);

        if ("BUY".equalsIgnoreCase(command)) {
            TraderUI trader = findTraderByName(traderName);
            StockUI stock = findStockBySymbol(stockSymbol);

            if (trader != null && stock != null) {
                BuyLimitOrderCommand buy = new BuyLimitOrderCommand(trader, stock, limitPrice, quantity);
                buy.execute();
            } else {
                System.out.println("Trader or stock not found for the buy order.");
            }
        } else if ("SELL".equalsIgnoreCase(command)) {
            TraderUI trader = findTraderByName(traderName);
            StockUI stock = findStockBySymbol(stockSymbol);

            if (trader != null && stock != null) {
                SellLimitOrderCommand sell = new SellLimitOrderCommand(trader, stock, limitPrice, quantity);
                sell.execute();
            } else {
                System.out.println("Trader or stock not found for the sell order.");
            }
        } else {
            System.out.println("Unknown command: " + command);
        }
    }

    private TraderUI findTraderByName(String name) {
        for (TraderUI trader : ) {
            if (trader.getName().equals(name)) {
                return trader;
            }
        }
        return null;
    }

    private StockUI findStockBySymbol(String symbol) {
        for (StockUI stock : ) {
            if (stock.getSymbol().equals(symbol)) {
                return stock;
            }
        }
        return null;
    }

}
