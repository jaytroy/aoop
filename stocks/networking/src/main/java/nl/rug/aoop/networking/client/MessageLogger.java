package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;

/**
 * Logs the receiving of messages.
 */
@Slf4j
public class MessageLogger implements MessageHandler { //Is this going to have future functionality?
    @Override
    public void handleMessage(String message){
        log.info("Got message : " + message);
    }
}
