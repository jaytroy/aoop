package nl.rug.aoop.networking;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Represents a network message with a header and a body.
 */
public class NetworkMessage {
    private String header;
    private String body;

    /**
     * Creates a new NetworkMessage with the specified header and body.
     *
     * @param header The header of the network message.
     * @param body   The body of the network message.
     */
    public NetworkMessage(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    /**
     * Converts the NetworkMessage object to its JSON representation.
     *
     * @return The JSON representation of the NetworkMessage.
     */
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Creates a NetworkMessage object from its JSON representation.
     *
     * @param json The JSON representation of the NetworkMessage.
     * @return The NetworkMessage object created from JSON.
     * @throws RuntimeException if there is an issue with JSON deserialization.
     */
    public static NetworkMessage fromJson(String json) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, NetworkMessage.class);
        } catch (JsonSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "NetworkMessage{" +
                "header='" + header + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
