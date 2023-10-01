package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class MagicInput implements InputGenerator {
    private boolean running = false;
    private Client client;

    public MagicInput(Client client) {
        this.client = client;
    }

    @Override
    public void run(Client client) {
        Scanner scanner = new Scanner(System.in);
        running = true;
        while (running) {
            String userInput = scanner.nextLine();

            if (userInput.trim().equalsIgnoreCase("quit")) {
                client.terminate();
                terminate();
            } else {
                client.sendMessage("Message: " + userInput);
                log.info("Message sent successfully");
            }
        }
        scanner.close();
    }

    public void terminate() {
        running = false;
    }
}
