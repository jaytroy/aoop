package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestOrderedQueue {
    MessageQueue queue = null;

    @BeforeEach
    void setUp() {
        queue = new OrderedQueue();
    }

    @Test
    void testQueueConstructor() {
        assertNotNull(queue);
    }

    @Test
    void testQueueMethods() {
        Method[] methods = queue.getClass().getDeclaredMethods();

        boolean containsEnqueue = false;
        boolean containsDequeue = false;
        boolean containsGetSize = false;

        for (Method method : methods) {
            if (method.getName().equals("enqueue")) {
                containsEnqueue = true;
            } else if (method.getName().equals("dequeue")) {
                containsDequeue = true;
            } else if (method.getName().equals("getSize")) {
                containsGetSize = true;
            }
        }

        assertTrue(containsEnqueue);
        assertTrue(containsDequeue);
        assertTrue(containsGetSize);
    }

    @Test
    void testQueueEnqueue() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());
    }

    @Test
    void testGetSize() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());
    }

    @Test
    void testOrderedTimeStamps() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message2);
        queue.enqueue(message3);
        queue.enqueue(message1);

        Message dequeued1 = queue.dequeue();
        Message dequeued2 = queue.dequeue();
        Message dequeued3 = queue.dequeue();

        assertEquals(message1, dequeued1);
        assertEquals(message2, dequeued2);
        assertEquals(message3, dequeued3);

        assertEquals(0, queue.getSize());
    }

    @Test
    void testLargeAmountOfMessages() {
        int numberOfMessages = 10000;
        for (int i = 0; i < numberOfMessages; i++) {
            Message message = new Message("header", "body");
            queue.enqueue(message);
        }
        for (int i = 0; i < numberOfMessages; i++) {
            queue.dequeue();
        }
        assertEquals(0, queue.getSize());
    }

}
