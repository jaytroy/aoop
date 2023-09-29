package nl.rug.aoop.messagequeue;

import java.io.IOException;

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

    /**
     * maxCharacters checks if the message exceeds max characters.
     *
     * @param message the message.
     */

    void maxCharacters(Message message) throws IOException;
}
