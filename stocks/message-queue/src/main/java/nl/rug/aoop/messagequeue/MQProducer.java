package nl.rug.aoop.messagequeue;

/**
 * MQProducer interface.
 */

public interface MQProducer {

    /**
     * PutMessage puts a message into the queue.
     *
     * @param message the message of the queue.
     */
    void putMessage(Message message);
}
