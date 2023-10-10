package nl.rug.aoop.messagequeue.testserverside;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.serverside.commands.CommandMessageHandler;
import java.util.HashMap;
import java.util.Map;

public class TestCommandMessageHandler {

    private CommandMessageHandler commandMessageHandler;
    private CommandHandler mockCommandHandler;

    @BeforeEach
    public void setUp() {
        mockCommandHandler = Mockito.mock(CommandHandler.class);
        commandMessageHandler = new CommandMessageHandler(mockCommandHandler);
    }

    @Test
    public void testHandleMessage() {
        Message message = new Message("testHeader", "testBody");
        String messageJson = message.toJson();

        Map<String, Object> expectedParams = new HashMap<>();
        expectedParams.put("messageJson", messageJson);
        expectedParams.put("header", "testHeader");
        expectedParams.put("body", "testBody");
        expectedParams.put("timestamp", message.getTimestamp());

        commandMessageHandler.handleMessage(messageJson);

        Mockito.verify(mockCommandHandler).executeCommand("testHeader", expectedParams);
    }
}