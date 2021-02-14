package me.moose.websocket.server.server.nethandler.impl.friend;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.player.impl.friend.PlayerFriendManager;
import me.moose.websocket.server.player.impl.friend.builder.PlayerFriendBuilder;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.UUID;

public class CBPacketFriendAcceptOrDeny extends CBPacket {
    private boolean added;
    private String playerId;

    public CBPacketFriendAcceptOrDeny() {}

    public CBPacketFriendAcceptOrDeny(boolean added, String playerId) {
        this.added = added;
        this.playerId = playerId;
    }


    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeBoolean(this.added);
        out.writeString(this.playerId);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
        this.added = in.readBoolean();
        this.playerId = in.readString(52);
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {
        Player expectedAccepter = PlayerManager.getPlayerMap().get(conn.getAttachment());
        Player expectedAddedOrDenied = WebServer.getInstance().getPlayerManager().getPlayerById(UUID.fromString(this.playerId));

        if (expectedAddedOrDenied != null) {
            boolean expectedAccepterIsRealAccepter = expectedAccepter.getReceivedFriendRequests().stream().anyMatch(playerFriendRequest -> playerFriendRequest.getPlayerId().equals(this.playerId));

            PlayerFriendManager.removeEachother(expectedAccepter, expectedAddedOrDenied);

            if (expectedAccepterIsRealAccepter && this.added) {
                PlayerFriendManager.addFriend(expectedAddedOrDenied, handler, new PlayerFriendBuilder().username(expectedAccepter.getUsername()).server("").playerId(expectedAccepter.getPlayerId().toString()).friendStatus(expectedAccepter.getFriendStatus()).online(true).status("Online").build(), expectedAccepter);
                PlayerFriendManager.addFriend(expectedAccepter, handler, new PlayerFriendBuilder().username(expectedAddedOrDenied.getUsername()).server("").playerId(expectedAddedOrDenied.getPlayerId().toString()).friendStatus(expectedAddedOrDenied.getFriendStatus()).online(true).status("Online").build(), expectedAddedOrDenied);
            }

            handler.sendPacket(expectedAccepter.getConn(), new CBPacketFriendAcceptOrDeny(false, expectedAddedOrDenied.getPlayerId().toString()));
            handler.sendPacket(expectedAddedOrDenied.getConn(), new CBPacketFriendAcceptOrDeny(false, expectedAccepter.getPlayerId().toString()));
        }
    }
}
