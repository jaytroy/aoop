package nl.rug.aoop.networking.client;

/**
 * An interface for client classes which read input.
 */
public interface InputGenerator {
    /**
     * Starts a function which allows us to read input.
     * @param client The client for whom we are handling input.
     */
    void run(Client client);

    /**
     * Terminates run() by setting bool running to false.
     */
    void terminate();
}
