package nl.rug.aoop.networking;

import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestNetworkMessage {
    @Test
    public void testToJson() {
        NetworkMessage message = new NetworkMessage("Header", "Body");
        String json = message.toJson();
        assertNotNull(json);
    }

    @Test
    public void testFromJson() {
        String jsonString = "{\"header\":\"Header\",\"body\":\"Body\"}";
        NetworkMessage message = NetworkMessage.fromJson(jsonString);
        assertNotNull(message);
        assertEquals("Header", message.getHeader());
        assertEquals("Body", message.getBody());
    }

    @Test
    public void testInvalidJson() {
        String invalidJson = "invalid_json";
        assertThrows(RuntimeException.class, () -> {
            NetworkMessage.fromJson(invalidJson);
        });
    }

    @Test
    public void testToString() {
        NetworkMessage message = new NetworkMessage("Header", "Body");
        String toString = message.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("header='Header'"));
        assertTrue(toString.contains("body='Body'"));
    }
}
