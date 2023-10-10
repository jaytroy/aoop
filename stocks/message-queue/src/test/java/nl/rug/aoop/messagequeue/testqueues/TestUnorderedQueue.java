package nl.rug.aoop.messagequeue.testqueues;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.UnorderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestUnorderedQueue {

    MessageQueue queue = null;

    @BeforeEach
    public void setUp() {
        queue = new UnorderedQueue();
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
    public void testDequeueNull() {
        assertNull(queue.dequeue());
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
    public void testGetSize() {
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
    public void testQueueOrdering() {
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
    public void testLargeAmountOfMessages() {
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
