package nl.rug.aoop.messagequeue.testconsumer;

import nl.rug.aoop.messagequeue.consumer.Consumer;
import nl.rug.aoop.messagequeue.producer.Producer;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.OrderedQueue;
import nl.rug.aoop.messagequeue.queues.UnorderedQueue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestConsumer {
    private Message msg;
    private Consumer consumerUnord;
    private Consumer consumerOrd;
    private MessageQueue queueUnord;
    private MessageQueue queueOrd;

    @BeforeEach
    void setup() {
        msg = new Message("Test", "Message");
        queueUnord = new UnorderedQueue();
        consumerUnord = new Consumer(queueUnord);
        queueOrd = new OrderedQueue();
        consumerOrd = new Consumer(queueOrd);
    }
    @Test
    public void testConsumerConstructorUnord() {
        assertEquals(consumerUnord.getQueue(), queueUnord);
    }

    @Test
    public void testConsumerConstructorOrd() {
        assertEquals(consumerOrd.getQueue(), queueOrd);
    }

    @Test
    public void testPollMessageUnord() {
        Producer p = new Producer(queueUnord);

        p.putMessage(msg);
        assertEquals(consumerUnord.pollMessage(), msg);
    }

    @Test
    public void testPollMessageOrd() {
        Producer p = new Producer(queueOrd);

        p.putMessage(msg);
        assertEquals(consumerOrd.pollMessage(), msg);
    }
}
