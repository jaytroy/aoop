package nl.rug.aoop.networking.client;

/**
 * Defines an interface for classes which handle messages.
 */
public interface MessageHandler {
    /**
     * Defines a standard handle method.
     * @param message The message being handled.
     */
    void handleMessage(String message);
}
