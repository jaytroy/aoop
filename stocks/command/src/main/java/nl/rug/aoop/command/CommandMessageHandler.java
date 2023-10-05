package nl.rug.aoop.command;

import java.util.HashMap;
import java.util.Map;

public class CommandMessageHandler implements MessageHandler {
    private final CommandHandler commandHandler;

    public CommandMessageHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void handleMessage(String messageJson) {
        // cnvert the jso message to a Map
        Map<String, Object> params = new HashMap<>();
        params.put("messageJson", messageJson);

        // extract the header from the message.
        Message message = Message.fromJson(messageJson);
        params.put("header", message.getHeader());

        params.put("body", message.getBody());
        params.put("timestamp", message.getTimestamp());

        // Execute the command based on the header
        commandHandler.executeCommand(message.getHeader(), params);
    }
}
