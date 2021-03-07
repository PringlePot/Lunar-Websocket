package me.moose.websocket.server.mongo;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import me.moose.websocket.server.WebServer;
import lombok.Getter;
import me.moose.websocket.utils.Config;

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
