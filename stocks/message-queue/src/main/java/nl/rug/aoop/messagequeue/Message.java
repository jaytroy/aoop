package nl.rug.aoop.messagequeue;

import lombok.Getter;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

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

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    public static Message fromJson(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(json, Message.class);
    }
}
