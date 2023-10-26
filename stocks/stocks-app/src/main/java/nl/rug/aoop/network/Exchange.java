package nl.rug.aoop.network;

import nl.rug.aoop.messagequeue.serverside.NetConsumer;
import nl.rug.aoop.messagequeue.serverside.TSMessageQueue;
import nl.rug.aoop.ui.StockUI;
import nl.rug.aoop.ui.StockList;
import nl.rug.aoop.ui.TraderUI;
import nl.rug.aoop.ui.TraderList;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

/**
 * The exchange keeps track of all the stocks, traders, and it resolves orders.
 */
public class Exchange implements Runnable {
    private List<Client> connectedClients; //This can probably be removed to be replaced by listeners
    private List<StockUI> stocks; // Change to List
    private List<TraderUI> traders; // Change to List
    private MessageQueue messageQueue;
    private NetConsumer consumer; //Replace this with interface? Throws an error. MQConsumer is not runnable.
    private List<ExchangeListener> listeners; //These will be traders, listening to any changes within the exchange. Observer pattern

    public Exchange() {
        connectedClients = new ArrayList<>();
        listeners = new ArrayList<>();
        stocks = new ArrayList<>(); // Initialize as ArrayList
        traders = new ArrayList<>(); // Initialize as ArrayList

        this.messageQueue = new TSMessageQueue(); //Initialize the messagequeue. Will be run "locally" in the exchange
        consumer = new NetConsumer(messageQueue); //This thread will continuously poll messages
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();
    }

    @Override
    public void run() {

    }

    public List<StockUI> initializeStocks() {
        try {
            YamlLoader stockLoader = new YamlLoader(Path.of("./data/stocks.yaml"));
            StockList stockList = stockLoader.load(StockList.class);
            return stockList.getStocks();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list. Why?
        }
    }

    public List<TraderUI> initializeTraders() {
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
        for (StockUI stock : stocks) {
            stockInfo.append(stock.getSymbol()).append(": ").append(stock.getPrice()).append("\n");
        }
        return stockInfo.toString();
    }

    private String generateTraderInformationForClient(Client client) {
        StringBuilder traderInfo = new StringBuilder();
        for (TraderUI traderUI : traders) {
            traderInfo.append(traderUI.getName()).append(": ").append(traderUI.getFunds()).append("\n");
        }
        return traderInfo.toString();
    }

    public void addClient(Client client) { //Replaced by addListener?
        connectedClients.add(client);
    }
}
