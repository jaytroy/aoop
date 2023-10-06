package nl.rug.aoop.messagequeue.producer;

import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.client.Client;

/**
 * The network producer. We take in and send input to the server through this class.
 */
public class NetProducer implements MQProducer {
    private InputGenerator input;
    private Client client;

    /**
     * NetProducer constructor.
     */
    public NetProducer(Client client) {
        this.client = client;
        input = new MagicInput();
        getInput();
    }

    @Override
    public void putMessage(Message msg) { //Should put a message into the queue via a network
        client.sendMessage(toJson(msg));
    }

    /**
     * Converts a message to a JSON string by calling a method within the message.
     * @param msg The message being converted.
     * @return Returns a JSON string.
     */
    public String toJson(Message msg) {
        return msg.toJson();
    }

    /**
     * Runs the function in the MagicInput class which allows us to take in input.
     */
    public void getInput() {
        input.run(this);
    }


    /**
     * Stops the function in the MagicInput class which allows us to take in input.
     */
    public void stopInput() {
        input.terminate();
    }
}
