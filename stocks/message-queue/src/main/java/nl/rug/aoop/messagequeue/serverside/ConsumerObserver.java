package nl.rug.aoop.messagequeue.serverside;

import nl.rug.aoop.messagequeue.queues.Message;

/**
 * Observer interface for any sort of consumer.
 */
public interface ConsumerObserver {
    /**
     * Update the observer with a message.
     * @param msg The message.
     */
    void update(Message msg);
}
