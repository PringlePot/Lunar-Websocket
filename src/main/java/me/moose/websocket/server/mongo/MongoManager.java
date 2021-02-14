package me.moose.websocket.server.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import me.moose.websocket.server.WebServer;
import lombok.Getter;

@Getter
public class MongoManager {
    private final MongoClient client;
    private DB database;
    private DBCollection profileCollection;

    public MongoManager() {
        MongoClientURI uri = new MongoClientURI(
                "mongodb://GodUser:BitUWOga5aOePYU2@cluster0-shard-00-00.au0ai.mongodb.net:27017,cluster0-shard-00-01.au0ai.mongodb.net:27017,cluster0-shard-00-02.au0ai.mongodb.net:27017/WebSocket?ssl=true&replicaSet=atlas-bmrp0p-shard-0&authSource=admin&retryWrites=true&w=majority");
        this.client =new MongoClient(uri);

        try {
            this.database = this.client.getDB("CBWSDev");
            this.profileCollection = this.database.getCollection("profiles");
            WebServer.getInstance().getLogger().info("Loaded mongo successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
