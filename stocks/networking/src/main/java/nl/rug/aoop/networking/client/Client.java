package nl.rug.aoop.networking.client;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
public class Client {
    public static final int TIMEOUT = 1000;
    private InetSocketAddress address;
    private Socket socket;
    private boolean running = false;
    private boolean connected = false;
    private BufferedReader in;
    private PrintWriter out;
    private MessageHandler messageHandler;

    public Client(InetSocketAddress address) throws IOException {
        this.address = address;
        intiSocket(address);
    }

    public void intiSocket(InetSocketAddress address) throws IOException {
        this.socket = new Socket();
        this.socket.connect(address, TIMEOUT);
        if(!this.socket.isConnected()) {
            log.error("Socket could not connect at port " + address.getPort());
            throw new IOException("Socket could not connect");
        }
        connected = true;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out = new PrintWriter(this.socket.getOutputStream());
    }

    public void run() {
        running = true;
        while (running) {
            try {
                String fromServer = in.readLine();
                log.info("Server sent " + fromServer);
                messageHandler = new MessageLogger();
                messageHandler.handleMessage(fromServer);
            } catch (IOException e) {
                log.error("Could not read line from server ", e);
            }
        }
    }

    public void terminate() {
        running = false;
    }
}
