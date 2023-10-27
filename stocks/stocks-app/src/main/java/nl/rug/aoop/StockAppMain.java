package nl.rug.aoop;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.messagequeue.serverside.commands.CommandMessageHandler;
import nl.rug.aoop.messagequeue.serverside.commands.MqPutCommand;
import nl.rug.aoop.messagequeue.serverside.TSMessageQueue;
import nl.rug.aoop.messagequeue.queues.MessageQueue;


import nl.rug.aoop.model.Stock;
import nl.rug.aoop.model.StockExchange;
import nl.rug.aoop.model.Trader;
import nl.rug.aoop.networking.server.Server;
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
        MessageQueue messageQueue = new TSMessageQueue();
        MqPutCommand mqPutCommand = new MqPutCommand(messageQueue);

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.registerCommand("mqputcommand", mqPutCommand);
        //commandHandler.registerCommand("Buy", );
        //commandHandler.registerCommand("Sell", );

        MessageHandler handler = new CommandMessageHandler(commandHandler); //Is this the right way to go?
        Server server = new Server(handler,port);
        Thread serverThread = new Thread(server);
        try {
            server.start(); //Start the server
        } catch(IOException e) {
            e.printStackTrace();
        }
        serverThread.start(); //Start the thread


        //Now the exchange part
        Exchange stockApp = new Exchange(messageQueue);

        //Should this be here? Why is UI here?
        List<Stock> stocks = stockApp.getStocks();
        List<Trader> traders = stockApp.getTraders();

        //Start up the view
        StockExchange stockExchange = new StockExchange(stocks, traders); //This is the UI exchange
        SimpleViewFactory viewFactory = new SimpleViewFactory();
        viewFactory.createView(stockExchange);

        //Start the stock app
        Thread appThread = new Thread(stockApp);
        appThread.start();
    }
}