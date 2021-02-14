package me.moose.websocket;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.server.nethandler.object.ChangeLoglThread;
import me.moose.websocket.server.server.nethandler.object.UpdateTagThread;
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

public class Start {
    public static void main(String[] args) {

        WebSocketServer server = new WebServer(new InetSocketAddress("0.0.0.0", Integer.parseInt(args[0])));
   /*     SSLContext context = null;
        try {
            context = getContextJKS();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (context != null) {

            server.setWebSocketFactory(new DefaultSSLWebSocketServerFactory(getContext()));
        } */
        server.setConnectionLostTimeout(0);
        server.run();
    }
    public static SSLContext getContextJKS() throws Exception {
        String STORETYPE = "JKS";
        String KEYSTORE = "E:\\SSL";

        String STOREPASSWORD = "mooshac";
        String KEYPASSWORD = "mooshac";

        KeyStore ks = KeyStore.getInstance(STORETYPE);
        File kf = new File(KEYSTORE);
        ks.load(new FileInputStream(kf), STOREPASSWORD.toCharArray());

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, KEYPASSWORD.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);

        SSLContext sslContext = null;
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return sslContext;
    }
    private static SSLContext getContext() {
        SSLContext context;
        String password = "";
        String pathname = "C:\\Users\\angel\\Desktop\\SSL";
        try {
            context = SSLContext.getInstance("TLS");
            System.out.println("Loading Certificates!");
            byte[] certBytes = parseDERFromPEM(getBytes(new File(pathname + File.separator + "public.pem")),
                    "-----BEGIN CERTIFICATE-----", "-----END CERTIFICATE-----");
            System.out.println("Loaded Public Certificate");
            byte[] keyBytes = parseDERFromPEM(
                    getBytes(new File(pathname + File.separator + "private.pem")),
                    "-----BEGIN RSA PRIVATE KEY-----", "-----END RSA PRIVATE KEY-----");
            System.out.println("Loaded Private Certificate");

            System.out.println("Loaded Certificates");

            X509Certificate cert = generateCertificateFromDER(certBytes);
            RSAPrivateKey key = generatePrivateKeyFromDER(keyBytes);

            KeyStore keystore = KeyStore.getInstance("JKS");
            keystore.load(null);
            keystore.setCertificateEntry("cert-alias", cert);
            keystore.setKeyEntry("key-alias", key, password.toCharArray(), new Certificate[]{cert});

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(keystore, password.toCharArray());

            KeyManager[] km = kmf.getKeyManagers();

            context.init(km, null, null);
        } catch (Exception e) {
            context = null;
        }
        return context;
    }

    private static byte[] parseDERFromPEM(byte[] pem, String beginDelimiter, String endDelimiter) {
        String data = new String(pem);
        String[] tokens = data.split(beginDelimiter);
        tokens = tokens[1].split(endDelimiter);
        return DatatypeConverter.parseBase64Binary(tokens[0]);
    }

    private static RSAPrivateKey generatePrivateKeyFromDER(byte[] keyBytes)
            throws InvalidKeySpecException, NoSuchAlgorithmException {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory factory = KeyFactory.getInstance("RSA");

        return (RSAPrivateKey) factory.generatePrivate(spec);
    }

    private static X509Certificate generateCertificateFromDER(byte[] certBytes)
            throws CertificateException {
        CertificateFactory factory = CertificateFactory.getInstance("X.509");

        return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(certBytes));
    }

    private static byte[] getBytes(File file) {
        byte[] bytesArray = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fis.read(bytesArray); //read file into bytes[]
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytesArray;
    }
}
