package nl.rug.aoop.messagequeue;

import lombok.Getter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that implements MQProducer so that you can enqueue messages.
 */

public class Producer implements MQProducer {
    @Getter
    private MessageQueue messageQueue;

    /**
     * Constructor for ComProducer.
     *
     * @param messageQueue the messagequeue.
     */
    public Producer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    //Defines a test which checks whether the header is made out of english characters
    @Override
    public void putMessage(Message message) {
        try {
            Pattern pattern = Pattern.compile("^[a-z]*$", Pattern.CASE_INSENSITIVE);
            Matcher matchHeader = pattern.matcher(message.getHeader());
            Matcher matchBody = pattern.matcher(message.getBody());

            if (!matchHeader.find()) {
                throw new IOException("Header contains illegal characters.");
            }
            if (!matchBody.find()) {
                throw new IOException("Body contains illegal characters.");
            }

            maxCharacters(message);

            messageQueue.enqueue(message);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    @Override
    public void maxCharacters(Message message) {
        try {
            String header = message.getHeader();
            String body = message.getBody();

            if (header.length() + body.length() > 280) {
                throw new IOException("Message exceeds maximum length of 280 characters.");
            }

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}