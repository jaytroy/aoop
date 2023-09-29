package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class ClientMain {
    public static void main(String[] args) {
        try {
            Client client = new Client(new InetSocketAddress("localhost", 8000));
            client.connect();
            client.setMessageHandler(new MessageLogger());

            Thread clientThread = new Thread(client);
            clientThread.start();

            while (!client.isConnected()) {
                Thread.sleep(100);
            }

            log.info("Client connected to server");

            InputGenerator inputGenerator = new MagicInput(client);
            inputGenerator.run(client);

        } catch (IOException e) {
            log.error("Client could not connect to server", e);
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for client connection", e);
        }
    }
}
