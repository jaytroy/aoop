package nl.rug.aoop.messagequeue.queues;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * A thread-safe message queue.
 */
public class TSMessageQueue implements MessageQueue {
    private PriorityBlockingQueue<Message> queue;

    /**
     * Class constructor.
     */
    public TSMessageQueue() {
        queue = new PriorityBlockingQueue<>();
    }

    @Override
    public void enqueue(Message message) {
        if (message != null) {
            queue.offer(message);
        } else {
            throw new IllegalArgumentException("Message cannot be null");
        }
    }

    @Override
    public Message dequeue() {
        return queue.poll();
    }

    @Override
    public int getSize() {
        return queue.size();
    }
}
