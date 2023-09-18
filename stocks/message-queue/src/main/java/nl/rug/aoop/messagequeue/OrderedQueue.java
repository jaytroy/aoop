package nl.rug.aoop.messagequeue;

import java.time.LocalDateTime;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *  Ordered queue class that takes time of messages into account.
 */

public class OrderedQueue implements MessageQueue {
    private SortedMap<LocalDateTime, Message> queue = new TreeMap();

    @Override
    public void enqueue(Message message) {
        if (message != null) {
            queue.put(message.getTimestamp(), message); //What happens when times for 2 messages are the same?
        }
    }

    @Override
    public Message dequeue() {
        if (!queue.isEmpty()) {
            LocalDateTime earliestTimestamp = queue.firstKey(); //What happens when times for 2 messages are the same?
            Message message = queue.get(earliestTimestamp);
            queue.remove(earliestTimestamp);
            return message;
        }
        return null;
    }

    @Override
    public int getSize() {
        return queue.size();
    }
}
