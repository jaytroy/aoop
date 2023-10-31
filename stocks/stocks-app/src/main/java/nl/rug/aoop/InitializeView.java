package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
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
import nl.rug.aoop.stockcommands.BuyLimitOrderCommand;
import nl.rug.aoop.stockcommands.SellLimitOrderCommand;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
public class InitializeView {
    public static void initialize() {
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

        //Start up the view
        StockExchange stockExchange = new StockExchange(stocks, traders); //This is the UI exchange
        SimpleViewFactory viewFactory = new SimpleViewFactory();
        viewFactory.createView(stockExchange);


        Thread appThread = new Thread(stockApp);
        appThread.start();
        //Start the stock app

        Random random = new Random();

        String[] stockSymbols = {"AAPL", "TSLA", "AMZN", "MSFT", "NVDA", "AMD", "ADBE", "FB", "INTC", "AOOP", "MRNA"};
        int numOrders = 10;

        for (int i = 0; i < numOrders; i++) {
            String randomStockSymbol = stockSymbols[random.nextInt(stockSymbols.length)];
            int randomQuantityBuy = random.nextInt(100) + 1;;
            int randomQuantitySell = random.nextInt(10) + 1;
            int randomTrader = random.nextInt(9) + 1;
            int buyOrSell = random.nextInt(2);
            double priceFactor = 1.0 + (0.01 * random.nextDouble());
            double limitPrice;

            Stock stock = findStockBySymbol(stocks, randomStockSymbol);

            if (stock != null) {
                if (buyOrSell == 1) {
                    limitPrice = stock.getPrice() * priceFactor;
                    BuyLimitOrderCommand buyLimitOrderCommand = new BuyLimitOrderCommand(findTraderById(traders, "bot" + randomTrader),
                            stock, limitPrice, randomQuantityBuy);
                    commandHandler.registerCommand("BUY " + i, buyLimitOrderCommand);
                    commandHandler.executeCommand("BUY " + i, null);
                    viewFactory.updateView();
                } else {
                    Map<String, Integer> stockSymbolSell = findTraderById(traders, "bot" + randomTrader).getOwnedStocks();
                    String[] stockSymbolsSelling = stockSymbolSell.keySet().toArray(new String[0]);
                    String randomStockSymbolSelling = stockSymbolsSelling[random.nextInt(stockSymbolsSelling.length)];
                    Stock stockToSell = findStockBySymbol(stocks, randomStockSymbolSelling);
                    limitPrice = stockToSell.getPrice() / priceFactor;
                    SellLimitOrderCommand sellLimitOrderCommand = new SellLimitOrderCommand(findTraderById(traders, "bot" + randomTrader),
                            stockToSell, limitPrice, randomQuantitySell);
                    commandHandler.registerCommand("SELL " + i, sellLimitOrderCommand);
                    commandHandler.executeCommand("SELL " + i, null);
                    viewFactory.updateView();

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
