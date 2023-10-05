package nl.rug.aoop.networking.server;

import nl.rug.aoop.messagequeue.queues.Message;

/**
 * Defines an interface for classes which handle messages.
 */
public interface MessageHandler {
    /**
     * Defines a standard handle method.
     * @param message The message being handled.
     */
    void handleMessage(Message message);
}
