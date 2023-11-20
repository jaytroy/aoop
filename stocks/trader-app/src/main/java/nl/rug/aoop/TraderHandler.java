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
        //System.out.println(message);
        try {
            Message msg = nl.rug.aoop.messagequeue.queues.Message.fromJson(message);
            String header = msg.getHeader();

            //Command pattern instead? This is bad handling. No time though.
            if (header.equals("TRADER")) {
                handleTraderInfo(msg.getBody());
            } else if (header.equals("STOCK")) {
                handleStockInfo(msg.getBody());
            }
        } catch(JsonSyntaxException e) {
            //A message was not sent from the exchange. This is going to be coming from the server.
            log.info(message);
        }
    }

    public void handleTraderInfo(String msg) {
        nl.rug.aoop.model.Trader modelTrader = nl.rug.aoop.model.Trader.fromJson(msg); //Is this the way this should be done?
        double funds = modelTrader.getFunds();
        String name = modelTrader.getName();
        Map<String, Integer> ownedStocks = modelTrader.getOwnedStocks();

        trader.updateInfo(funds,name,ownedStocks);
    }

    public void handleStockInfo(String msg) {
        Gson gson = new Gson();
        Type stockListType = new TypeToken<List<Stock>>() {}.getType();

        List<Stock> stocks = gson.fromJson(msg, stockListType); //here this list has all the stock info

        if (stocks != null) {
            trader.updateStocks(stocks);
        } else {
            log.warn("Failed to parse stock information.");
        }
    }
}