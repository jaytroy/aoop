package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static org.junit.jupiter.api.Assertions.*;


public class TestMessage {

    private Message message;
    private String messageHeader;
    private String messageBody;


    @BeforeEach
    void setUp() {
        messageHeader = "header";
        messageBody = "body";
        message = new Message(messageHeader, messageBody);
    }


    @Test
    void testMessageConstructor() {
        assertEquals(messageHeader, message.getHeader());
        assertEquals(messageBody, message.getBody());
        assertNotNull(message.getTimestamp());
    }

    @Test
    void testMessageImmutable() {
        List<Field> fields = List.of(Message.class.getDeclaredFields());
        fields.forEach(field -> {
            assertTrue(Modifier.isFinal(field.getModifiers()), field.getName() + " is not final");
        });
    }

    //Defines a test which checks whether the header is made out of english characters
    @Test
    void testMessageValidChars() {
        Message message1 = new Message("TEST", "test"); //Is this line necessary?

        Pattern pattern = Pattern.compile("^[a-z]*$", Pattern.CASE_INSENSITIVE);
        Matcher matchHeader = pattern.matcher(message1.getHeader());
        Matcher matchBody = pattern.matcher(message1.getBody());

        assertTrue(matchHeader.find());
        assertTrue(matchBody.find());
    }
}
