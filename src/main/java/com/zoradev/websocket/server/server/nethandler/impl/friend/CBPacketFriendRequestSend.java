package com.zoradev.websocket.server.server.nethandler.impl.friend;

import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.impl.Player;
import com.zoradev.websocket.server.player.impl.friend.PlayerFriendRequest;
import com.zoradev.websocket.server.server.nethandler.ByteBufWrapper;
import com.zoradev.websocket.server.server.nethandler.CBPacket;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import com.zoradev.websocket.server.server.nethandler.impl.packetids.WSFriendMessage;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class CBPacketFriendRequestSend extends CBPacket {
    private String playerId;
    private String username;

    public CBPacketFriendRequestSend() {}

    public CBPacketFriendRequestSend(String playerId, String username) {
        this.playerId = playerId;
        this.username = username;
    }


    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeString(this.playerId);
        out.writeString(this.username);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
        this.playerId = in.readString(52);
        this.username = in.readString(32);
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {
        Player senderPlayer = WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment());
        Player targetPlayer = WebServer.getInstance().getPlayerManager().getPlayerByName(this.username);

        if (targetPlayer == null) {
            handler.sendPacket(conn, new WSFriendMessage(" ", "Unknown User"));
            return;
        } else if (senderPlayer.getSentFriendRequests().stream().anyMatch(playerFriendRequest -> playerFriendRequest.getPlayerId().equals(targetPlayer.getPlayerId().toString()))) {
            return;
        }  else if (senderPlayer.getReceivedFriendRequests().stream().anyMatch(playerFriendRequest -> playerFriendRequest.getPlayerId().equals(targetPlayer.getPlayerId().toString()))) {
            return;
        }  else if (senderPlayer == targetPlayer) {
            handler.sendPacket(conn, new WSFriendMessage(" ", "You cannot friend your self"));
            return;

        } else if (senderPlayer.getFriends().stream().anyMatch(friend -> friend.getPlayerId().equals(targetPlayer.getPlayerId().toString()))) {
            handler.sendPacket(conn, new WSFriendMessage(" ", "That User is already your friend"));
            return;
        } else if (!targetPlayer.isAcceptingFriends()) {
            handler.sendPacket(conn, new WSFriendMessage(" ", "That User isnt accepting friends"));
            return;
        }


        if (targetPlayer.isOnline())
            handler.sendPacket(targetPlayer.getConn(), new CBPacketFriendRequestSend(senderPlayer.getPlayerId().toString(), senderPlayer.getUsername()));

        handler.sendPacket(conn, new CBPacketFriendRequestSent(targetPlayer.getPlayerId().toString(), targetPlayer.getUsername(), true));

        senderPlayer.getSentFriendRequests().add(new PlayerFriendRequest(targetPlayer.getUsername(), targetPlayer.getPlayerId().toString()));
        targetPlayer.getReceivedFriendRequests().add(new PlayerFriendRequest(senderPlayer.getUsername(), senderPlayer.getPlayerId().toString()));

        if (!targetPlayer.isOnline())
            WebServer.getInstance().getPlayerManager().removePlayer(targetPlayer.getPlayerId(), false);
    }
}
