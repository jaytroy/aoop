package nl.rug.aoop.messagequeue.producer;

import nl.rug.aoop.messagequeue.queues.Message;

/**
 * MQProducer interface.
 */

public interface MQProducer {

    /**
     * PutMessage puts a message into the queue.
     *
     * @param message the message.
     */
    void putMessage(Message message);
}
