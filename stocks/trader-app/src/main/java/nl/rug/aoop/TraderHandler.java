package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
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
        log.info(message);
        if(true) {
            
        } else {
            //handle different type of message
        }
        // Ideally, we would use JSON paired with our message class here. We tried to, but we ran out of time.

    }
}