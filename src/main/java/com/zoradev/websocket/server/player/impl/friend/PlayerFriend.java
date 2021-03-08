package com.zoradev.websocket.server.player.impl.friend;

import com.google.gson.JsonParser;
import com.zoradev.websocket.server.player.impl.friend.objects.EnumFriendStatus;
import com.zoradev.websocket.server.WebServer;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter @Setter
public class PlayerFriend {
    private String playerId;
    private String username;
    private String status;
    private String server;
    private boolean online;
    private long offlineSince;
    private EnumFriendStatus friendStatus;

    @ConstructorProperties({ "playerId", "username", "status", "server", "online", "offlineSince", "friendStatus" })
    public PlayerFriend(String playerId, String username, String status, String server, boolean online, long offlineSince, EnumFriendStatus friendStatus) {
        this.friendStatus = EnumFriendStatus.ONLINE;
        this.playerId = playerId;
        this.username = username;
        this.status = status;
        this.server = server;
        this.online = online;
        this.offlineSince = offlineSince;
        this.friendStatus = friendStatus;
    }

    public String toJson() { return WebServer.GSON.toJson(this); }

    public static PlayerFriend fromJson(String friendJson) {
        return WebServer.GSON.fromJson(new JsonParser().parse(friendJson), PlayerFriend.class);
    }
}
