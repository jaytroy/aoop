package nl.rug.aoop.network;

import java.util.Map;

/**
 * The ExchangeListener interface represents a listener that can be registered with the Exchange to receive updates
 * about the state of the exchange.
 */
public interface ExchangeListener {
    /**
     * This method is called when the state of the exchange has changed, and the listener should update its information.
     *
     * @param ownedStocks A map containing the stocks owned by the client, where the keys are stock symbols and values
     *                   are the quantities owned.
     * @param availableFunds The amount of available funds for trading.
     */
    void update(Map<String, Integer> ownedStocks, int availableFunds);
}
