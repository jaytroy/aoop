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
        // Ideally, we would use JSON paired with our message class here. We tried to, but we ran out of time.
        String[] parts = message.split("  ");
        Map<String, Integer> ownedStocks = new HashMap<>();
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals("Name:")) {
                trader.setName(parts[++i]);
                log.info(parts[++i]);
            } else if (parts[i].equals("Funds:")) {
                trader.setAvailableFunds(Double.parseDouble(parts[++i]));
            } else if (parts[i].equals("Stocks:")) {
                i++;
                while (i < parts.length) {
                    String[] stockInfo = parts[i].split(":");
                    if (stockInfo.length == 2) {
                        String stockSymbol = stockInfo[0];
                        int quantity = Integer.parseInt(stockInfo[1]);
                        ownedStocks.put(stockSymbol, quantity);
                    } else {
                        break;
                    }
                    i++;
                }
            }
        }
        trader.setOwnedStocks(ownedStocks);
    }
}