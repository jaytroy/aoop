package nl.rug.aoop.messagequeue;

public interface MQProducer {
    void putMessage(Message message);
}
