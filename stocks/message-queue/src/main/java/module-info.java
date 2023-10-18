module messagequeue {
    exports nl.rug.aoop.messagequeue.queues;
    requires static lombok;
    requires com.google.gson;
    requires command;
    requires networking;
    requires org.slf4j;
    requires org.mockito;
}