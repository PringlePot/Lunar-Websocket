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
}
