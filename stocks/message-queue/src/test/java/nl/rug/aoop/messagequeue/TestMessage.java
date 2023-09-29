package nl.rug.aoop.messagequeue;

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
}
