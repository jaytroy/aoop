package nl.rug.aoop;

import lombok.extern.slf4j.Slf4j;

/**
 * The `JayMain` class represents the entry point for the Jay Trader application in the stock exchange system.
 */
@Slf4j
public class TraderMain {
    /**
     * The main method that initializes the Stock App view and components.
     *
     * @param args the main args.
     */
    public static void main( String[] args ) {
        TraderApp app = new TraderApp();
        app.initialize();
    }
}
