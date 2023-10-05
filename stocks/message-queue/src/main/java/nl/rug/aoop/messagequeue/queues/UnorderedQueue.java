package nl.rug.aoop.messagequeue.queues;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Unordered queue class the implements messagequeue, does not take into account time of message.
 */

public class UnorderedQueue implements MessageQueue {
    private Queue<Message> queue = new LinkedList<>();

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
        if (!queue.isEmpty()) {
            return queue.poll();
        }
        throw new IllegalArgumentException("Queue has no elements");
    }

    @Override
    public int getSize() {
        return queue.size();
    }
}
