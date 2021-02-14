package me.moose.websocket.server.server.nethandler.impl.friend;

import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.UUID;

public class CBPacketFriendStartedTyping extends CBPacket {
    private String playerId;

    private boolean read;

    public CBPacketFriendStartedTyping() {}

    public CBPacketFriendStartedTyping(String playerId) {
        this.playerId = playerId;
    }

    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeString(this.playerId);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
        this.playerId = in.readString(52);
        this.read = true;
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {
        if (this.read) {
            Player sender = PlayerManager.getPlayerMap().get(conn.getAttachment());
            Player target = PlayerManager.getPlayerMap().get(UUID.fromString(this.playerId));

            if (target != null) {
                handler.sendPacket(target.getConn(), new CBPacketFriendStartedTyping(sender.getPlayerId().toString()));
            }
        }
    }
}
