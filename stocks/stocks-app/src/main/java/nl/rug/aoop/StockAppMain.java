package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
import nl.rug.aoop.command.CommandHandler;
import nl.rug.aoop.initialization.SimpleViewFactory;
import nl.rug.aoop.messagequeue.queues.Message;
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
import nl.rug.aoop.stockcommands.BuyLimitOrderCommand;
import nl.rug.aoop.stockcommands.SellLimitOrderCommand;

import java.io.IOException;
import java.util.List;
import java.util.Random;

@Slf4j
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
        commandHandler.registerCommand("PUT", mqPutCommand);

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

        Random random = new Random();

        String[] stockSymbols = {"AAPL", "TSLA", "AMZN", "MSFT", "NVDA", "AMD", "ADBE", "FB", "INTC", "AOOP", "MRNA"};
        int numOrders = 10;

        for (int i = 0; i < numOrders; i++) {
            String randomStockSymbol = stockSymbols[random.nextInt(stockSymbols.length)];
            int randomQuantity = random.nextInt(10000) + 1;
            int randomTrader = random.nextInt(10);
            int buyOrSell = random.nextInt(2);
            double priceFactor = 1.0 + (0.01 * random.nextDouble());
            double limitPrice;

            Stock stock = findStockBySymbol(stocks, randomStockSymbol);

            if (stock != null) {
                if (buyOrSell == 1) {
                    limitPrice = stock.getPrice() * priceFactor;
                    BuyLimitOrderCommand buyLimitOrderCommand = new BuyLimitOrderCommand(findTraderById(traders, "bot" + randomTrader),
                            stock, limitPrice, randomQuantity);
                    commandHandler.registerCommand("BUY " + i, buyLimitOrderCommand);
                } else {
                    limitPrice = stock.getPrice() / priceFactor;
                    SellLimitOrderCommand sellLimitOrderCommand = new SellLimitOrderCommand(findTraderById(traders, "bot" + randomTrader),
                            stock, limitPrice, randomQuantity);
                    commandHandler.registerCommand("SELL " + i, sellLimitOrderCommand);
                }
            } else {
                log.error("Stock with symbol " + randomStockSymbol + " not found.");
            }

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                log.error("Thread failed to sleep");
            }
        }
        //Start up the view

        StockExchange stockExchange = new StockExchange(stocks, traders); //This is the UI exchange
        SimpleViewFactory viewFactory = new SimpleViewFactory();
        viewFactory.createView(stockExchange);

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

    private static Trader findTraderById(List<Trader> traders, String id) {
        for (Trader trader : traders) {
            if (trader.getId().equals(id)) {
                return trader;
            }
        }
        return null;
    }
}

