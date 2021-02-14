package me.moose.websocket.server.server.nethandler.impl.friend;

import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.UUID;

public class CBPacketFriendRemove extends CBPacket {
    private String playerId;

    private boolean read;

    public CBPacketFriendRemove() {}

    public CBPacketFriendRemove(String playerId) {
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
            Player remover = PlayerManager.getPlayerMap().get(conn.getAttachment());
            Player removed = PlayerManager.getPlayerMap().get(UUID.fromString(this.playerId));

            remover.getFriends().removeIf(friend -> friend.getPlayerId().equals(this.playerId));
            removed.getFriends().removeIf(friend -> friend.getPlayerId().equals(remover.getPlayerId().toString()));

            if (removed.isOnline()) handler.sendPacket(removed.getConn(), new CBPacketFriendRemove(remover.getPlayerId().toString()));
        }
    }
}
