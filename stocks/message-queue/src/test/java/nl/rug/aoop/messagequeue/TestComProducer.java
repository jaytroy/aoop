package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestComProducer {
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
        Message msg = new Message("test","test");

        producer1.putMessage(msg);
        //Assert nothing? Test passed?
    }

    @Test
    void testHeaderInvalid() {
        Message msg = new Message("1234","1234");
        IOException exception = assertThrows(IOException.class, () -> producer1.putMessage(msg));
        assertEquals("Header contains illegal characters", exception.getMessage());
    }

    @Test
    void testBodyValid() {

    }

    @Test
    void testBodyInvalid() {

    }

    //Next two are integration tests? Are these necessary?
    @Test
    void messageIntoOrdered() {

    }

    void messageIntoUnordered() {

    }
}
