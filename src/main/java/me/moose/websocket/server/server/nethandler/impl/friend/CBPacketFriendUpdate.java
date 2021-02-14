package me.moose.websocket.server.server.nethandler.impl.friend;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.player.impl.friend.PlayerFriendManager;
import me.moose.websocket.server.player.impl.friend.builder.PlayerFriendBuilder;
import me.moose.websocket.server.player.impl.friend.objects.EnumFriendStatus;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.UUID;

public class CBPacketFriendUpdate extends CBPacket {
    private boolean online;
    private long onlineStatusOrOffline;
    private String playerId;
    private String username;

    private boolean read;

    public CBPacketFriendUpdate() {}

    public CBPacketFriendUpdate(boolean online, long onlineStatusOrOffline, String playerId, String username) {
        this.online = online;
        this.onlineStatusOrOffline = onlineStatusOrOffline;
        this.playerId = playerId;
        this.username = username;
    }

    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeString(this.playerId);
        out.writeString(this.username);
        out.writeLong(this.onlineStatusOrOffline);
        out.writeBoolean(this.online);
        this.read = false;
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
        this.playerId = in.readString(52);
        this.username = in.readString(32);
        this.onlineStatusOrOffline = in.readLong();
        this.online = in.readBoolean();
        this.read = true;
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {
        if (this.read) {
            Player player = PlayerManager.getPlayerMap().get(conn.getAttachment());
            player.setFriendStatus(EnumFriendStatus.getByOrdinal((int) this.onlineStatusOrOffline));

            if (!player.getFriends().isEmpty()) {
                player.getFriends().forEach(friend -> {
                    Player friendPlayer = WebServer.getInstance().getPlayerManager().getPlayerById(UUID.fromString(friend.getPlayerId()));

                    if (friendPlayer != null) {
                        if (!player.getFriendStatus().equals(EnumFriendStatus.OFFLINE)) {
                            PlayerFriendManager.updateFriend(friendPlayer, true, new PlayerFriendBuilder().username(player.getUsername()).playerId(player.getPlayerId().toString()).server("").online(true).friendStatus(player.getFriendStatus()).status("Online").build(), player);
                        } else {
                            PlayerFriendManager.updateFriend(friendPlayer, true, new PlayerFriendBuilder().username(player.getUsername()).playerId(player.getPlayerId().toString()).server("").online(false).friendStatus(EnumFriendStatus.OFFLINE).offlineSince(System.currentTimeMillis()).status("Online").build(), player);
                        }
                    }
                });
            }
        }
    }
}
