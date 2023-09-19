package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
        msg = new Message("header","body");
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
    void testMaxCharacters() {
        MessageText m = new MessageText();

        Message messageLong = new Message(m.LONG_HEADER, m.LONG_BODY);
        assertThrows(IOException.class, () -> producerUnord.maxCharacters(messageLong));
    }

    @Test
    void testPutMessageIllegalHeader() {
        Message m = new Message("1234", "Valid");

        producerOrd.putMessage(m);
        assertNull(queueOrd.dequeue()); //If null, no message was ever enqueued
    }

    @Test
    void testPutMessageIllegalBody() {
        Message m = new Message("Valid","1234");

        producerOrd.putMessage(m);
        assertNull(queueOrd.dequeue()); //If null, no message was ever enqueued
    }
    @Test
    void testPutMessageOrd() {
        producerOrd.putMessage(msg);
        assertSame(queueOrd.dequeue(), msg);
    }
}
