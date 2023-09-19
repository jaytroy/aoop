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

    /*
    @Test
    void testMaxCharacter() {
        String longHeader = "This is more than two hundred and eighty characters This is more than two hundred and " +
                "eighty characters This is more than two hundred and eighty characters This is more than two hundred " +
                "and eighty characters This is more than two hundred and eighty characters This is more than two " +
                "hundred and eighty characters";
        String longBody = "This is more than two hundred and eighty characters This is more than two hundred and " +
                "eighty characters This is more than two hundred and eighty characters This is more than two hundred " +
                "and eighty characters This is more than two hundred and eighty characters This is more than two " +
                "hundred and eighty characters";

        String maxHeader = "This is the max amount of characters. This is the max amount of characters. This is the " +
                "max amount of characters. This is the max amount of ";
        String maxBody = "This is the max amount of characters. This is the max amount of characters. This is the " +
                "max amount of characters. This is the max amount of ";



        Message messageLong = new Message(longHeader, longBody);
        Message messageMax = new Message (maxHeader, maxBody);

        assertThrows(IOException.class, () -> producerUnord.maxCharacters(messageLong));
        assertThrows(IOException.class, () -> producerUnord.maxCharacters(messageMax));
    }

    @Test
    void testShortMessage() {
        String shortHeader = "This is a short header";
        String shortBody = "This is a short header";

        Message messageShort = new Message (shortHeader, shortBody);

        producerUnord.putMessage(messageShort);
        assertSame(queueUnord.dequeue(), messageShort);
    } */

    @Test
    void testPutMessageOrd() {
        producerOrd.putMessage(msg);
        assertSame(queueOrd.dequeue(), msg);
    }
}
