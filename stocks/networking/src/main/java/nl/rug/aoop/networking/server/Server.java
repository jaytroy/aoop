package nl.rug.aoop.networking.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class Server implements Runnable {
    private final int port;
    private ServerSocket serverSocket;
    private Boolean running = false;
    private ExecutorService service;
    private int id = 0;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(8000);
        this.port = port;
        service = Executors.newCachedThreadPool();
    }

    public int getPort() {
        return this.serverSocket.getLocalPort();
    }

    @Override
    public void run() {
        running = true;
        while (running) {
            try {
                Socket socket = this.serverSocket.accept();
                //here is a new connection
                log.info("New connection from client");
                ClientHandler clientHandler = new ClientHandler(socket, id);
                //clientHandler.run();
                this.service.submit(clientHandler);
                id++;
            } catch (IOException e) {
                log.error("Socket error : ", e);
            }
        }
    }

    public void terminate() {
        running = false;
        this.service.shutdown();
    }

    public boolean isRunning() {
        return this.running;
    }

}
