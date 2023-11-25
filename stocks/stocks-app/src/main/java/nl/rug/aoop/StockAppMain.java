package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.TSMessageQueue;
import nl.rug.aoop.messagequeue.serverside.commands.CommandMessageHandler;
import nl.rug.aoop.messagequeue.serverside.commands.MqPutCommand;
import nl.rug.aoop.model.Exchange;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.server.Server;

import java.io.IOException;

/**
 * This class serves as the entry point for the Stock App.
 * It initializes the view and other components of the application.
 */
@Slf4j
public class StockAppMain {
    /**
     * The main method that initializes the Stock App view and components.
     *
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        int port = getPort();

        MessageQueue messageQueue = new TSMessageQueue();

        CommandHandler commandHandler = new CommandHandler();
        MqPutCommand mqPutCommand = new MqPutCommand(messageQueue);
        commandHandler.registerCommand("PUT", mqPutCommand);

        MessageHandler handler = new CommandMessageHandler(commandHandler);

        Server server = new Server(handler, port);
        Thread serverThread = new Thread(server);
        try {
            server.start();
        } catch (IOException e) {
            log.error("Failed to start stockapp");
        }
        serverThread.start();
        // Set up the Exchange
        Exchange stockApp = new Exchange(messageQueue, server);
        // Start the view
        SimpleViewFactory viewFactory = new SimpleViewFactory();
        viewFactory.createView(stockApp);

        updateView(viewFactory);
    }

    private static void updateView(SimpleViewFactory viewFactory) {
        while (true) {
            viewFactory.updateView();
            try {
                Thread.sleep(8); //Updates ever ~every hz on a 120 hz screen
            } catch (InterruptedException e) {
                log.error("Failed to put thread to sleep");
            }
        }
    }

    /**
     * Determine the port for the server, either from the environment variable or use a backup port.
     *
     * @return The selected port for the server.
     */
    private static int getPort() {
        int port;
        int BACKUP_PORT = 8080;
        if (System.getenv("MESSAGE_QUEUE_PORT") != null) {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } else {
            port = BACKUP_PORT;
            log.info("Using backup port at StockAppMain");
        }
        return port;
    }
}
