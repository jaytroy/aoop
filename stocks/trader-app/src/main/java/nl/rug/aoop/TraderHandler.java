package nl.rug.aoop;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.producer.MQProducer;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;

import java.util.Map;

public class TraderHandler implements MessageHandler, MQProducer {
    private final CommandHandler commandHandler;

    public TraderHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public void handleMessage(String message) {

    }

    @Override
    public void putMessage(Message message) {
        String messageJson = message.toJson();

        commandHandler.executeCommand("mqputcommand", Map.of("messageJson", messageJson));
    }
}
