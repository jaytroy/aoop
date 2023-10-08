package nl.rug.aoop.command;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestCommandHandler {
    private CommandHandler commandHandler;

    @BeforeEach
    public void setUp() {
        commandHandler = new CommandHandler();
    }

    @Test
    public void testRegisterCommand() {
        Command mockCommand = mock(Command.class);
        commandHandler.registerCommand("testCommand", mockCommand);

        assertTrue(commandHandler.getCommands().containsKey("testCommand"));
        assertEquals(mockCommand, commandHandler.getCommands().get("testCommand"));
    }

    @Test
    public void testExecuteCommand() {
        Command mockCommand = mock(Command.class);
        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");

        commandHandler.registerCommand("testCommand", mockCommand);
        commandHandler.executeCommand("testCommand", params);

        verify(mockCommand, times(1)).execute(params);
    }

    @Test
    public void testExecuteCommandWithUnknownCommand() {
        Command mockCommand = mock(Command.class);
        Map<String, Object> params = new HashMap<>();
        params.put("param1", "value1");

        commandHandler.registerCommand("testCommand", mockCommand);
        commandHandler.executeCommand("unknownCommand", params);

        verify(mockCommand, never()).execute(any());
    }
}
