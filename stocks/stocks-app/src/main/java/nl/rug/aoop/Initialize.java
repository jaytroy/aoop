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
            e.printStackTrace();
        }
        serverThread.start();
        // Set up the Exchange
        Exchange stockApp = new Exchange(messageQueue,server);
<<<<<<< HEAD
        List<Stock> stocks = stockApp.getStocks();
        List<Trader> traders = stockApp.getTraders();
=======

>>>>>>> refs/remotes/origin/stonks
        // Start the view
        SimpleViewFactory viewFactory = new SimpleViewFactory();
        viewFactory.createView(stockApp);

<<<<<<< HEAD
        updateView(viewFactory);
    }

    private static void updateView(SimpleViewFactory viewFactory) {
=======
        int updates = 0;
>>>>>>> refs/remotes/origin/stonks
        while (true) {
            viewFactory.updateView();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
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
            System.out.println("Using backup port at StockAppMain");
        }
        return port;
    }

    /**
     * Finds the stock by symbol.
     *
     * @param stocks the list of stocks.
     * @param symbol the symbol.
     * @return the stock you are trying to find.
     */
    private static Stock findStockBySymbol(List<Stock> stocks, String symbol) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equals(symbol)) {
                return stock;
            }
        }
        return null;
    }

    /**
     * Finds the trader by id.
     *
     * @param traders the list of traders.
     * @param id the id.
     * @return the trader you are trying to find.
     */

    private static Trader findTraderById(List<Trader> traders, String id) {
        for (Trader trader : traders) {
            if (trader.getId().equals(id)) {
                return trader;
            }
        }
        return null;
    }
}