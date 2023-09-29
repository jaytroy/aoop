package nl.rug.aoop.networking.client;

public interface InputGenerator {
    void run(Client client);
    void terminate();
}
