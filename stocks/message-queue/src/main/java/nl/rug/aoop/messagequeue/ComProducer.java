package nl.rug.aoop.messagequeue;

/**
 * Class that implements MQProducer so that you can enqueue messages.
 */

public class ComProducer implements MQProducer {
    private MessageQueue messageQueue;

    /**
     * Constructor for ComProducer.
     *
     * @param messageQueue the messagequeue.
     */
    public ComProducer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void putMessage(Message message) {
        messageQueue.enqueue(message);
    }
}