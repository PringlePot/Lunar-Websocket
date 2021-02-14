package me.moose.websocket.server.server.nethandler.impl.server;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.player.impl.friend.PlayerFriend;
import me.moose.websocket.server.player.impl.friend.PlayerFriendManager;
import me.moose.websocket.server.player.impl.friend.builder.PlayerFriendBuilder;
import me.moose.websocket.server.player.impl.friend.objects.EnumFriendStatus;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.UUID;

public class CBPacketServerUpdate extends CBPacket {
    private String playerId;
    private String serverAddress;

    public CBPacketServerUpdate() {}

    public CBPacketServerUpdate(String playerId, String serverAddress) {
        this.playerId = playerId;
        this.serverAddress = serverAddress;
    }

    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeString(this.playerId);
        out.writeString(this.serverAddress);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
        this.playerId = in.readString(52);
        this.serverAddress = in.readString(100);
        System.out.println(WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment()).getUsername() + " Server: " + serverAddress);
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {
        Player player = PlayerManager.getPlayerMap().get(conn.getAttachment());

        if (this.serverAddress.equalsIgnoreCase(player.getServer())) return;

        if (!this.serverAddress.equalsIgnoreCase("")) {
            player.setServer(this.serverAddress);
        } else {
            player.setServer("");
        }

        if (!player.getFriends().isEmpty()) {
            for (PlayerFriend friend : player.getFriends()) {
                Player friendPlayer = PlayerManager.getPlayerMap().get(UUID.fromString(friend.getPlayerId()));

                if (friendPlayer != null) {
                    PlayerFriendManager.updateFriend(friendPlayer, false,new PlayerFriendBuilder().username(player.getUsername()).playerId(player.getPlayerId().toString()).friendStatus(player.getFriendStatus()).online((player.getFriendStatus() != EnumFriendStatus.OFFLINE)).server(this.serverAddress).build(), player);
                    handler.sendPacket(friendPlayer.getConn(), new CBPacketServerUpdate(player.getPlayerId().toString(), this.serverAddress));
                }
            }
        }
    }
}
