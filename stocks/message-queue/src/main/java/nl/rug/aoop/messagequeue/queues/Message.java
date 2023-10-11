package nl.rug.aoop.messagequeue.queues;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Message class that stores the different information on each message.
 */
@Getter
public class Message implements Comparable<Message> {
    private final String header;
    private final String body;
    @Getter
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

    /**
     * Turns a string into JSON.
     *
     * @return Returns the string converted to JSON.
     */
    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    /**
     * Turns a string from JSON to message.
     *
     * @param json The JSON string.
     * @return Returns a message.
     */
    public static Message fromJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, Message.class);
    }

    @Override
    public int compareTo(Message m) {
        return this.timestamp.compareTo(m.timestamp);
    }
}
