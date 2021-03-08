package com.zoradev.websocket.server.player.impl.friend;

import com.google.gson.JsonParser;
import com.zoradev.websocket.server.WebServer;
import lombok.Getter;

@Getter
public class PlayerFriendRequest {
    private final String username;
    private final String playerId;
    private boolean request;

    public PlayerFriendRequest(String username, String playerId) {
        this.username = username;
        this.playerId = playerId;
    }

    public PlayerFriendRequest setRequest(boolean request) {
        this.request = request;
        return this;
    }

    public String toJson() { return WebServer.GSON.toJson(this); }

    public static PlayerFriendRequest fromJson(String friendRequestJson) {
        return WebServer.GSON.fromJson(new JsonParser().parse(friendRequestJson), PlayerFriendRequest.class);
    }
}
