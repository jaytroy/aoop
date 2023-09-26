package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class ClientMain {
    public static void main(String[] args){
        try {
            Client client = new Client(new InetSocketAddress("localhost", 8000));
            client.run();
            log.info("Client connected to server");
        } catch (IOException e) {
            log.error("Client could not connect to server", e);
        }
    }
}