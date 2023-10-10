package nl.rug.aoop.messagequeue.testqueues;

import nl.rug.aoop.messagequeue.queues.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TestMessage {
    private Message message;
    private String messageHeader;
    private String messageBody;


    @BeforeEach
    public void setUp() {
        messageHeader = "header";
        messageBody = "body";
        message = new Message(messageHeader, messageBody);
    }


    @Test
    public void testMessageConstructor() {
        assertEquals(messageHeader, message.getHeader());
        assertEquals(messageBody, message.getBody());
        assertNotNull(message.getTimestamp());
    }

    @Test
    public void testTimeStamp() { //checks if the current message is within 1 second of creating this test
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timestamp = message.getTimestamp();

        LocalDateTime windowStart = now.minusSeconds(1);
        LocalDateTime windowEnd = now.plusSeconds(1);

        assertTrue(timestamp.isAfter(windowStart) && timestamp.isBefore(windowEnd));
    }

    @Test
    public void testMessageImmutable() {
        List<Field> fields = List.of(Message.class.getDeclaredFields());
        fields.forEach(field -> {
            assertTrue(Modifier.isFinal(field.getModifiers()), field.getName() + " is not final");
        });
    }

    @Test
    public void testToJson() {
        String json = message.toJson();
        assertNotNull(json);
        assertTrue(json.contains("\"header\":\"header\""));
        assertTrue(json.contains("\"body\":\"body\""));
        assertTrue(json.contains("\"timestamp\""));
    }

    @Test
    public void testFromJson() {
        String json = "{\"header\":\"testHeader\",\"body\":\"testBody\",\"timestamp\":\"2023-10-10T12:00:00\"}";
        Message newMessage = Message.fromJson(json);
        assertNotNull(newMessage);
        assertEquals("testHeader", newMessage.getHeader());
        assertEquals("testBody", newMessage.getBody());
        assertNotNull(newMessage.getTimestamp());
        assertEquals(LocalDateTime.parse("2023-10-10T12:00:00"), newMessage.getTimestamp());
    }

    @Test
    public void testOverallToJsonAndFromJson() {
        Message originalMessage = new Message("header", "body");
        String json = originalMessage.toJson();
        Message newMessage = Message.fromJson(json);
        assertEquals(originalMessage.getHeader(), newMessage.getHeader());
        assertEquals(originalMessage.getBody(), newMessage.getBody());
        assertEquals(originalMessage.getTimestamp(), newMessage.getTimestamp());
    }

}
