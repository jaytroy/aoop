package nl.rug.aoop.messagequeue;
import java.time.LocalDateTime;
import java.util.SortedMap;
import java.util.TreeMap;

public class OrderedQueue implements MessageQueue {
    private SortedMap<LocalDateTime, Message> queue = new TreeMap();
    @Override
    public void enqueue(Message message) {
        if (message != null) {
            queue.put(message.getTimestamp(), message); //What happens when times for 2 messages are the same?
        }
        throw new IllegalArgumentException("Message cannot be null");
    }

    @Override
    public Message dequeue() {
        if (!queue.isEmpty()) {
            LocalDateTime earliestTimestamp = queue.firstKey(); //What happens when times for 2 messages are the same?
            Message message = queue.get(earliestTimestamp);
            queue.remove(earliestTimestamp);
            return message;
        }
        throw new IllegalArgumentException("Queue has no elements");
    }

    @Override
    public int getSize() {
        return queue.size();
    }
}
