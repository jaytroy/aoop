package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;
import java.net.InetSocketAddress;

@Slf4j
public class TraderApp {
    public void initialize() {
        Trader trader1 = new Trader("bot1",getAdd());
        Trader trader2 = new Trader("bot2",getAdd());
        Trader trader3 = new Trader("bot3",getAdd());


        //trader2.placeOrder(SELL, "AMD", 10, 900);
        //trader1.placeOrder(BUY, "AMD", 100, 950);
        //trader3.placeOrder(SELL, "AMD", 50, 800);
        //trader2.placeOrder(BUY, "AMD", 40, 850);
    }

    public InetSocketAddress getAdd() {
        int port;
        int BACKUP_PORT = 8080;
        InetSocketAddress address;
        if(System.getenv("MESSAGE_QUEUE_PORT") != null) {
            port = Integer.parseInt(System.getenv("MESSAGE_QUEUE_PORT"));
        } else {
            port = BACKUP_PORT;
            System.out.println("Using backup port at TraderAppMain");
        }
        return new InetSocketAddress("localhost", port);
    }

    /*
    private static void BuyAndSellStrat(Trader trader1) {
        Random random = new Random();
        String[] stockSymbols = {"AAPL", "TSLA", "AMZN", "MSFT", "NVDA", "AMD", "ADBE", "FB", "INTC", "AOOP", "MRNA"};

        int numOrders = 10;
        for (int i = 0; i < numOrders; i++) {
            String randomStockSymbol = stockSymbols[random.nextInt(stockSymbols.length)];
            int randomQuantityBuy = random.nextInt(100) + 1;;
            double priceFactor = 1.0 + (0.01 * random.nextDouble());
            double limitPriceBuy = stock.getPrice() * priceFactor;
            double limitPriceSell = stock.getPrice() / priceFactor;
            int buyOrSell = random.nextInt(2);

            if (buyOrSell == 1) {
                trader1.placeOrder(BUY, randomStockSymbol, randomQuantityBuy, limitPriceBuy);
            } else {
                trader1.placeOrder(SELL, randomStockSymbol,randomQuantityBuy, limitPriceSell);
            }
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                log.error("Thread failed to sleep");
            }
        }
    }
     */
}

