package nl.rug.aoop;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;


/**
 * The TraderHandler class is responsible for handling messages and producing messages for a
 * trader in the stock exchange system.
 */
@Slf4j
public class TraderHandler implements MessageHandler {
    private TraderFacade traderFacade;

    /**
     * Constructs a TraderHandler for a specific trader.
     *
     * @param traderFacade The trader associated with this handler.
     */
    public TraderHandler(TraderFacade traderFacade) {
        this.traderFacade = traderFacade;
    }

    /**
     * Handle an incoming message, updating the trader's information based on the received message.
     *
     * @param message The incoming message to be handled.
     */
    @Override
    public void handleMessage(String message) {
        try {
            Message msg = nl.rug.aoop.messagequeue.queues.Message.fromJson(message);
            String header = msg.getHeader();

            if (header.equals("TRADER")) {
                handleTraderInfo(msg.getBody());
            } else if (header.equals("STOCK")) {
                handleStockInfo(msg.getBody());
            }
        } catch(JsonSyntaxException e) {
            log.info(message);
        }
    }

    /**
     * Handles the incoming trader info.
     *
     * @param msg the message that arrives.
     */
    public void handleTraderInfo(String msg) {
        nl.rug.aoop.model.components.Trader modelTrader = nl.rug.aoop.model.components.Trader.fromJson(msg);
        double funds = modelTrader.getFunds();
        String name = modelTrader.getName();
        Map<String, Integer> ownedStocks = modelTrader.getOwnedStocks();

        traderFacade.getTrader().updateInfo(funds,name,ownedStocks);
    }

    /**
     * Handles the incoming stock info.
     *
     * @param msg the message that arrives.
     */
    public void handleStockInfo(String msg) {
        Gson gson = new Gson();
        Type stockListType = new TypeToken<List<Stock>>() {}.getType();

        List<Stock> stocks = gson.fromJson(msg, stockListType); //here this list has all the stock info

        if (stocks != null) {
            traderFacade.getTrader().updateStocks(stocks);
        } else {
            log.warn("Failed to parse stock information.");
        }
    }
}