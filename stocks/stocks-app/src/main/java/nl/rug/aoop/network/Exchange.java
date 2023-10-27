package nl.rug.aoop.network;

import lombok.Getter;
import nl.rug.aoop.model.Stock;
import nl.rug.aoop.model.Trader;
import nl.rug.aoop.model.TraderList;
import nl.rug.aoop.messagequeue.serverside.NetConsumer;
import nl.rug.aoop.messagequeue.serverside.TSMessageQueue;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.model.StockList;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * The exchange keeps track of all the stocks, traders, and it resolves orders.
 */
public class Exchange implements Runnable {
    private List<Client> connectedClients; //This can probably be removed to be replaced by listeners
    @Getter
    private List<Stock> stocks;
    @Getter
    private List<Trader> traders;
    private MessageQueue messageQueue;
    private NetConsumer consumer; //Replace this with interface? Throws an error. MQConsumer is not runnable.
    private List<ExchangeListener> listeners; //These will be traders, listening to any changes within the exchange. Observer pattern

    public Exchange(MessageQueue messageQueue) {
        connectedClients = new ArrayList<>();
        listeners = new ArrayList<>();

        stocks = initializeStocks();
        traders = initializeTraders();

        this.messageQueue = messageQueue; //Initialize the messagequeue. Will be run "locally" in the exchange
        consumer = new NetConsumer(messageQueue); //This thread will continuously poll messages
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        updateListeners();
    }

    @Override
    public void run() {

    }

    public List<Stock> initializeStocks() {
        try {
            YamlLoader stockLoader = new YamlLoader(Path.of("./data/stocks.yaml"));
            StockList stockList = stockLoader.load(StockList.class);
            return stockList.getStocks();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list. Why?
        }
    }

    public List<Trader> initializeTraders() {
        try {
            YamlLoader traderLoader = new YamlLoader(Path.of("./data/traders.yaml"));
            TraderList traderList = traderLoader.load(TraderList.class);
            return traderList.getTraders();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list
        }
    }

    //This is implemented in the observer pattern. Connected clients is useful tho. When a client disconnects, remove observer?
    public void trackConnectedClients() { //This will only run once? It needs to run always. Make exchange runnable?
        for (Client client : connectedClients) {
            if(client.isConnected()) {
                updateListeners();
            }
        }
    }

    public void updateListeners() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for(ExchangeListener listener : listeners) {
                    listener.update();
                }
            }
        }, 0, 1000);
    }

    public void addListener(ExchangeListener l) { //Call this when a client connects
        listeners.add(l);
    }


    private void sendStockInformation() { //We can do this a different way
        for (Client client : connectedClients) {
            String stockInfo = generateStockInformationForClient(client);
            client.sendMessage(stockInfo);
        }
    }

    private void sendTraderInformation() {
        for (Client client : connectedClients) {
            String traderInfo = generateTraderInformationForClient(client);
            client.sendMessage(traderInfo);
        }
    }

    private String generateStockInformationForClient(Client client) {
        StringBuilder stockInfo = new StringBuilder();
        for (Stock stock : stocks) {
            stockInfo.append(stock.getSymbol()).append(": ").append(stock.getPrice()).append("\n");
        }
        return stockInfo.toString();
    }

    private String generateTraderInformationForClient(Client client) {
        StringBuilder traderInfo = new StringBuilder();
        for (Trader traderUI : traders) {
            traderInfo.append(traderUI.getName()).append(": ").append(traderUI.getFunds()).append("\n");
        }
        return traderInfo.toString();
    }

    public void addClient(Client client) { //Replaced by addListener?
        connectedClients.add(client);
    }
}
