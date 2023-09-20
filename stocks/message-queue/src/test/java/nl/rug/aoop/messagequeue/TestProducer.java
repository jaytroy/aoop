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
    public void setup() {
        queueUnord = new UnorderedQueue();
        producerUnord = new Producer(queueUnord);
        queueOrd = new OrderedQueue();
        producerOrd = new Producer(queueOrd);
        msg = new Message("header","body");
    }

    @Test
    public void testProducerConstructorUnord() {
        assertEquals(queueUnord, producerUnord.getMessageQueue());
    }

    @Test
    public void testProducerConstructorOrd() {
        assertEquals(queueOrd, producerOrd.getMessageQueue());
    }

    @Test
    public void testPutMessageUnord() {
        producerUnord.putMessage(msg);
        assertSame(queueUnord.dequeue(), msg);
    }

    @Test
    public void testPutMessageOrd() {
        producerOrd.putMessage(msg);
        assertSame(queueOrd.dequeue(), msg);
    }

    @Test
    public void testMaxCharacters() {
        MessageText m = new MessageText();

        Message messageLong = new Message(m.LONG_HEADER, m.LONG_BODY);
        assertThrows(IOException.class, () -> producerUnord.maxCharacters(messageLong));
    }

    @Test
    public void testPutMessageIllegalHeader() {
        Message m = new Message("1234", "Valid");

        producerOrd.putMessage(m);
        assertNull(queueOrd.dequeue()); //If null, no message was ever enqueued
    }

    @Test
    public void testPutMessageIllegalBody() {
        Message m = new Message("Valid","1234");

        producerOrd.putMessage(m);
        assertNull(queueOrd.dequeue()); //If null, no message was ever enqueued
    }

    @Test
    public void testNonEmptyHeader() {
        Message m = new Message("","Valid");

        producerOrd.putMessage(m);
        assertNull(queueOrd.dequeue()); //If null, no message was ever enqueued
    }
    @Test
    public void testNonEmptyBody() {
        Message m = new Message("Valid","");

        producerOrd.putMessage(m);
        assertNull(queueOrd.dequeue()); //If null, no message was ever enqueued
    }
}
