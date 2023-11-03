package nl.rug.aoop.model;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import nl.rug.aoop.model.Trader;

import java.io.IOException;

public class TraderTypeAdapter extends TypeAdapter<Trader> {

    @Override
    public void write(JsonWriter out, Trader trader) throws IOException {
        out.beginObject();
        out.name("id").value(trader.getId());
        // write other fields
        out.endObject();
    }

    @Override
    public Trader read(JsonReader in) throws IOException {
        Trader trader = new Trader();
        in.beginObject();
        while (in.hasNext()) {
            switch (in.nextName()) {
                case "id":
                    trader.setId(in.nextString());
                    break;
                // read other fields
            }
        }
        in.endObject();
        return trader;
    }
}
