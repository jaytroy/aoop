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
        List<Stock> stocks = stockApp.getStocks();
        List<Trader> traders = stockApp.getTraders();

        // Start the view
        SimpleViewFactory viewFactory = new SimpleViewFactory();
        viewFactory.createView(stockApp);

        //buyAndSellActivation(stocks, traders, commandHandler, viewFactory);
    }

/*
    private static void buyAndSellActivation(List<Stock> stocks, List<Trader> traders, CommandHandler commandHandler, 
                                             SimpleViewFactory viewFactory) {
        Random random = new Random();
        String[] stockSymbols = {"AAPL", "TSLA", "AMZN", "MSFT", "NVDA", "AMD", "ADBE", "FB", "INTC", "AOOP", "MRNA"};
        int numOrders = 10;
        for (int i = 0; i < numOrders; i++) {
            String randomStockSymbol = stockSymbols[random.nextInt(stockSymbols.length)];
            int randomTrader = random.nextInt(9) + 1;
            int buyOrSell = random.nextInt(2);
            double priceFactor = 1.0 + (0.01 * random.nextDouble());
            Stock stock = findStockBySymbol(stocks, randomStockSymbol);
            if (stock != null) {
                if (buyOrSell == 1) {
                    buyCommand(traders, commandHandler, viewFactory, stock, priceFactor, randomTrader, i);
                } else {
                    sellCommand(stocks, traders, commandHandler, viewFactory, randomTrader, priceFactor, i);
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

    private static void buyCommand(List<Trader> traders, CommandHandler commandHandler, SimpleViewFactory viewFactory,
                                   Stock stock, double priceFactor, int randomTrader, int i) {
        Random random = new Random();
        double limitPrice;
        int randomQuantityBuy = random.nextInt(100) + 1;;
        limitPrice = stock.getPrice() * priceFactor;
        BuyLimitOrderCommand buyLimitOrderCommand = new BuyLimitOrderCommand(findTraderById(traders,
                "bot" + randomTrader),
                stock, limitPrice, randomQuantityBuy);
        commandHandler.registerCommand("BUY " + i, buyLimitOrderCommand);
        viewFactory.updateView();
    }

    private static void sellCommand(List<Stock> stocks, List<Trader> traders, CommandHandler commandHandler,
                                    SimpleViewFactory viewFactory, int randomTrader, double priceFactor, int i) {
        Random random = new Random();
        double limitPriceMap<String,Integer> ownedStocks, int availableFunds;
        int randomQuantitySell = random.nextInt(10) + 1;
        Map<String, Integer> stockSymbolSell = findTraderById(traders, "bot" + randomTrader).
                getOwnedStocks();
        String[] stockSymbolsSelling = stockSymbolSell.keySet().toArray(new String[0]);
        String randomStockSymbolSelling = stockSymbolsSelling[random.nextInt(stockSymbolsSelling.length)];
        Stock stockToSell = findStockBySymbol(stocks, randomStockSymbolSelling);
        limitPrice = stockToSell.getPrice() / priceFactor;
        SellLimitOrderCommand sellLimitOrderCommand = new SellLimitOrderCommand(findTraderById(traders,
                "bot" + randomTrader),
                stockToSell, limitPrice, randomQuantitySell);
        commandHandler.registerCommand("SELL " + i, sellLimitOrderCommand);
        viewFactory.updateView();
    }
*/
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