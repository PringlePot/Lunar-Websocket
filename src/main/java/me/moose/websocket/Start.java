package me.moose.websocket;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.utils.Config;

import java.net.InetSocketAddress;

public class Start {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("\nMongo: " +
                "\n User: " + Config.Mongo.USERNAME +
                "\n Host: " + Config.Mongo.HOST +
                "\n Port: " + Config.Mongo.PORT +
                "\n Ping: 0ms");
        System.out.println("\nRedis: " +
                "\n Host: " + Config.Redis.HOST +
                "\n Port: " + Config.Redis.PORT +
                "\n DB Id: " + Config.Redis.DBID +
                "\n UUID Cache: " + Config.Redis.UUIDCACHE +
                "\n Ping: 1ms");
        System.out.println("\nStarting on port 80!");
        System.out.println("Setting Databases!");
        Thread.sleep(1250);
       new WebServer(new InetSocketAddress("0.0.0.0", 80)).run();
       WebServer.getInstance().stop();
       WebServer.getInstance().stop();
       System.out.println("Shutting down");
       System.exit(0);
    }
}
