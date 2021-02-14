package me.moose.websocket.server.player.impl;

import com.google.common.collect.Maps;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.model.DBCollectionUpdateOptions;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.cosmetics.CosmeticType;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.friend.PlayerFriend;
import me.moose.websocket.server.player.impl.friend.PlayerFriendManager;
import me.moose.websocket.server.player.impl.friend.PlayerFriendRequest;
import me.moose.websocket.server.player.impl.rank.Rank;
import me.moose.websocket.server.server.logger.WebLogger;
import me.moose.websocket.server.player.impl.friend.objects.EnumFriendStatus;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import lombok.Getter;
import lombok.Setter;
import me.moose.websocket.server.server.nethandler.impl.friend.CBPacketFriendListUpdate;
import me.moose.websocket.server.server.nethandler.impl.packetids.SendChatMessagfe;
import me.moose.websocket.server.server.nethandler.impl.packetids.WSPacketCosmeticGive;
import me.moose.websocket.server.server.objects.CC;
import me.moose.websocket.server.server.objects.LunarLogoColors;
import org.java_websocket.WebSocket;

import java.beans.ConstructorProperties;
import java.util.*;

@Getter @Setter @SuppressWarnings("unchecked")
public class Player {
    private UUID playerId;
    private String username;

    private WebLogger logger = WebServer.getInstance().getLogger();

    // Non Persist
    private long lastMessageSent;
    private String version;
    private EnumFriendStatus friendStatus;
    private String server;
    private WebSocket conn;

    // Log off
    private long logOffTime;

    // Friend Persist
    private List<PlayerFriend> friends;
    private List<PlayerFriendRequest> sentFriendRequests;
    private List<PlayerFriendRequest> receivedFriendRequests;
    private Map<String, List<Object>> onlineFriends = new HashMap<>();
    private Map<String, List<Object>> offlineFriends = new HashMap<>();
    private boolean acceptingFriends;

    // Rank
    private Rank rank;

    //Cosmetic Persist
    private ArrayList<Integer> cosmetics = new ArrayList<>();

    public boolean isOnline() {
        return this.conn != null;
    }

    public void load(boolean thread) {
        if (thread) {
            new Thread(() -> this.load(false)).start();
            return;
        }

        long start = System.currentTimeMillis();

        DBObject profile = WebServer.getInstance().getMongoManager().getProfileCollection().find(new BasicDBObject("_id", this.playerId.toString())).one();

        if (profile == null) { // Set Defaults.
            this.friendStatus = EnumFriendStatus.ONLINE;
            this.acceptingFriends = true;
            this.logOffTime = 0;
            this.logger.info("Took " + (System.currentTimeMillis() - start) + "ms to create a new player");
            WebServer.getInstance().getServerHandler().sendPacket(conn, new CBPacketFriendListUpdate(true, true, onlineFriends, offlineFriends));

            return;
        }
        this.processRank();

        if (this.isOnline()) this.friendStatus = EnumFriendStatus.ONLINE;
         else this.friendStatus = EnumFriendStatus.OFFLINE;

        if (this.isOnline()) this.logOffTime = 0;

        // Allow new mongo implementation without problems.
  /*      if (profile.get("rank") != null)
            this.rank = Rank.getRankById((int) profile.get("rank"));*/
        if (profile.get("cosmetics") != null)
            ((List<Integer>) profile.get("cosmetics")).forEach(string -> this.getCosmetics().add(string ));
        if (profile.get("accepting") != null)
            this.acceptingFriends = (boolean) profile.get("accepting");
        if (profile.get("friends") != null)
            ((List<String>) profile.get("friends")).forEach(string -> this.getFriends().add(PlayerFriend.fromJson(string)));
        if (profile.get("requestSent") != null)
            ((List<String>) profile.get("requestSent")).forEach(string -> this.getSentFriendRequests().add(PlayerFriendRequest.fromJson(string)));
        if (profile.get("requestReceived") != null)
            ((List<String>) profile.get("requestReceived")).forEach(string -> this.getReceivedFriendRequests().add(PlayerFriendRequest.fromJson(string)));

        this.logger.info("Took " + (System.currentTimeMillis() - start) + "ms to load " + this.getUsername() + " (" + (this.isOnline() ? this.conn.getRemoteSocketAddress() : "Not Online") + ")");
        if (this.isOnline()) this.sendAllPackets();
    }


    public void sendAllPackets() {
        WebServer.getInstance().getLogger().info("Sending all packets");
        ServerHandler handler = WebServer.getInstance().getServerHandler();
        handler.sendPacket(conn, new WSPacketCosmeticGive());
        PlayerFriendManager.sendFriendRequestBulk(this, handler);
        PlayerFriendManager.sendAllFriendRequestToPlayer(this);
        PlayerFriendManager.updateFriendForOthers(this);
        PlayerFriendManager.recacheFriendList(this);
        handler.sendPacket(conn, new CBPacketFriendListUpdate(true, true, onlineFriends, offlineFriends));
        for(Player online : PlayerManager.getPlayerMap().values()) {
            if(online != this)
                handler.sendPacket(conn, new WSPacketCosmeticGive(online.getPlayerId()));
        }


    }

    private void processRank() {
        if(username.equalsIgnoreCase("Tellinq")) {
            this.rank = Rank.TELLING;
        } else if(username.equalsIgnoreCase("Jegox")) {
            this.rank = Rank.JEGOX;
        } else if(username.equalsIgnoreCase("Moose1301")) {
            this.rank = Rank.MOOSE;
        } else
           this.rank = Rank.OWNER;

    }

    public void save(boolean thread) {
        long start = System.currentTimeMillis();
        if (thread) {
            new Thread(() -> this.save(false));
            return;
        }


        WebServer.getInstance().getMongoManager().getProfileCollection().update(new BasicDBObject("_id", this.playerId.toString()), this.toJson(), new DBCollectionUpdateOptions().upsert(true));
        this.logger.info("Took " + (System.currentTimeMillis() - start) + "ms to save " + this.getUsername());
    }

    private DBObject toJson() {
        return new BasicDBObjectBuilder().add("_id", this.playerId.toString())
                .add("username", this.username)
                .add("friends", PlayerFriendManager.friendsAsListWithJson(this))
                .add("requestSent", PlayerFriendManager.friendRequestSentAsListWithJson(this))
                .add("requestReceived", PlayerFriendManager.friendRequestReceivedAsListWithJson(this))
                .add("accepting", this.acceptingFriends)
                .add("rank", this.rank.id)
                .add("logOffTime", this.logOffTime)
                .add("cosmetics", this.cosmetics)
                .get();
    }

    @ConstructorProperties({ "playerId", "username" })
    public Player(UUID playerId, String username) {
        this.playerId = playerId;
        this.username = username;
        this.friends = new ArrayList<>();
        this.sentFriendRequests = new ArrayList<>();
        this.receivedFriendRequests = new ArrayList<>();
    }

}
