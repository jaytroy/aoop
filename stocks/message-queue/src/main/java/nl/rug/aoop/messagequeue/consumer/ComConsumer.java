package nl.rug.aoop.messagequeue.consumer;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;

/**
 * ComConsumer class that imlements MQConsumer.
 */

public class ComConsumer implements MQConsumer {
    private MessageQueue messageQueue;

    /**
     * Constructor for comconsumer.
     *
     * @param messageQueue the messagequeue.
     */
    public ComConsumer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public Message pollMessage() {
        return messageQueue.dequeue();
    }
}
