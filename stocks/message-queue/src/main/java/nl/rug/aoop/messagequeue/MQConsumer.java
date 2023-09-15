package nl.rug.aoop.messagequeue;

/**
 * Interface to pull the message from queue.
 *
 */
public interface MQConsumer {
    /**
     * Method to pull messages from queue.
     *
     * @return the pulled message from queue.
     */
    Message pollMessage();
}