package nl.rug.aoop.messagequeue;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *  Ordered queue class that takes time of messages into account.
 */

public class OrderedQueue implements MessageQueue {
    private SortedMap<LocalDateTime, List<Message>> queue = new TreeMap<>();

    @Override
    public void enqueue(Message message) {
        if (message != null) {
            LocalDateTime timestamp = message.getTimestamp();
            if (queue.containsKey(timestamp)) {
                // Explination for jay: If there are messages with the same timestamp, add the new message to the list
                queue.get(timestamp).add(message);
            } else {
                // Explination for jay: If there are no messages with this timestamp, create a new list and add the
                // message
                List<Message> messages = new ArrayList<>();
                messages.add(message);
                queue.put(timestamp, messages);
            }
        }
    }

    @Override
    public Message dequeue() {
        if (!queue.isEmpty()) {
            LocalDateTime earliestTimestamp = queue.firstKey();
            List<Message> messages = queue.get(earliestTimestamp);
            Message message = messages.remove(0);

            if (messages.isEmpty()) {
                queue.remove(earliestTimestamp);
            }
            return message;
        }
        return null;
    }

    @Override
    public int getSize() {
        int size = 0;
        for (List<Message> messages : queue.values()) {
            size += messages.size();
        }
        return size;
    }
}
