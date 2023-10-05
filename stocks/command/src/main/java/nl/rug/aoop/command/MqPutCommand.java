package nl.rug.aoop.command;

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
