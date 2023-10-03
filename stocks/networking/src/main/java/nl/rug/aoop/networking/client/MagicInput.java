package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

/**
 * Handles client input.
 */
@Slf4j
public class MagicInput implements InputGenerator {
    private boolean running = false;

    @Override
    public void run(Client client) {
        Scanner scanner = new Scanner(System.in); //Initialize reading from console
        running = true;
        while (running) { //While running the input reader?
            String userInput = scanner.nextLine(); //Read from console

            if (userInput.trim().equalsIgnoreCase("quit")) { //Quitting the reader
                client.terminate(); //Terminates the client
                terminate(); //Then terminates the actual input reader
            } else {
                client.sendMessage("Message: " + userInput); //Sends message to client
                log.info("Message sent successfully");
            }
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
