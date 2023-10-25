package nl.rug.aoop.network;

import nl.rug.aoop.messagequeue.consumer.Consumer;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.networking.MessageHandler;

/**
 * A consumer which continuously polls the TSMessageQueue in the exchange.
 */
public class NetConsumer extends Consumer implements Runnable, MessageHandler { //Should this implement messagehandler?
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
            try {
                Message msg = queue.dequeue();
                //turn message into a string
                //handleMessage(message)

            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage() + " in netproducer");
            }
        }
    }

    @Override
    public void handleMessage(String message) { //Or should it be passed onto a specific handler?
        //Logic here
    }
}
