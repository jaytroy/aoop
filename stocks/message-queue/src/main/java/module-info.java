module messagequeue {
    exports nl.rug.aoop.messagequeue.queues;
    exports nl.rug.aoop.messagequeue.serverside;
    exports nl.rug.aoop.messagequeue.consumer to stocksapp;
    exports nl.rug.aoop.messagequeue.serverside.commands;
    requires static lombok;
    requires com.google.gson;
    requires command;
    requires networking;
    requires org.slf4j;
    requires org.mockito;
    opens nl.rug.aoop.messagequeue.queues to com.google.gson;
}