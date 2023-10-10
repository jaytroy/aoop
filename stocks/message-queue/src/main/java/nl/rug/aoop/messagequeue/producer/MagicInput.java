package nl.rug.aoop.messagequeue.producer;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.messagequeue.serverside.NetProducer;

import java.util.Scanner;

/**
 * Handles client input.
 */
@Slf4j
public class MagicInput implements InputGenerator {
    private boolean running = false;

    @Override
    public void run(NetProducer producer) {
        Scanner scanner = new Scanner(System.in); //Initialize reading from console
        running = true;
        while (running) { //While running the input reader?
            log.info("Enter a header");
            String header = scanner.nextLine(); //Read from console
            log.info("Enter a body");
            String body = scanner.nextLine(); //Read from console
            Message msg = new Message(header,body);

            producer.putMessage(msg);
            /* This should be sent to the client which would then terminate.
            if (userInput.trim().equalsIgnoreCase("quit")) { //Quitting the reader
                client.terminate(); //Terminates the client
                terminate(); //Then terminates the actual input reader
            } else {
                client.sendMessage("Message: " + userInput); //Sends message to client
                log.info("Message sent successfully");
            }*/
        }
        scanner.close();
    }

    /**
     * Terminates the reading of input.
     */
    public void terminate() {
        running = false;
    }
}

