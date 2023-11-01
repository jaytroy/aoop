package nl.rug.aoop.messagequeue.serverside;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.consumer.Consumer;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.networking.MessageHandler;

/**
 * A consumer which continuously polls the TSMessageQueue in the exchange.
 */
@Slf4j
public class NetConsumer extends Consumer implements Runnable, MessageHandler {
    private MessageQueue queue;

    /**
     * Constructor for consumer.
     *
     * @param messageQueue the messagequeue.
     */

    public NetConsumer(MessageQueue messageQueue) {
        super(messageQueue);
        queue = super.getQueue();
    }

    @Override
    public void run() {
        System.out.println("Threaded consumer is running");
        while(!Thread.currentThread().isInterrupted()) {
            if (queue.getSize() != 0) {
                Message msg = queue.dequeue();
                log.info("Polled from the queue: " + msg);
                //turn message into a string
                //handleMessage(message)
            }
        }
    }

    @Override
    public void handleMessage(String message) { //Or should it be passed onto a specific handler?
        //Logic here. Do what needs to be done depending on the type of order
    }
}
