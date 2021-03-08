package com.zoradev.websocket.server.player.impl.friend.builder;

import com.zoradev.websocket.server.player.impl.friend.PlayerFriend;
import com.zoradev.websocket.server.player.impl.friend.objects.EnumFriendStatus;

public class PlayerFriendBuilder {
    private String playerId;
    private String username;
    private String status;
    private String server;
    private boolean online;
    private long offlineSince;
    private EnumFriendStatus friendStatus;

    public PlayerFriendBuilder() {}

    public PlayerFriendBuilder playerId(final String playerId) {
        this.playerId = playerId;
        return this;
    }

    public PlayerFriendBuilder username(final String username) {
        this.username = username;
        return this;
    }

    public PlayerFriendBuilder status(final String status) {
        this.status = status;
        return this;
    }

    public PlayerFriendBuilder server(final String server) {
        this.server = server;
        return this;
    }

    public PlayerFriendBuilder online(final boolean online) {
        this.online = online;
        return this;
    }

    public PlayerFriendBuilder offlineSince(final long offlineSince) {
        this.offlineSince = offlineSince;
        return this;
    }

    public PlayerFriendBuilder friendStatus(final EnumFriendStatus friendStatus) {
        this.friendStatus = friendStatus;
        return this;
    }

    public PlayerFriend build() {
        return new PlayerFriend(this.playerId, this.username, this.status, this.server, this.online, this.offlineSince, this.friendStatus);
    }
}
