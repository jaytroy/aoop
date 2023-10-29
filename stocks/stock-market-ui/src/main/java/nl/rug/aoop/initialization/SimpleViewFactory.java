package nl.rug.aoop.initialization;

import com.formdev.flatlaf.FlatDarculaLaf;
import nl.rug.aoop.uimodel.StockExchangeDataModel;
import nl.rug.aoop.simpleview.TerminalFrame;

import javax.swing.*;


/**
 * Creates a new SWING UI for the provided stock exchange.
 */
public class SimpleViewFactory implements AbstractViewFactory {
    private TerminalFrame terminalFrame; // Declare terminalFrame as an instance variable

    @Override
    public void createView(StockExchangeDataModel stockExchangeDataModel) {
        FlatDarculaLaf.setup();
        System.setProperty("sun.java2d.opengl", "true");
        SwingUtilities.invokeLater(() -> {
            terminalFrame = new TerminalFrame(stockExchangeDataModel); // Initialize terminalFrame
        });
    }

    public void updateView() {
        if (terminalFrame != null) {
            terminalFrame.updateView(); // You need to implement this method in your TerminalFrame
        }
    }
}
