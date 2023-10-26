package nl.rug.aoop;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.serverside.commands.CommandMessageHandler;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.ui.StockUI;
import nl.rug.aoop.ui.StockExchange;
import nl.rug.aoop.ui.TraderUI;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.network.Exchange;
import nl.rug.aoop.networking.MessageHandler;

import java.io.IOException;
import java.util.List;

public class StockAppMain {
    public static void main(String[] args) {
        // I don't think this is needed here, but it's needed somewhere else
        int port;
        int BACKUP_PORT = 8080;
        if(System.getenv("MESSAGE_QUEUE_PORT") != null) {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } else {
            port = BACKUP_PORT;
            System.out.println("Using backup port at StockAppMain");
        }
        //Start the server
        MessageHandler handler = new CommandMessageHandler(new CommandHandler()); //Is this the right way to go?
        Server server = new Server(handler,port);
        Thread serverThread = new Thread(server);
        try {
            server.start(); //Start the server
        } catch(IOException e) {
            e.printStackTrace();
        }
        serverThread.start(); //Start the thread


        //Now the exchange part
        Exchange stockApp = new Exchange();

        //Should this be here? Why is UI here?
        List<StockUI> stocks = stockApp.initializeStocks();
        List<TraderUI> traderUIS = stockApp.initializeTraders();

        //Start up the view
        StockExchange stockExchange = new StockExchange(stocks, traderUIS); //This is the UI exchange
        SimpleViewFactory viewFactory = new SimpleViewFactory();
        viewFactory.createView(stockExchange);

        //Start the stock app
        Thread appThread = new Thread(stockApp);
        appThread.start();
    }
}