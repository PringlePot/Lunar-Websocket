package me.moose.websocket;

import me.moose.websocket.command.Command;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.server.nethandler.object.AdvertiseThread;
import me.moose.websocket.server.server.nethandler.object.SaveThread;
import me.moose.websocket.utils.Commands;
import me.moose.websocket.utils.Config;
import org.java_websocket.server.WebSocketServer;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Random;

public class Start {
    public static void main(String[] args) {
        System.out.println("\nStarted Save Thread");
        new SaveThread().start();
        System.out.println("\nStarted Advertise Thread");

        new AdvertiseThread().start();
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
       new WebServer(new InetSocketAddress("0.0.0.0", 80)).run();
       System.out.println("Shutting down");
       System.exit(0);
    }
}
