module messagequeue {
    requires static lombok;
    requires com.google.gson;
    exports nl.rug.aoop.messagequeue.queues to networking;
    exports nl.rug.aoop.messagequeue.consumer to networking;
    exports nl.rug.aoop.messagequeue.producer to networking;
}