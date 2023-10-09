package nl.rug.aoop.messagequeue.serverside.commands;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * The command message handler class.
 *
 */

public class CommandMessageHandler implements MessageHandler {
    private final CommandHandler commandHandler;

    /**
     * Constructor.
     *
     * @param commandHandler the commandHandler you pass in.
     */

    public CommandMessageHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void handleMessage(String messageJson) {
        // convert the json message to a Map
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
