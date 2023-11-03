package nl.rug.aoop.messagequeue.testqueues;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.TSMessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TestTSMessageQueue {
    private TSMessageQueue queue;
    private Message msg;
    @BeforeEach
    public void setQueue() {
        queue = new TSMessageQueue();
        msg = new Message("Header","Body");
    }

    @Test
    public void testEnqueue() {
        queue.enqueue(msg);
        assertEquals(msg, queue.dequeue());
    }

    @Test
    public void testDequeue() {
        queue.enqueue(msg);
        assertEquals(1,queue.getSize());
    }

    @Test
    public void testEnqueueMultiple() {
        Message msg1 = new Message("1","1");
        Message msg2 = new Message("2","2");
        Message msg3 = new Message("3","3");

        queue.enqueue(msg1);
        queue.enqueue(msg2);
        queue.enqueue(msg3);

        assertEquals(3,queue.getSize());
    }

    @Test
    public void testDequeueMultiple() {
        Message msg1 = new Message("1","1");
        Message msg2 = new Message("2","2");
        Message msg3 = new Message("3","3");

        queue.enqueue(msg1);
        queue.enqueue(msg2);
        queue.enqueue(msg3);

        queue.dequeue();
        queue.dequeue();
        queue.dequeue();

        assertEquals(0,queue.getSize());
    }

    @Test
    public void testDequeueEmpty() {
        assertNull(queue.dequeue());
    }

    @Test
    public void testGetSize() {
        queue.enqueue(msg);
        queue.dequeue();
        assertEquals(0,queue.getSize());
    }

    @Test
    public void testNullMessage() {
        assertThrows(IllegalArgumentException.class, () -> queue.enqueue(null));
    }


    @Test
    public void testCorrectlySorted() throws IllegalAccessException, NoSuchFieldException {
        Message message1 = new Message("header", "body");
        Message message2 = new Message("header", "body");
        Message message3 = new Message("header", "body");

        //Who cares about encapsulation 3
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
}