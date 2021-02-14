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
        this.client = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));

        try {
            this.database = this.client.getDB("CBWSDev");
            this.profileCollection = this.database.getCollection("profiles");
            WebServer.getInstance().getLogger().info("Loaded mongo successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
