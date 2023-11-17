package nl.rug.aoop.model;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;

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
        Trader trader = new Trader();
        in.beginObject();
        while (in.hasNext()) {
            String name = in.nextName();
            if (name.equals("id")) {
                trader.setId(in.nextString());
            } else if (name.equals("name")) {
                trader.setName(in.nextString());
            } else if (name.equals("funds")) {
                trader.setFunds(in.nextDouble());
            } else if (name.equals("ownedStocks")) {
                trader.setOwnedStocks(parseOwnedStocks(in.nextString()));
            }
        }
        in.endObject();
        return trader;
    }

    private Map<String, Integer> parseOwnedStocks(String ownedStocksString) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, Integer>>(){}.getType();
        return gson.fromJson(ownedStocksString, mapType);
    }
}
