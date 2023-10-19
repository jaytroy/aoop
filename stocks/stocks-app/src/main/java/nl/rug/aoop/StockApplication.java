package nl.rug.aoop;

import nl.rug.aoop.messagequeue.queues.MessageQueue;
import nl.rug.aoop.messagequeue.queues.Message;
import nl.rug.aoop.networking.MessageHandler;
import nl.rug.aoop.networking.client.Client;
import nl.rug.aoop.util.YamlLoader;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class StockApplication implements MessageHandler {
    private List<Client> connectedClients;
    private List<StockDataModel> stocks;
    private List<TraderDataModel> traders;
    private MessageQueue messageQueue;
    private int messageQueuePort;

    public StockApplication(MessageQueue queue) {
        this.messageQueue = queue;
        connectedClients = new ArrayList<>();
        stocks = new ArrayList<>();
        traders = new ArrayList();
        messageQueuePort = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));

        initializeStocks();
        initializeTraders();
    }

    private void initializeStocks() {
        try {
            YamlLoader stockLoader = new YamlLoader(Path.of("stocks/data/stocks.yaml"));
            stocks = (List<StockDataModel>) stockLoader.load(StockDataModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initializeTraders() {
        try {
            YamlLoader traderLoader = new YamlLoader(Path.of("stocks/data/traders.yaml"));
            traders = (List<TraderDataModel>) traderLoader.load(TraderDataModel.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startMessageQueue() {
        Thread messageQueueThread = new Thread(() -> {
            while (true) {
                Message message = messageQueue.dequeue();
                String messageJson = message.toJson();
                if (message != null) {
                    handleMessage(messageJson);
                }

                Thread.sleep(1000);
            }
        });
        messageQueueThread.start();
    }

    public void pollMessages() {
        while(true) {
            Message message = messageQueue.dequeue();
            String messageJson = message.toJson();
            if (message != null) {
                handleMessage(messageJson);
            }


        }
    }


    public void handleBuyOrder(Client client, String ticker, int quantity) {

    }

    public void handleSellOrder(Client client, String ticker, int quantity) {

    }

    public void trackConnectedClients() {
        for (Client client : connectedClients) {
            if(client.isConnected()) {
                //keeping track of connected clients, not sure what to put in ehre
            } else {
                //not connected.
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
        for (StockDataModel stock : stocks) {
            stockInfo.append(stock.getSymbol()).append(": ").append(stock.getPrice()).append("\n");
        }
        return stockInfo.toString();
    }

    private String generateTraderInformationForClient(Client client) {
        StringBuilder traderInfo = new StringBuilder();
        for (TraderDataModel trader : traders) {
            traderInfo.append(trader.getName()).append(": ").append(trader.getFunds()).append("\n");
        }
        return traderInfo.toString();
    }

    @Override
    public void handleMessage(String message) {

    }
}
