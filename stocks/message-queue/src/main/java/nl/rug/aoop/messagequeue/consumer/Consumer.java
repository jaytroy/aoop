package nl.rug.aoop.messagequeue.consumer;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;

import lombok.Getter;

/**
 * ComConsumer class that imlements MQConsumer.
 */

public class Consumer implements MQConsumer {
    @Getter
    private MessageQueue queue;

    /**
     * Constructor for consumer.
     *
     * @param messageQueue the messagequeue.
     */
    public Consumer(MessageQueue messageQueue) {
        this.queue = messageQueue;
    }

    @Override
    public Message pollMessage() {
        return queue.dequeue();
    }
}
