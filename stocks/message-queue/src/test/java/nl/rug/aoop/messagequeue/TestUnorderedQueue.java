package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestUnorderedQueue {

    MessageQueue queue = null;

    @BeforeEach
    void setUp() {
        queue = new UnorderedQueue();
    }

    @Test
    void testQueueConstructor() {
        assertNotNull(queue);
        assertEquals(0, queue.getSize());
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
    void testEnqueue() {
        Message nullMessage = null;
        queue.enqueue(nullMessage);
        assertEquals(0, queue.getSize());

        Message validMessage = new Message("header", "body");
        queue.enqueue(validMessage);
        assertEquals(1, queue.getSize());
    }

    @Test
    void testDequeue() {
        assertNull(queue.dequeue());
        assertEquals(0, queue.getSize());

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

        assertEquals(0, queue.getSize());

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(3, queue.getSize());

        Message message = queue.dequeue();
        assertEquals(2, queue.getSize());
        message = queue.dequeue();
        assertEquals(1, queue.getSize());
        message = queue.dequeue();
        assertEquals(0, queue.getSize());
    }



    @Test
    void testQueueOrdering() {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        queue.enqueue(message3);
        queue.enqueue(message1);
        queue.enqueue(message2);

        assertEquals(message3, queue.dequeue());
        assertEquals(message1, queue.dequeue());
        assertEquals(message2, queue.dequeue());
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
