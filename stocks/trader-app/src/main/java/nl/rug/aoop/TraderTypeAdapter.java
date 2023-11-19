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
    private String id;

    public TraderTypeAdapter(String id) {
        this.id = id;
    }

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
        String traderId = id;
        Trader trader = new Trader(traderId, new InetSocketAddress("localhost", 8080));

        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("id")) {
                traderId = in.nextString();
                trader.setId(traderId);
                trader.setAddress(getSocketAddressForTrader(traderId));
            } else if (name.equals("name")) {
                String check = in.nextString();
                trader.setName(check);
            } else if (name.equals("funds")) {
                Double check = in.nextDouble();
                trader.setFunds(check);
            } else if (name.equals("ownedStocks")) {
                String ownedStocksString = in.nextString();
                trader.setOwnedStocks(parseOwnedStocks(ownedStocksString));
            }
        }
        in.endObject();

        return trader;
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
