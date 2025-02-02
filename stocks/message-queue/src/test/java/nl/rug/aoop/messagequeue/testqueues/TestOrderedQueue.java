package nl.rug.aoop.messagequeue.testqueues;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.OrderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.time.LocalDateTime;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrderedQueue {
    MessageQueue queue = null;

    @BeforeEach
    public void setUp() {
        queue = new OrderedQueue();
    }

    @Test
    public void testQueueConstructor() {
        assertNotNull(queue);
        assertEquals(0, queue.getSize());
    }

    @Test
    public void testQueueMethods() {
        List<Method> methods = List.of(queue.getClass().getDeclaredMethods());

        assertEquals(methods.get(0).getName(), "enqueue");
        assertEquals(methods.get(1).getName(), "getSize");
        assertEquals(methods.get(2).getName(), "dequeue");
    }

    @Test
    public void testEnqueueAndDequeueDifferentTimeStamps() throws NoSuchFieldException, IllegalAccessException {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        //Who cares about encapsulation
        Field timestampField = Message.class.getDeclaredField("timestamp");
        timestampField.setAccessible(true);

        LocalDateTime message1Timestamp = (LocalDateTime) timestampField.get(message1);
        LocalDateTime oneHourLater = message1Timestamp.plusHours(1);
        timestampField.set(message2, oneHourLater);
        timestampField.set(message3, timestampField.get(message1));

        queue.enqueue(message1);
        queue.enqueue(message2);
        queue.enqueue(message3);

        assertEquals(message1, queue.dequeue());
        assertEquals(message3, queue.dequeue());
        assertEquals(message2, queue.dequeue());

        assertEquals(0, queue.getSize());
    }

    @Test
    public void testEnqueueAndDequeueSameTimeStamps() throws NoSuchFieldException, IllegalAccessException {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        //Who cares about encapsulation 2
        Field timestampField = Message.class.getDeclaredField("timestamp");
        timestampField.setAccessible(true);

        timestampField.set(message2, timestampField.get(message1));
        timestampField.set(message3, timestampField.get(message1));

        queue.enqueue(message2);
        queue.enqueue(message3);
        queue.enqueue(message1);

        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
        assertEquals(message1, queue.dequeue());

        assertEquals(0, queue.getSize());
    }

    @Test
    public void testEnqueue() {
        Message validMessage = new Message("header", "body");
        queue.enqueue(validMessage);
        assertEquals(1, queue.getSize());
    }

    @Test
    public void testEnqueueNull() {
        Message nullMessage = null;
        queue.enqueue(nullMessage);
        assertEquals(0, queue.getSize());
    }

    @Test
    public void testDequeue() {
        Message validMessage = new Message("header", "body");
        queue.enqueue(validMessage);
        assertEquals(validMessage, queue.dequeue());
        assertEquals(0, queue.getSize());
    }

    @Test
    public void testDequeueNull() {
        assertNull(queue.dequeue());
        assertEquals(0, queue.getSize());
    }

    @Test
    public void testGetSize() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());
    }

    @Test
    public void testLargeAmountOfMessages() {
        int numberOfMessages = 10000;
        for (int i = 0; i < numberOfMessages; i++) {
            Message message = new Message("header", "body");
            queue.enqueue(message);
        }
        assertEquals(10000, queue.getSize());

        for (int i = 0; i < numberOfMessages; i++) {
            queue.dequeue();
        }
        assertEquals(0, queue.getSize());
    }
}
