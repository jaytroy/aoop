package nl.rug.aoop.network;

import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.stockcommands.BuyLimitOrderCommand;
import nl.rug.aoop.stockcommands.SellLimitOrderCommand;

public class OrderHandler implements MessageHandler {

    @Override
    public void handleMessage(String message) {

        if ("BUY".equalsIgnoreCase(message)) {
            BuyLimitOrderCommand buy = new BuyLimitOrderCommand();
            buy.execute();
        } else if ("SELL".equalsIgnoreCase(message)) {
            SellLimitOrderCommand sell = new SellLimitOrderCommand();
            sell.execute();
        } else {
            System.out.println("Unknown command: " + message);
        }
    }
}