package nl.rug.aoop.networking.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
public class ServerMain {
    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        try {
            Server server = new Server(8000);
            service.submit(server);
            log.info("Server started at port " + server.getPort());

        } catch (IOException e) {
            log.error("Could not run server ", e);
        }
    }
}
