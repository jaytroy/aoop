package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.messagequeue.serverside.commands.CommandMessageHandler;
import nl.rug.aoop.messagequeue.serverside.commands.MqPutCommand;
import nl.rug.aoop.messagequeue.queues.TSMessageQueue;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.model.Stock;
import nl.rug.aoop.model.Trader;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.model.Exchange;
import nl.rug.aoop.networking.MessageHandler;

import java.io.IOException;
import java.util.List;


/**
 * This class provides initialization for the Stock App, including starting the server and setting up the view.
 */
@Slf4j
public class Initialize {
    /**
     * Initialize the Stock App, start the server, and set up the view.
     */
    public static void initialize() {
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

    //Change this to a timer?
    private static void updateView(SimpleViewFactory viewFactory) {
        while (true) {
            viewFactory.updateView();
            try {
                Thread.sleep(17); //updates ever ~every hz on a 60 hz screen
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