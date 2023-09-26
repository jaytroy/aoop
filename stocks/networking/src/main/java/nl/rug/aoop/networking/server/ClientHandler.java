package nl.rug.aoop.networking.server;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@Slf4j
public class ClientHandler implements Runnable {
    private Socket socket;
    private int id;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean running = false;

    public ClientHandler(Socket socket, int id) throws IOException {
        this.socket = socket;
        this.id = id;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        running = true;
        out.println("Hello, enter BYE to exit. You id : " + id);
        while (running) {
            try {
                String fromClient = in.readLine();
                if (fromClient == null || fromClient.trim().equalsIgnoreCase("BYE")) { //change this
                    terminate();
                    break;
                }
                out.println(fromClient);
                log.info("Recieved from client " + id + fromClient);
            } catch (IOException e) {
                log.error("Error reading string from client with id" + id, e);
            }
        }
    }
    public void terminate() {
        running = false;
        try {
            this.socket.close();
        } catch (IOException e) {
            log.error("Could not close the socket ", e);
        }
    }
}
