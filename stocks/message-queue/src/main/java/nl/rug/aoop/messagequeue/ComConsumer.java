package nl.rug.aoop.messagequeue;

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
