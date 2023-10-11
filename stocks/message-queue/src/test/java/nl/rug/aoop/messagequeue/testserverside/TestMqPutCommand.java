package nl.rug.aoop.messagequeue.testserverside;

import nl.rug.aoop.messagequeue.serverside.commands.MqPutCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.argThat;

public class TestMqPutCommand {

    private MqPutCommand mqPutCommand;
    private MessageQueue mockMessageQueue;

    @BeforeEach
    public void setUp() {
        mockMessageQueue = Mockito.mock(MessageQueue.class);
        mqPutCommand = new MqPutCommand(mockMessageQueue);
    }

    @Test
    public void testExecute() {
        Message message = new Message("testHeader", "testBody");
        String messageJson = message.toJson();

        Map<String, Object> params = new HashMap<>();
        params.put("messageJson", messageJson);

        mqPutCommand.execute(params);

        Mockito.verify(mockMessageQueue).enqueue(argThat(m ->
                m.getHeader().equals(message.getHeader()) &&
                        m.getBody().equals(message.getBody()) &&
                        m.getTimestamp().equals(message.getTimestamp())
        ));

        assertNotNull(message.getTimestamp());

        // Check if the JSON parsing works correctly
        Message parsedMessage = Message.fromJson(messageJson);
        assertEquals(message.getHeader(), parsedMessage.getHeader());
        assertEquals(message.getBody(), parsedMessage.getBody());
    }


}
