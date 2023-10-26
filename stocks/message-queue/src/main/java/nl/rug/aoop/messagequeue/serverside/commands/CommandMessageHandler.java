package nl.rug.aoop.messagequeue.serverside.commands;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;

import java.util.HashMap;
import java.util.Map;
   
/**
 * Handles messages in the form of commands.
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
        System.out.println("CommandMessageHandler handling message");

        Map<String, Object> params = new HashMap<>();
        params.put("messageJson", messageJson);

        Message message = Message.fromJson(messageJson);
        params.put("header", message.getHeader());

        params.put("body", message.getBody());
        params.put("timestamp", message.getTimestamp());

        commandHandler.executeCommand(message.getHeader(), params);
    }
}
