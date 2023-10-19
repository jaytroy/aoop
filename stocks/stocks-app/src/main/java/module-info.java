module stocksapp {
    requires static lombok;
    requires org.slf4j;
    requires java.desktop;
    requires com.formdev.flatlaf;
    requires networking;
    requires util;
    requires messagequeue;
    requires stock.market.ui;
    opens nl.rug.aoop.basic;   // Open the package for reflection
    requires com.fasterxml.jackson.annotation;
}