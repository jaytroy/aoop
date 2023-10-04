package nl.rug.aoop.networking.queue;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * A thread-safe message queue.
 */
public class TSMessageQueue implements MessageQueue {
    //This type of queue ensures thread safety, while ordering messages based on their timestamp
    private PriorityBlockingQueue<Message> queue = new PriorityBlockingQueue<>();

    @Override
    public void enqueue(Message message) {
        if (message != null) {
            queue.offer(message); //Used offer so no exception is thrown. Queue space should be unlimited. What happens
            // when times for 2 messages are the same?
        } else {
            throw new IllegalArgumentException("Message cannot be null");
        }
    }

    @Override
    public Message dequeue() {
        if (!queue.isEmpty()) {
            return queue.poll(); //Used poll instead of take to return null if queue is empty. Given the if statement,
            // this should technically be no factor
        }
        throw new IllegalArgumentException("Queue has no elements");
    }

    @Override
    public int getSize() {
        return queue.size();
    }
}
