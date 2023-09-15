package nl.rug.aoop.messagequeue;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Message class that stores the different information on each message.
 */
@Getter
public class Message {
    private final String header;
    private final String body;
    private final LocalDateTime timestamp;

    /**
     * Message constructor.
     *
     * @param messageHeader the head of the message.
     * @param messageBody the body of the message.
     */
    public Message(String messageHeader, String messageBody) {
        this.header = messageHeader;
        this.body = messageBody;
        this.timestamp = LocalDateTime.now();
    }
}
