package nl.rug.aoop;

import nl.rug.aoop.messagequeue.producer.MQProducer;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;

public class TraderHandler implements MessageHandler, MQProducer {
    @Override
    public void handleMessage(String message) {

    }

    @Override
    public void putMessage(Message message) {

    }
}
