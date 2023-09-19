package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestProducer {
    private Message msg;
    private Producer producerUnord;
    private Producer producerOrd;
    private MessageQueue queueUnord;
    private MessageQueue queueOrd;
    @BeforeEach
    void setup() {
        queueUnord = new UnorderedQueue();
        producerUnord = new Producer(queueUnord);
        queueOrd = new OrderedQueue();
        producerOrd = new Producer(queueOrd);
        msg = new Message("test","test");
    }

    @Test
    void testProducerConstructorUnord() {
        assertEquals(queueUnord, producerUnord.getMessageQueue());
    }

    @Test
    void testProducerConstructorOrd() {
        assertEquals(queueOrd, producerOrd.getMessageQueue());
    }

    @Test
    void testPutMessageUnord() {
        producerUnord.putMessage(msg);
        assertSame(queueUnord.dequeue(), msg);
    }

    @Test
    void testPutMessageOrd() {
        producerOrd.putMessage(msg);
        assertSame(queueOrd.dequeue(), msg);
    }
}
