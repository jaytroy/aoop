package nl.rug.aoop.messagequeue.serverside.commands;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.command.Command;

import java.util.Map;

public class MqPutCommand implements Command {
    private final MessageQueue messageQueue;

    public MqPutCommand(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void execute(Map<String, Object> params) {
        String messageJson = (String) params.get("messageJson");
        Message message = Message.fromJson(messageJson);

        // add more execution logic here, by batter is gonna run out so i cant do more now.

        // Enqueue the message into the message queue
        messageQueue.enqueue(message);
    }
}
