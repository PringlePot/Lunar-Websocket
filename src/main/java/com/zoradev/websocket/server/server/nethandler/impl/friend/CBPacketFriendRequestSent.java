package com.zoradev.websocket.server.server.nethandler.impl.friend;

import com.zoradev.websocket.server.server.nethandler.ByteBufWrapper;
import com.zoradev.websocket.server.server.nethandler.CBPacket;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class CBPacketFriendRequestSent extends CBPacket {
    private String playerId;
    private String username;
    private boolean add;

    public CBPacketFriendRequestSent() {}

    public CBPacketFriendRequestSent(String playerId, String username, boolean add) {
        this.playerId = playerId;
        this.username = username;
        this.add = add;
    }

    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeString(this.playerId);
        out.writeString(this.username);
        out.writeBoolean(this.add);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException { }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException { }
}
