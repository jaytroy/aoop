package nl.rug.aoop.network;

/**
 * The ExchangeListener interface represents a listener that can be registered with the Exchange to receive updates
 * about the state of the exchange.
 */
public interface ExchangeListener {
    /**
     * This method is called when the state of the exchange has changed, and the listener should update its information.
     */
    void update();
}
