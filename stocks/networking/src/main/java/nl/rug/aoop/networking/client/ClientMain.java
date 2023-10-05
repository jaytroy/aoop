package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Runs the client.
 */
@Slf4j
public class ClientMain {
    /**
     * Main method of client.
     * @param args Standard signature of main.
     */
    public static void main(String[] args) {
        /*Client client = new Client(new InetSocketAddress("localhost", 8000));

        try {
            client.connect();

            Thread clientThread = new Thread(client);
            clientThread.start();

            while (!client.isConnected()) {
                Thread.sleep(100);
            }

            log.info("Client connected to server");

        } catch (IOException e) {
            log.error("Client could not connect to server", e);
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for client connection", e);
        }*/
    }
}
