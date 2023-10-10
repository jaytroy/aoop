package nl.rug.aoop.messagequeue.testserverside;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.serverside.TSMessageQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestTSMessageQueue {
    private TSMessageQueue queue;
    @BeforeEach
    public void setQueue() {
        queue = new TSMessageQueue();
    }

    @Test
    public void testCorrectlySorted() { //Naming follows: Message 1 Time 1 -> M1T1
        LocalDateTime T1 = LocalDateTime.now();

        Message M1T1 = new Message("m1t1","m1t1");
        Message M2T2 = new Message("m1t1","m1t1"); //Should create a timestamp later, though could fail if compute is fast enough
        Message M3T1 = new Message("m1t1","m1t1");

        M1T1.setTimestamp(T1);
        M3T1.setTimestamp(T1);

        queue.enqueue(M1T1);
        queue.enqueue(M2T2);
        queue.enqueue(M3T1);

        assertEquals(queue.dequeue(), M1T1);
        assertEquals(queue.dequeue(), M3T1);
        assertEquals(queue.dequeue(), M2T2);
    }

}
