package nl.rug.aoop.messagequeue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    void testTimeStamp() { //checks if the current message is within 1 second of creating this test
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime timestamp = message.getTimestamp();

        LocalDateTime windowStart = now.minusSeconds(1);
        LocalDateTime windowEnd = now.plusSeconds(1);

        assertTrue(timestamp.isAfter(windowStart) && timestamp.isBefore(windowEnd));
    }


    @Test
    void testNonEmptyHeaderAndBody() { // Test checks if either the header or body is empty
        assertNotEquals("", message.getHeader());
        assertNotEquals("", message.getBody());
    }

    @Test
    void testMaxCharacter() {
        String longHeader = "This is more than two hundred and eighty characters This is more than two hundred and " +
                "eighty characters This is more than two hundred and eighty characters This is more than two hundred " +
                "and eighty characters This is more than two hundred and eighty characters This is more than two " +
                "hundred and eighty characters";
        String longBody = "This is more than two hundred and eighty characters This is more than two hundred and " +
                "eighty characters This is more than two hundred and eighty characters This is more than two hundred " +
                "and eighty characters This is more than two hundred and eighty characters This is more than two " +
                "hundred and eighty characters";
        String shortHeader = "This is a short header";
        String shortBody = "This is a short header";
        String maxHeader = "This is the max amount of characters. This is the max amount of characters. This is the " +
                "max amount of characters. This is the max amount of ";
        String maxBody = "This is the max amount of characters. This is the max amount of characters. This is the " +
                "max amount of characters. This is the max amount of ";

        String greaterThanSixtyHeader = "Header should be a max of sixty character Header should be a max of sixty " +
                "characters ";
        String sixtyHeader = "Header should be a max of sixty characters Header should be ";
        String lessThanSixtyHeader = "Header should be a max of sixty character";

        assertTrue((longHeader.length() + longBody.length()) > 280);
        assertTrue((shortHeader.length() + shortBody.length()) < 280);
        assertEquals(280, maxHeader.length() + maxBody.length());
        assertTrue(greaterThanSixtyHeader.length() > 60);
        assertEquals(60, sixtyHeader.length());
        assertTrue(lessThanSixtyHeader.length() < 60);
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
