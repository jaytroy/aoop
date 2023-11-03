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
public class NetConsumer extends Consumer implements Runnable {
    private MessageQueue queue;
    private ConsumerObserver observer;

    /**
     * Constructor for consumer.
     *
     * @param messageQueue the messagequeue.
     * @param observer the exchange related to the consumer.
     */

    public NetConsumer(MessageQueue messageQueue, ConsumerObserver observer) {
        super(messageQueue);
        queue = super.getQueue();
        this.observer = observer;
    }

    @Override
    public void run() {
        System.out.println("Threaded consumer is running");
        while(!Thread.currentThread().isInterrupted()) {
            if (queue.getSize() != 0) {
                Message msg = queue.dequeue();
                log.info("Polled from the queue: " + msg);

                log.info("Updating " + observer + " with " + msg);
                observer.update(msg); //Should send the order to the exchange
            }
        }
    }
}

