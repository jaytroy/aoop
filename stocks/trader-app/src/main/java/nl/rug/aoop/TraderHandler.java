package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.actions.Order;
import nl.rug.aoop.messagequeue.producer.MQProducer;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.NetworkMessage;
import nl.rug.aoop.networking.client.Client;

import java.util.Map;
import java.util.HashMap;

/**
 * The TraderHandler class is responsible for handling messages and producing messages for a
 * trader in the stock exchange system.
 */
@Slf4j
public class TraderHandler implements MessageHandler {
    Trader trader;

    /**
     * Constructs a TraderHandler.
     */
    public TraderHandler(Trader trader) {
        this.trader = trader;
    }

    /**
     * Handle an incoming message.
     *
     * @param message The incoming message to be handled.
     */
    @Override
    public void handleMessage(String message) {
        Message msg = Message.fromJson(message);
        if(msg.getHeader().equals("TRADER")) {
            nl.rug.aoop.model.Trader traderinfo = nl.rug.aoop.model.Trader.fromJson(msg.getBody());
            trader.setName(traderinfo.getName());
            log.info(trader.getName());
            trader.setOwnedStocks(traderinfo.getOwnedStocks());
            trader.setAvailableFunds(traderinfo.getFunds());
            log.info(Double.toString(trader.getAvailableFunds()));
        }

        /*String[] parts = message.split("~");
        Map<String, Integer> ownedStocks = new HashMap<>();

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("Name:")) {
                trader.setName(parts[++i]);
                log.info(parts[++i]);
            } else if (parts[i].equals("Funds:")) {
                trader.setAvailableFunds(Double.parseDouble(parts[++i]));
            } else if (parts[i].equals("Stocks:")) {
                // Parse the owned stocks
                i++; // Move to the first stock
                while (i < parts.length) {
                    String[] stockInfo = parts[i].split(":");
                    if (stockInfo.length == 2) {
                        String stockSymbol = stockInfo[0];
                        int quantity = Integer.parseInt(stockInfo[1]);
                        ownedStocks.put(stockSymbol, quantity);
                    } else {
                        // This part is not in "SYMBOL:QUANTITY" format, so break the loop
                        break;
                    }
                    i++;
                }
            }*/
        }

        //trader.setOwnedStocks(ownedStocks);
}