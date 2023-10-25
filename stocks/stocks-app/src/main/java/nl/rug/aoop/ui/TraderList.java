package nl.rug.aoop.ui;

import java.util.List;

public class TraderList {
    private List<TraderUI> traderUIS;

    public List<TraderUI> getTraders() {
        return traderUIS;
    }

    public void setTraders(List<TraderUI> traderUIS) {
        this.traderUIS = traderUIS;
    }
}