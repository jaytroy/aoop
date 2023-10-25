package nl.rug.aoop.messagequeue.serverside;

import nl.rug.aoop.messagequeue.producer.MQProducer;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.NetworkMessage;
import nl.rug.aoop.networking.client.Client;

/**
 * The network producer. We take in and send input to the server through this class.
 */
public class NetProducer implements MQProducer {
    private Client client;

    /**
     * Netproducer.
     *
     * @param client the client to pass in the producer.
     */
    public NetProducer(Client client) {
        this.client = client;
    }

    @Override
    public void putMessage(Message msg) {
        NetworkMessage networkMessage = new NetworkMessage(msg.getHeader(), msg.toJson());
        client.sendMessage(networkMessage.toJson());
    }

    /**
     * Converts a message to a JSON string by calling a method within the message.
     * @param msg The message being converted.
     * @return Returns a JSON string.
     */

    public String toJson(Message msg) {
        return msg.toJson();
    }
}
