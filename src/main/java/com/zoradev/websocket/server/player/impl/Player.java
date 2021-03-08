package com.zoradev.websocket.server.player.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import com.mongodb.client.model.DBCollectionUpdateOptions;
import com.zoradev.websocket.server.player.impl.friend.PlayerFriend;
import com.zoradev.websocket.server.player.impl.friend.PlayerFriendManager;
import com.zoradev.websocket.server.player.impl.friend.PlayerFriendRequest;
import com.zoradev.websocket.server.player.impl.friend.objects.EnumFriendStatus;
import com.zoradev.websocket.server.player.impl.rank.Rank;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import com.zoradev.websocket.server.server.nethandler.impl.friend.CBPacketFriendListUpdate;
import com.zoradev.websocket.server.WebServer;
import lombok.Getter;
import lombok.Setter;
import com.zoradev.websocket.server.server.nethandler.impl.packetids.WSPacketCosmeticGive;
import com.zoradev.websocket.server.utils.Logger;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

import java.beans.ConstructorProperties;
import java.util.*;

@Getter @Setter @SuppressWarnings("unchecked")
public class Player {
    private UUID playerId;
    private String username;

    private Logger logger = WebServer.getInstance().getLogger();

    // Non Persist
    private long lastMessageSent;
    private String version;
    private EnumFriendStatus friendStatus;
    public String server;
    private WebSocket conn;
    private ClientHandshake handshake;
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
        this.processRank();

        if (profile == null) { // Set Defaults.
            this.friendStatus = EnumFriendStatus.ONLINE;
            this.acceptingFriends = true;
            this.logOffTime = 0;
            this.logger.info("Took " + (System.currentTimeMillis() - start) + "ms to create a new player");
            WebServer.getInstance().getServerHandler().sendPacket(conn, new CBPacketFriendListUpdate(Rank.isRankOverId(rank, Rank.VIP), true, onlineFriends, offlineFriends));

            return;
        }
        if(handshake != null) {
            this.server = handshake.getFieldValue("server");
        } else {
            this.server = "Unknown";
        }
        if (this.isOnline()) this.friendStatus = EnumFriendStatus.ONLINE;
         else this.friendStatus = EnumFriendStatus.OFFLINE;

        if (this.isOnline()) this.logOffTime = 0;

        // Allow new mongo implementation without problems.
       if (profile.get("rank") != null)
            this.rank = Rank.getRankById((int) profile.get("rank"));
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

        this.logger.info("Took " + (System.currentTimeMillis() - start) + "ms to load " + this.getUsername() + " (" + (this.isOnline() ?
                "Online" : "Not Online") + ")");
        if (this.isOnline()) this.sendAllPackets();
    }


    public void sendAllPackets() {
        ServerHandler handler = WebServer.getInstance().getServerHandler();
        PlayerFriendManager.sendFriendRequestBulk(this, handler);
        PlayerFriendManager.sendAllFriendRequestToPlayer(this);
        PlayerFriendManager.updateFriendForOthers(this);
        PlayerFriendManager.recacheFriendList(this);

        handler.sendPacket(conn, new CBPacketFriendListUpdate(Rank.isRankOverId(rank, Rank.VIP), true, onlineFriends, offlineFriends));
        handler.sendPacket(conn, new WSPacketCosmeticGive());
        WebServer.getInstance().getToGiveCosmetics().add(this);


    }

    private void processRank() {

       if(username.equalsIgnoreCase("Moose1301")) {
            this.rank = Rank.MOOSE;
        } else if(username.equalsIgnoreCase("SheepKiller69")) {
           this.rank = Rank.VIP;
       }else
           this.rank = Rank.USER;
      //  getLogger().info("Setting " + username + " Rank to " + rank.getName());

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
    public Player(UUID playerId, ClientHandshake handshake, String username) {
        this.playerId = playerId;
        this.username = username;
        this.friends = new ArrayList<>();
        this.server = "";
        this.sentFriendRequests = new ArrayList<>();
        this.receivedFriendRequests = new ArrayList<>();
        this.handshake = handshake;
    }
    @ConstructorProperties({ "playerId", "username" })
    public Player(UUID playerId, String username) {
        this.playerId = playerId;
        this.server = "";
        this.username = username;
        this.friends = new ArrayList<>();
        this.sentFriendRequests = new ArrayList<>();
        this.receivedFriendRequests = new ArrayList<>();
    }

}
