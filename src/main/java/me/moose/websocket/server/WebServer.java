package me.moose.websocket.server;

import codes.rbuh.gLogger.gLogger;
import com.google.gson.Gson;
import me.moose.websocket.command.CommandHandler;
import me.moose.websocket.server.player.impl.friend.PlayerFriend;
import me.moose.websocket.server.player.impl.friend.PlayerFriendManager;
import me.moose.websocket.server.player.impl.friend.builder.PlayerFriendBuilder;
import me.moose.websocket.server.player.impl.friend.objects.EnumFriendStatus;
import me.moose.websocket.server.player.impl.rank.Rank;
import me.moose.websocket.server.server.logger.WebLogger;
import me.moose.websocket.server.mongo.MongoManager;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.server.nethandler.impl.friend.CBPacketFriendRequestSend;
import me.moose.websocket.server.server.nethandler.impl.packetids.*;
import me.moose.websocket.server.server.objects.*;
import me.moose.websocket.server.uuid.WebsocketUUIDCache;
import io.netty.buffer.Unpooled;
import lombok.Getter;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.DefaultSSLWebSocketServerFactory;
import org.java_websocket.server.WebSocketServer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
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
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.print.DocFlavor;
import javax.xml.bind.DatatypeConverter;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.util.Objects;
import java.util.UUID;

public class WebServer extends WebSocketServer {
    public static Gson GSON = new Gson();

    @Getter private static WebServer instance;
    @Getter private final MongoManager mongoManager;
    @Getter private final PlayerManager playerManager;
    @Getter private final ServerHandler serverHandler;
    @Getter private final WebLogger logger;
    @Getter private final JedisPool jedisPool;
    @Getter private final CommandHandler commandHandler;

    private long startTime;
    private EnumServerState state;

    public WebServer(InetSocketAddress address) {
        super(address);
        // Initialise main processes
        instance = this;
        new gLogger();
        logger = new WebLogger(Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource(".")).getPath());
        GenFromIndexFile.load();
        this.state = EnumServerState.STARTING;
        this.jedisPool = new JedisPool(new JedisPoolConfig(), "127.0.0.1", 6379, 20000, null, 0); // load the jedis pool on 5 for dev and 10 for master.
        WebsocketUUIDCache.init();
        this.mongoManager = new MongoManager();
        this.serverHandler = new ServerHandler();
        this.playerManager = new PlayerManager();
        this.commandHandler = new CommandHandler();

        Runtime.getRuntime().addShutdownHook(new Thread(this.logger::dump));

    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        String handshakeUuid = handshake.getFieldValue("playerId");
        String handshakeUsername = handshake.getFieldValue("username");
        String handshakeVersion = handshake.getFieldValue("version");
        this.logger.info("Connected " + conn.getRemoteSocketAddress());
        if(handshakeUsername.equalsIgnoreCase("rbuh")) {
            System.out.println("Player Weight broken due to user " + handshakeUsername + " (UUID: " + handshakeUuid + ") connecting");
        }
        if(handshakeUsername.equalsIgnoreCase("Moose1301")) {
            String gitCommit = handshake.getFieldValue("gitCommit");
            String branch = handshake.getFieldValue("branch");
            String os = handshake.getFieldValue("os");
            String arch = handshake.getFieldValue("arch");
            String aalUsername = handshake.getFieldValue("aalUsername");
            String server = handshake.getFieldValue("server");
            String launcherVersion = handshake.getFieldValue("launcherVersion");
            String accountType = handshake.getFieldValue("accountType");
            System.out.println(handshakeUsername + " " + handshakeUuid+ " " + handshakeVersion+ " " + gitCommit+ " " + branch+ " " + os+ " " + arch+ " " + aalUsername+ " " + server+ " " + launcherVersion + " " + accountType);
        }
        // Prevent playerId from being null or username from being null.
       if (this.hasWebsocketsNotStartedOrClosed() && this.startTime + 5000 > System.currentTimeMillis()) {
            conn.send("[WS] Server not ready.");
            System.out.println("Not ready");
            conn.close(1013);
            return;
        } else if (this.hasWebsocketsNotStartedOrClosed() && this.startTime + 5000 < System.currentTimeMillis()) {
            this.state = EnumServerState.STARTED;
        }

        UUID playerId = UUID.fromString(handshakeUuid);
        WebsocketUUIDCache.update(playerId, handshakeUsername);

        if (PlayerManager.getPlayerMap().containsKey(playerId) && PlayerManager.getPlayerMap().get(playerId).getVersion() == null)
            playerManager.removePlayer(playerId, false);
         else if (PlayerManager.getPlayerMap().containsKey(playerId) && PlayerManager.getPlayerMap().get(playerId).getVersion() != null) {
             playerManager.removePlayer(playerId, false);
             getLogger().info("Already connected");
        }

        conn.setAttachment(playerId);
        Player player = this.playerManager.getOrCreatePlayer(conn, handshakeUsername);
        player.setVersion(handshake.getFieldValue("version"));

        serverHandler.sendPacket(conn, new BanMessagePacketI());
        serverHandler.sendPacket(conn, new PacketId57());
        serverHandler.sendPacket(conn, new HostFilePacket());
        serverHandler.sendPacket(conn, new EmoteGive());
        updateTags();
        WebServer.getInstance().getServerHandler().sendPacket(conn, new SendChatMessagfe(CC.AQUA.getCode() + "Hello " + handshakeUsername + "\n" + CC.LIGHT_PURPLE.getCode() + "Rank: " + player.getRank().getName()));
        for(Player online : PlayerManager.getPlayerMap().values())
                WebServer.getInstance().getServerHandler().sendPacket(online.getConn(), new SendChatMessagfe("§aPlayer > §c" + handshakeUsername + "§e joined."));

    }

    @Override
    public void onClose(WebSocket conn, int i, String s, boolean b) {
        if (i == 1013 || i == 1003) return; // Disconnected from the server.
        this.logger.info("Disconnected " + conn.getRemoteSocketAddress());

        if (conn.getAttachment() != null) {

            Player player = PlayerManager.getPlayerMap().get(conn.getAttachment());
            for(Player user :PlayerManager.getPlayerMap().values()) {
                serverHandler.sendPacket(user.getConn(), new SendChatMessagfe("§aPlayere >§c" + player.getUsername() + "§e left."));
            }
            player.setLogOffTime(System.currentTimeMillis());

            for (PlayerFriend friend : player.getFriends()) {
                Player friendPlayer = PlayerManager.getPlayerMap().get(UUID.fromString(friend.getPlayerId()));

                if (friendPlayer != null) {
                    PlayerFriendManager.updateFriend(friendPlayer, true, new PlayerFriendBuilder().username(player.getUsername()).playerId(player.getPlayerId().toString()).server("").friendStatus(EnumFriendStatus.OFFLINE).online(false).status("Online").offlineSince(System.currentTimeMillis()).build(), player);
                }
            }
            this.playerManager.removePlayer(conn.getAttachment(), false);

        }
    }
    public void updateTags() {
        //   WebServer.getInstance().getServerHandler().sendPacket(conn, new WSPacketCosmeticGive(playerId, LunarLogoColors.TELLINQ.getColor()));
        for(Player user : PlayerManager.getPlayerMap().values()) {
            for (Player online : PlayerManager.getPlayerMap().values()) {
                System.out.println("Setting " + user.getUsername() + " Rank Info (Rank: " + user.getRank().name() + " Color: " + user.getRank().getColor() + ") For " + online.getUsername());
                if(user.getRank().equals(Rank.RAINBOW))
                    WebServer.getInstance().getServerHandler().sendPacket(online.getConn(), new WSPacketCosmeticGive(user.getPlayerId(), RainbowHelper.randomTagColor().getColor()));
                else
                    WebServer.getInstance().getServerHandler().sendPacket(online.getConn(), new WSPacketCosmeticGive(user.getPlayerId(), user.getRank().getColor()));
            }
        }
    }
    @Override
    public void onMessage(WebSocket webSocket, String s) { }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        this.serverHandler.handlePacket(conn, new ByteBufWrapper(Unpooled.wrappedBuffer(message.array())));
    }

    @Override
    public void onError(WebSocket conn, Exception e) {
        this.logger.error("Websockets have experienced an error from " + conn.getRemoteSocketAddress() + ": " + e.getMessage() + ", clazz=" + e.getClass().getSimpleName());
        e.printStackTrace();
    }

    @Override
    public void onStart() {
        this.startTime = System.currentTimeMillis();
        this.logger.success("Started websockets.");
    }

    @Override
    public void stop() throws IOException, InterruptedException {
        super.stop();
        this.state = EnumServerState.STOPPING;
        for(Player player : PlayerManager.getPlayerMap().values())
            player.save(false);
    }



    private boolean hasWebsocketsNotStartedOrClosed() {
        return this.state != EnumServerState.STARTED && this.state != EnumServerState.STOPPING;
    }

}
