package nl.rug.aoop.model.components;

import java.util.List;

/**
 * The TraderList class represents a list of traders in the trading system.
 */
public class TraderList {
    private List<Trader> traders;

    /**
     * Get the list of traders.
     *
     * @return The list of traders.
     */
    public List<Trader> getTraders() {
        return traders;
    }

    /**
     * Set the list of traders.
     *
     * @param traders The list of traders to set.
     */
    public void setTraders(List<Trader> traders) {
        this.traders = traders;
    }
}
