package nl.rug.aoop.networking.client;

import java.util.Scanner;

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
            client.sendMessage(userInput);

            if (userInput.trim().equalsIgnoreCase("quit")) {
                client.terminate();
                terminate();
            }
        }
        scanner.close();
    }

    public void terminate() {
        running = false;
    }
}
