package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageLogger implements MessageHandler {
    @Override
    public void handleMessage(String message){
        log.info("Got message : " + message);
    }
}
