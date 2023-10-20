package nl.rug.aoop.network;

import nl.rug.aoop.basic.Stock;
import nl.rug.aoop.basic.StockList;
import nl.rug.aoop.basic.Trader;
import nl.rug.aoop.basic.TraderList;
import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.model.StockDataModel;
import nl.rug.aoop.model.TraderDataModel;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.networking.server.Server;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class StockApplication extends Server {
    private List<Client> connectedClients;
    private List<Stock> stocks; // Change to List
    private List<Trader> traders; // Change to List
    private MessageQueue messageQueue;

    public StockApplication(MessageQueue queue, MessageHandler messageHandler, int messageQueuePort) {
        super(messageHandler, messageQueuePort);
        this.messageQueue = queue;
        connectedClients = new ArrayList<>();
        stocks = new ArrayList<>(); // Initialize as ArrayList
        traders = new ArrayList<>(); // Initialize as ArrayList
    }

    public List<Stock> initializeStocks() {
        try {
            YamlLoader stockLoader = new YamlLoader(Path.of("./data/stocks.yaml"));
            StockList stockList = stockLoader.load(StockList.class);
            return stockList.getStocks();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>(); // Return an empty list
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

    public void startMessageQueue() {
        /*
        Thread messageQueueThread = new Thread(() -> {
            while (true) {
                Message message = messageQueue.dequeue();

                if (message != null) {
                    String messageJson = message.toJson(); //This should ideally be in the handler
                    super.getMsgHandler().handleMessage(messageJson);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        messageQueueThread.start();
        */

    }

    public void trackConnectedClients() {
        for (Client client : connectedClients) {
            if(client.isConnected()) {
                sendPeriodicUpdates();
            }
        }
    }

    public void sendPeriodicUpdates() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                sendStockInformation();
                sendTraderInformation();
            }
        }, 0, 1000);
    }


    private void sendStockInformation() {
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
        for (Trader trader : traders) {
            traderInfo.append(trader.getName()).append(": ").append(trader.getFunds()).append("\n");
        }
        return traderInfo.toString();
    }

    public void addClient(Client client) {
        connectedClients.add(client);
    }
}
