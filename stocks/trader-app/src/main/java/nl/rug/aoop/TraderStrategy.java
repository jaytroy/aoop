package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

import static nl.rug.aoop.actions.Order.Type.BUY;
import static nl.rug.aoop.actions.Order.Type.SELL;

/**
 * The TraderStrategy class represents the strategy for generating random orders for a trader.
 */
@Slf4j
public class TraderStrategy {
    private Trader trader;

    public TraderStrategy(Trader trader) {
        this.trader = trader;
    }

    /**
     * Implement a trader strategy for generating random orders.
     */
    public void executeStrategy() {
        Random random = new Random();

        // For buying, get all possible stock symbols from available stocks
        List<String> stockSymbolsToBuy = trader.getAvailableStocks().stream().map(Stock::getSymbol).toList();

        // For selling, get stock symbols from the symbols of owned stocks
        List<String> stockSymbolsToSell = trader.getOwnedStocks().keySet().stream().toList();

        int randomQuantityBuy = random.nextInt(100) + 1;
        double priceFactor = 1.0 + (0.01 * random.nextDouble());

        // For buying, randomly choose a stock symbol from available stocks
        String randomStockSymbolBuy = stockSymbolsToBuy.get(random.nextInt(stockSymbolsToBuy.size()));

        // For selling, randomly choose a stock symbol from owned stocks
        String randomStockSymbolSell = stockSymbolsToSell.get(random.nextInt(stockSymbolsToSell.size()));

        // Choose the correct stock price from available stocks
        Stock chosenStock = trader.getAvailableStocks().stream()
                .filter(stock -> stock.getSymbol().equals(randomStockSymbolBuy))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Stock not found for symbol: " + randomStockSymbolBuy));

        double price = chosenStock.getPrice();

        double limitPriceBuy = price * priceFactor;
        double limitPriceSell = price / priceFactor;

        int buyOrSell = random.nextInt(2);

        if (buyOrSell == 1 && trader.getAvailableFunds() > 0) {
            trader.placeOrder(BUY, randomStockSymbolBuy, randomQuantityBuy, limitPriceBuy);
        } else {
            trader.placeOrder(SELL, randomStockSymbolSell, randomQuantityBuy, limitPriceSell);
        }
    }
}
