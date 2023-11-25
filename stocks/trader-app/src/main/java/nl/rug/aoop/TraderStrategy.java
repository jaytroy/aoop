package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

import static nl.rug.aoop.actions.Order.Action.BUY;
import static nl.rug.aoop.actions.Order.Action.SELL;
import static nl.rug.aoop.actions.Order.Type.LIMIT;
import static nl.rug.aoop.actions.Order.Type.MARKET;

/**
 * The TraderStrategy class represents the strategy for generating random orders for a trader.
 */
@Slf4j
public class TraderStrategy {
    private TraderFacade traderFacade;

    /**
     * Implements a trader strategy on a trader.
     *
     * @param traderFacade the trader that will have a strategy implemented on them.
     */

    public TraderStrategy(TraderFacade traderFacade) {
        this.traderFacade = traderFacade;
    }

    /**
     * Implement a trader strategy for generating random orders.
     */
    public void executeStrategy() {
        Random random = new Random();
        int marketOrLimit = random.nextInt(2);
        if(marketOrLimit == 0) {
            buySellMarketStrategy();
        } else {
            buySellStrategy();
        }
        //more strategies here as needed
    }

    private void buySellMarketStrategy() {
        Random random = new Random();
        // For buying, get all possible stock symbols from available stocks
        List<String> stockSymbolsToBuy = traderFacade.getTrader().getAvailableStocks().stream().map(Stock::getSymbol).
                toList();
        // For selling, get stock symbols from the symbols of owned stocks
        List<String> stockSymbolsToSell = traderFacade.getTrader().getOwnedStocks().keySet().stream().toList();
        // For buying, randomly choose a stock symbol from available stocks
        String randomStockSymbolBuy = stockSymbolsToBuy.get(random.nextInt(stockSymbolsToBuy.size()));
        // For selling, randomly choose a stock symbol from owned stocks
        String randomStockSymbolSell = stockSymbolsToSell.get(random.nextInt(stockSymbolsToSell.size()));
        // Choose the correct stock price from available stocks
        Stock chosenStock = traderFacade.getTrader().getAvailableStocks().stream()
                .filter(stock -> stock.getSymbol().equals(randomStockSymbolBuy))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Stock not found for symbol: " + randomStockSymbolBuy));
        int maxQuantitySell = traderFacade.getTrader().getOwnedStocks().get(randomStockSymbolSell);
        int randomQuantitySell = random.nextInt(maxQuantitySell) + 1;
        int randomQuantityBuy = random.nextInt(100) + 1;
        double price = chosenStock.getPrice();
        int buyOrSell = random.nextInt(2);
        if (buyOrSell == 1 && traderFacade.getTrader().getAvailableFunds() > 0) {
            traderFacade.placeOrder(BUY, MARKET, randomStockSymbolBuy, randomQuantityBuy, price);
        } else {
            traderFacade.placeOrder(SELL, MARKET, randomStockSymbolSell, randomQuantitySell, price);
        }
    }

    private void buySellStrategy() {
        Random random = new Random();
        // For buying, get all possible stock symbols from available stocks
        List<String> stockSymbolsToBuy = traderFacade.getTrader().getAvailableStocks().stream().map(Stock::getSymbol).
                toList();
        // For selling, get stock symbols from the symbols of owned stocks
        List<String> stockSymbolsToSell = traderFacade.getTrader().getOwnedStocks().keySet().stream().toList();
        double priceFactor = 1.0 + (0.01 * random.nextDouble());
        // For buying, randomly choose a stock symbol from available stocks
        String randomStockSymbolBuy = stockSymbolsToBuy.get(random.nextInt(stockSymbolsToBuy.size()));
        // For selling, randomly choose a stock symbol from owned stocks
        String randomStockSymbolSell = stockSymbolsToSell.get(random.nextInt(stockSymbolsToSell.size()));
        // Choose the correct stock price from available stocks
        Stock chosenStock = traderFacade.getTrader().getAvailableStocks().stream()
                .filter(stock -> stock.getSymbol().equals(randomStockSymbolBuy))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Stock not found for symbol: " + randomStockSymbolBuy));
        int maxQuantitySell = traderFacade.getTrader().getOwnedStocks().get(randomStockSymbolSell);
        int randomQuantitySell = random.nextInt(maxQuantitySell) + 1;
        int randomQuantityBuy = random.nextInt(100) + 1;
        double price = chosenStock.getPrice();
        double limitPriceBuy = price * priceFactor;
        double limitPriceSell = price / priceFactor;
        int buyOrSell = random.nextInt(2);
        //Randomly choose between LIMIT and MARKET
        if (buyOrSell == 1 && traderFacade.getTrader().getAvailableFunds() > 0) {
            traderFacade.placeOrder(BUY, LIMIT, randomStockSymbolBuy, randomQuantityBuy, limitPriceBuy);
        } else {
            traderFacade.placeOrder(SELL, LIMIT, randomStockSymbolSell, randomQuantitySell, limitPriceSell);
        }
    }
}
