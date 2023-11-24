module stocksapp {
    exports nl.rug.aoop;
    exports nl.rug.aoop.model;
    requires static lombok;
    requires org.slf4j;
    requires java.desktop;
    requires networking;
    requires util;
    requires messagequeue;
    requires com.fasterxml.jackson.databind;
    requires command;
    requires stock.market.ui;
    requires com.google.gson;
    opens nl.rug.aoop.model to com.fasterxml.jackson.databind, com.google.gson;
    opens nl.rug.aoop.actions to com.google.gson;
    exports nl.rug.aoop.model.typeadapters;
    opens nl.rug.aoop.model.typeadapters to com.fasterxml.jackson.databind;
    exports nl.rug.aoop.model.components;
    opens nl.rug.aoop.model.components to com.fasterxml.jackson.databind, com.google.gson;
}