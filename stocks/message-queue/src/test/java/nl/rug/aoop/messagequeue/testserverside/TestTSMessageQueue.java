package nl.rug.aoop.messagequeue.testserverside;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.serverside.TSMessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TestTSMessageQueue {
    private TSMessageQueue queue;
    private Message msg;
    @BeforeEach
    public void setQueue() {
        queue = new TSMessageQueue();
        msg = new Message("Header","Body");
    }

    @Test
    public void testEnqueue() { //Also tests dequeue
        queue.enqueue(msg);
        assertEquals(queue.dequeue(), msg);
    }

    @Test
    public void testGetSize() {
        queue.enqueue(msg);
        assertEquals(queue.getSize(),1);
    }

    @Test
    public void testNullMessage() {
        Message badmsg = null;
        assertThrows(IllegalArgumentException.class, () -> queue.enqueue(badmsg));
    }


    @Test
    public void testCorrectlySorted() {
        Message M1 = new Message("M1","M1");
        Message M2 = new Message("M2","M2");

        queue.enqueue(M1);
        queue.enqueue(M2);
        queue.enqueue(M1);

        assertEquals(queue.dequeue(), M1);
        assertEquals(queue.dequeue(), M1);
        assertEquals(queue.dequeue(), M2);
    }
}