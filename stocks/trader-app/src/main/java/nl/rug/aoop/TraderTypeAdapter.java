package nl.rug.aoop;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.util.Map;

@Slf4j
public class TraderTypeAdapter extends TypeAdapter<Trader> {
    @Override
    public void write(JsonWriter out, Trader trader) throws IOException {
        out.beginObject();
        out.name("id").value(trader.getId());
        out.name("name").value(trader.getName());
        out.name("funds").value(trader.getFunds());
        out.name("ownedStocks").value(trader.getOwnedStocks().toString());
        out.endObject();
    }

    @Override
    public Trader read(JsonReader in) throws IOException {
        String traderId = "bot1";  // Default value, it will be overwritten by the actual ID
        Trader trader = new Trader(traderId, new InetSocketAddress("localhost", 8080));

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("id")) {
                traderId = in.nextString();
                trader.setId(traderId);
                trader.setAddress(getSocketAddressForTrader(traderId));
                logWithDelay("id: " + traderId);
            } else if (name.equals("name")) {
                String check = in.nextString();
                trader.setName(check);
                logWithDelay("Name: " + check);
            } else if (name.equals("funds")) {
                Double check = in.nextDouble();
                trader.setFunds(check);
                logWithDelay("Funds: " + check);
            } else if (name.equals("ownedStocks")) {
                String ownedStocksString = in.nextString();
                trader.setOwnedStocks(parseOwnedStocks(ownedStocksString));
                logWithDelay("Owned Stocks: " + ownedStocksString);
            }
        }
        in.endObject();

        return trader;
    }

    private void logWithDelay(String message) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            log.error("Thread sleep interrupted: " + e.getMessage());
        }
        log.info(message);
    }
    private InetSocketAddress getSocketAddressForTrader(String traderId) {
        int port = 8080;
        return new InetSocketAddress("localhost", port);
    }

    private Map<String, Integer> parseOwnedStocks(String ownedStocksString) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Integer>>(){}.getType();
        return gson.fromJson(ownedStocksString, mapType);
    }
}
