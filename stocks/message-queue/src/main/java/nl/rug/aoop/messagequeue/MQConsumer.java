package nl.rug.aoop.messagequeue;

public interface MQConsumer {
    Message pollMessage();
}