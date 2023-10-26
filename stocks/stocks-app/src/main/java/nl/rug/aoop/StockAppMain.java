package nl.rug.aoop;

import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.messagequeue.serverside.commands.CommandMessageHandler;
import nl.rug.aoop.messagequeue.serverside.commands.MqPutCommand;
import nl.rug.aoop.messagequeue.serverside.TSMessageQueue;
import nl.rug.aoop.messagequeue.queues.MessageQueue;


import nl.rug.aoop.model.Stock;
import nl.rug.aoop.model.Trader;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.network.Exchange;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.stockcommands.BuyLimitOrderCommand;
import nl.rug.aoop.stockcommands.SellLimitOrderCommand;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
        Scanner scanner = new Scanner(System.in);

        CommandHandler commandHandler = new CommandHandler();

        commandHandler.registerCommand("mqputcommand", mqPutCommand);

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
        List<Stock> stocks = stockApp.initializeStocks();
        List<Trader> traders = stockApp.initializeTraders();

        while (true) {
            System.out.println("Enter 'B' to buy or 'S' to sell (or 'Q' to quit):");
            String action = scanner.nextLine();

            if (action.equalsIgnoreCase("Q")) {
                break;
            }

            if (action.equalsIgnoreCase("B") || action.equalsIgnoreCase("S")) {
                System.out.print("Enter the trader name: ");
                String traderName = scanner.nextLine();
                System.out.print("Enter the stock symbol: ");
                String stockSymbol = scanner.nextLine();
                System.out.print("Enter the limit price: ");
                double limitPrice = scanner.nextDouble();
                System.out.print("Enter the quantity: ");
                int quantity = scanner.nextInt();

                if (action.equalsIgnoreCase("B")) {
                    BuyLimitOrderCommand buyCommand = new BuyLimitOrderCommand(findTraderByName(traders, traderName), findStockBySymbol(stocks, stockSymbol), limitPrice, quantity);

                    Map<String, Object> params = new HashMap<>();
                    commandHandler.registerCommand("Buy", buyCommand);
                    buyCommand.execute(params);
                } else if (action.equalsIgnoreCase("S")) {
                    SellLimitOrderCommand sellCommand = new SellLimitOrderCommand(findTraderByName(traders, traderName), findStockBySymbol(stocks, stockSymbol), limitPrice, quantity);

                    Map<String, Object> params = new HashMap<>();
                    commandHandler.registerCommand("Sell", sellCommand);
                    sellCommand.execute(params);
                }

                scanner.nextLine(); // Consume the newline character
            }
        }
        //Start up the view
        //StockExchange stockExchange = new StockExchange(stocks, traders); //This is the UI exchange
        //SimpleViewFactory viewFactory = new SimpleViewFactory();
        //viewFactory.createView(stockExchange);

        //Start the stock app
        Thread appThread = new Thread(stockApp);
        appThread.start();
    }

    private static Stock findStockBySymbol(List<Stock> stocks, String symbol) {
        for (Stock stock : stocks) {
            if (stock.getSymbol().equals(symbol)) {
                return stock;
            }
        }
        return null;
    }

    private static Trader findTraderByName(List<Trader> traders, String name) {
        for (Trader trader : traders) {
            if (trader.getName().equals(name)) {
                return trader;
            }
        }
        return null;
    }
}

