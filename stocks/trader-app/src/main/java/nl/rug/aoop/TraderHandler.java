package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;
import java.util.Map;
import java.util.HashMap;

/**
 * The TraderHandler class is responsible for handling messages and producing messages for a
 * trader in the stock exchange system.
 */
@Slf4j
public class TraderHandler implements MessageHandler {
    private Trader trader;

    /**
     * Constructs a TraderHandler for a specific trader.
     *
     * @param trader The trader associated with this handler.
     */
    public TraderHandler(Trader trader) {
        this.trader = trader;
    }

    /**
     * Handle an incoming message, updating the trader's information based on the received message.
     *
     * @param message The incoming message to be handled.
     */
    @Override
    public void handleMessage(String message) {
        //log.info(message);

        Message msg = nl.rug.aoop.messagequeue.queues.Message.fromJson(message);
/*
        String header = msg.getHeader();

        //Command pattern instead? This is bad handling. No time though.
        if(header.equals("TRADER")) {
            handleTraderInfo(msg.getBody());
        } else {
            //handle different type of message
        }
*/
    }

    public void handleTraderInfo(String msg) {
        nl.rug.aoop.model.Trader modelTrader = nl.rug.aoop.model.Trader.fromJson(msg); //Is this the way this should be done?
        double funds = modelTrader.getFunds();
        String name = modelTrader.getName();
        Map<String, Integer> ownedStocks = modelTrader.getOwnedStocks();

        trader.updateInfo(funds,name,ownedStocks);
    }
}