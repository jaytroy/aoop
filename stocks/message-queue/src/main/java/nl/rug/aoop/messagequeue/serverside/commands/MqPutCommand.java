package nl.rug.aoop.messagequeue.serverside.commands;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.command.Command;

import java.util.Map;

/**
 * MQPutCommand class.
 */

@Slf4j
public class MqPutCommand implements Command {
    private final MessageQueue messageQueue;

    /**
     * Constructor.
     *
     * @param messageQueue message queue you pass in.
     */

    public MqPutCommand(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void execute(Map<String, Object> params) {
        log.info("Executing MQPut");
        String messageJson = (String) params.get("messageJson");
        Message message = Message.fromJson(messageJson);
        Message message2 = Message.fromJson(message.getBody());

        messageQueue.enqueue(message2);
        log.info("Message enqueued : " + message);
    }
}