module stock.market.ui {
    requires static lombok;
    exports nl.rug.aoop.uimodel;
    exports nl.rug.aoop.initialization;
    exports nl.rug.aoop.simpleview;
    requires org.slf4j;
    requires java.desktop;
    requires com.formdev.flatlaf;
    requires networking;
    requires messagequeue;
}