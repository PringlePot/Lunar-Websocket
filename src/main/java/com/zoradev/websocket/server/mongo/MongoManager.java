package com.zoradev.websocket.server.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.utils.Config;
import lombok.Getter;

@Getter
public class MongoManager {
    private final MongoClient client;
    private DB database;
    private DBCollection profileCollection;

    public MongoManager() {

        this.client =new MongoClient(Config.Mongo.HOST, Integer.parseInt(Config.Mongo.PORT));

        try {
            this.database = this.client.getDB(Config.Mongo.DB);
            this.profileCollection = this.database.getCollection("profiles");
            WebServer.getInstance().getLogger().info("Loaded mongo successfully.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
