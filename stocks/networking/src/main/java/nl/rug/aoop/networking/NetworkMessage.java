package nl.rug.aoop.networking;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class NetworkMessage {
    private String header;
    private String body;

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

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

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
