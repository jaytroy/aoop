package nl.rug.aoop.network;

import nl.rug.aoop.networking.MessageHandler;

public class OrderHandler implements MessageHandler {
    @Override
    public void handleMessage(String message) {
        //We should do whatever with the order here.
        //Consider this and intersection which deconstructs a message and tells the program what to do based on the logic.
    }
}
