package nl.rug.aoop.simpleview;

import nl.rug.aoop.uimodel.StockExchangeDataModel;
import nl.rug.aoop.simpleview.tables.StockTableModel;
import nl.rug.aoop.simpleview.tables.StockTerminalTable;
import nl.rug.aoop.simpleview.tables.TraderTableModel;
import nl.rug.aoop.simpleview.tables.TraderTerminalTable;

import javax.swing.*;
import java.awt.*;

/**
 * A custom frame for the view of the stocks.
 */
public class TerminalFrame extends JFrame {
    private JTable stockTable; // Declare stockTable as an instance variable
    private JTable traderTable; // Declare traderTable as an instance variable

    /**
     * The width of the frame.
     */
    public static final int WIDTH = 1600;
    /**
     * The height of the frame.
     */
    public static final int HEIGHT = 1100;

    /**
     * Creates a new frame for the stock terminal view.
     *
     * @param stockExchange A data model of the stock exchange.
     */
    public TerminalFrame(StockExchangeDataModel stockExchange) {
        super("Bloomberg Terminal 2.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        stockTable = new StockTerminalTable(new StockTableModel(stockExchange)); // Initialize stockTable
        JScrollPane stockTableScrollPane = new JScrollPane(stockTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        traderTable = new TraderTerminalTable(new TraderTableModel(stockExchange)); // Initialize traderTable
        JScrollPane traderTableScrollPane = new JScrollPane(traderTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        JSplitPane topBottomSplit = new JSplitPane(
                JSplitPane.VERTICAL_SPLIT,
                stockTableScrollPane,
                traderTableScrollPane);
        topBottomSplit.setDividerLocation(HEIGHT / 2);
        add(topBottomSplit);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Update the view with new data.
     */
    public void updateView() {
        ((StockTableModel) stockTable.getModel()).fireTableDataChanged();
        ((TraderTableModel) traderTable.getModel()).fireTableDataChanged();
    }
}
