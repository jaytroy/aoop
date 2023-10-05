package nl.rug.aoop.messagequeue.producer;

import lombok.Getter;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.queues.MessageQueue;

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
     * @param messageQueue the message queue.
     */
    public Producer(MessageQueue messageQueue) {
        this.messageQueue = messageQueue;
    }

    //Defines a test which checks whether the header is made out of english characters
    @Override
    public void putMessage(Message message) {
        try {
            Pattern pattern = Pattern.compile("^[a-z\s]+$", Pattern.CASE_INSENSITIVE);
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

    public void maxCharacters(Message message) throws IOException {
        String header = message.getHeader();
        String body = message.getBody();

        if (header.length() + body.length() > 280) {
            throw new IOException("Message exceeds maximum length of 280 characters.");
        }
    }
}