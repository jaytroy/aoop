module stocksapp {
    exports nl.rug.aoop;
    exports nl.rug.aoop.model;
    requires static lombok;
    requires org.slf4j;
    requires java.desktop;
    //requires com.formdev.flatlaf;
    requires networking;
    requires util;
    requires messagequeue;
    requires com.fasterxml.jackson.databind;
    requires command;
    opens nl.rug.aoop.model to com.fasterxml.jackson.databind;
}