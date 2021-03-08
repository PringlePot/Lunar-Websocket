package me.moose.websocket.utils;

import java.io.File;

public class Config {
    public static int port;
    public static class Mongo {
        public static String HOST = "localhost";
        public static String PORT = "27017";
        public static String DB = "WebSocket";
        public static boolean AUTH = false;
        public static String USERNAME = "";
        public static String PASSWORD = "";
    }
    public static class Redis {
        public static String HOST = "localhost";
        public static int PORT = 6379;
        public static int DBID = 0;
        public static String UUIDCACHE = "UUIDCACHE";
    }


    public static String getTldString(String urlString) {
        String tldString = null;

        String[] domainNameParts = urlString.split("\\.");
        if(domainNameParts.length < 2) {
            return urlString;
        }
        tldString = domainNameParts[domainNameParts.length-2] + "." + domainNameParts[domainNameParts.length-1];
        return tldString;
    }
}
