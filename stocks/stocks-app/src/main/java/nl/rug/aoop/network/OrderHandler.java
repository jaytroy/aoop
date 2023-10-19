package nl.rug.aoop.network;

import nl.rug.aoop.networking.MessageHandler;

public class OrderHandler implements MessageHandler {

    @Override
    public void handleMessage(String message) {

        if ("BUY".equalsIgnoreCase(message)) {
            //executeBuy();
        } else if ("SELL".equalsIgnoreCase(message)) {
            //executeSell();
        } else {
            System.out.println("Unknown command: " + message);
        }
    }
}
