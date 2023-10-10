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
    public void testCorrectlySorted() { //Naming follows: Message 1 Time 1 -> M1T1
        Message M1T1 = new Message("123","123");
        Message M2T2 = new Message("123","123");
        Message M3T3 = new Message("123","123");

        queue.enqueue(M1T1);
        queue.enqueue(M2T2);
        queue.enqueue(M3T3);

        assertEquals(queue.dequeue(), M1T1);
        assertEquals(queue.dequeue(), M2T2);
        assertEquals(queue.dequeue(), M3T3);
    }
}
