package nl.rug.aoop.messagequeue;

public class ComProducer implements MQProducer {
    private MessageQueue messageQueue;

    public ComProducer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void putMessage(Message message) {
        messageQueue.enqueue(message);
    }
}