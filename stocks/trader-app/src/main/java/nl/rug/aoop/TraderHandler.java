package nl.rug.aoop;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.networking.MessageHandler;
import java.lang.reflect.Type;

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
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Trader.class, new TraderTypeAdapter(trader.getId()))
                    .create();

            Type traderType = new TypeToken<Trader>() {}.getType();

            Trader trader = gson.fromJson(message, traderType);

            if (trader != null) {
                this.trader.setName(trader.getName());
                this.trader.setAvailableFunds(trader.getFunds());
                this.trader.setOwnedStocks(trader.getOwnedStocks());

                this.trader.traderStrategy();

                log.info("Trader information updated: " + trader.getName());
            } else {
                log.error("Failed to parse trader information from JSON message: " + message);
            }
        } catch (JsonSyntaxException e) {
            log.error("Error parsing JSON message: " + trader.getName());
        }
    }

}