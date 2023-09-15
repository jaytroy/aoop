package nl.rug.aoop.messagequeue;

public class ComConsumer implements MQConsumer {
    private MessageQueue messageQueue;
    public ComConsumer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public Message pollMessage() {
        return messageQueue.dequeue();
    }
}
