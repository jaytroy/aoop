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
        List<Method> methods = List.of(queue.getClass().getDeclaredMethods());

        assertEquals(methods.get(0).getName(), "enqueue"); //What if methods are switched around? Other option is a bunch of loops
        assertEquals(methods.get(1).getName(), "getSize"); //If switched around it seems to work. getDeclaredMethods() comes up with hashtable entries which sort themselves I guess?
        assertEquals(methods.get(2).getName(), "dequeue");
    }

    @Test
    void testEnqueueAndDequeueTimeStamps() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message1);
        queue.enqueue(message3); //No matter what order we dequeue the results should be the same
        queue.enqueue(message2);

        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());

        assertEquals(0, queue.getSize());
    }
    @Test
    void testEnqueueAndDequeueSameTimeStamps() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message1);
        queue.enqueue(message2);
        queue.enqueue(message1);
        queue.enqueue(message3);
        queue.enqueue(message2);
        queue.enqueue(message1);

        assertEquals(message1, queue.dequeue());
        assertEquals(message1, queue.dequeue());
        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message2, queue.dequeue());
        assertEquals(message3, queue.dequeue());

        assertEquals(0, queue.getSize());
    }

    @Test
    void testOverallEnqueue() {
        Message validMessage = new Message("header", "body");
        queue.enqueue(validMessage);
        assertEquals(1, queue.getSize());
    }

    @Test
    void testEnqueueNull() {
        Message nullMessage = null;
        queue.enqueue(nullMessage);
        assertEquals(0, queue.getSize());
    }

    @Test
    void testDequeueNull() {
        assertNull(queue.dequeue());
        assertEquals(0, queue.getSize());
    }

    @Test
    void testQueueDequeue() {
        Message validMessage = new Message("header", "body");
        queue.enqueue(validMessage);
        assertEquals(validMessage, queue.dequeue());
        assertEquals(0, queue.getSize());
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
    void testLargeAmountOfMessages() {
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
