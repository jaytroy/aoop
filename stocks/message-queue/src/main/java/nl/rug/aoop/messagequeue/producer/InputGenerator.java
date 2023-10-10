package nl.rug.aoop.messagequeue.producer;

import nl.rug.aoop.messagequeue.serverside.NetProducer;

/**
 * An interface for client classes which read input.
 */
public interface InputGenerator {
    /**
     * Starts a function which allows us to read input.
     * @param producer The producer to whom we'll relay the input.
     */
    void run(NetProducer producer);

    /**
     * Terminates run() by setting bool running to false.
     */
    void terminate();
}
