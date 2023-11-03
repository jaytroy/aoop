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
        System.out.println("Netproducer is putting message");
        NetworkMessage networkMessage = new NetworkMessage(msg.getHeader(), msg.toJson());
        client.sendMessage(networkMessage.toJson());
    }
}