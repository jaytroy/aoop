package nl.rug.aoop.messagequeue.queues;

/**
 * Message queue interface.
 */
public interface MessageQueue {
    /**
     * Enqueue takes in a message and puts it onto the queue.
     *
     * @param message takes message as a parameter to put it onto the queue.
     */
    void enqueue(Message message);

    /**
     * Message get dequeued.
     *
     * @return the messsage you dequeued.
     */
    Message dequeue();

    /**
     * The size of the queue.
     *
     * @return the size of the queue.
     */

    int getSize();
}
