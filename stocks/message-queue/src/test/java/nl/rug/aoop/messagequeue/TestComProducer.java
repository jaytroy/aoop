package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TestComProducer {
    private Message msg;
    private ComProducer producer1;
    private ComProducer producer2;
    private MessageQueue queue1;
    private MessageQueue queue2;
    @BeforeEach
    void setup() {
        queue1 = new UnorderedQueue();
        producer1 = new ComProducer(queue1);
        queue2 = new OrderedQueue();
        producer2 = new ComProducer(queue2);
        msg = new Message("test","test");
    }

    //Add a test to check if producer is not null?

    @Test
    void testProducerConstructor() {
        assertEquals(queue1, producer1.getMessageQueue());
        assertEquals(queue2, producer2.getMessageQueue());
    }

    //Defines a test which checks whether the header is made out of english characters
    //These could probably be turned into a test factory
    //If we have these we shouldn't need test methods
    @Test
    void testHeaderValid() {
        producer1.putMessage(msg);
        //Assert nothing? Test passed?
    }

    @Test
    void testHeaderInvalid() {
        Message msg = new Message("1234","valid");

        //assertThrows(IOException.class, () -> producer1.putMessage(msg));
    }

    @Test
    void testBodyValid() {

    }

    @Test
    void testBodyInvalid() {

    }

    @Test
    void messageIntoOrdered() {
        producer1.putMessage(msg);
        assertSame(queue1.dequeue(), msg);
    }

    @Test
    void messageIntoUnordered() {
        producer2.putMessage(msg);
        assertSame(queue2.dequeue(), msg);
    }
}
