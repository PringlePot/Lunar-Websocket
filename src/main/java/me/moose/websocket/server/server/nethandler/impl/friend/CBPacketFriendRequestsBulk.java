package me.moose.websocket.server.server.nethandler.impl.friend;

import com.google.gson.JsonArray;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import lombok.Getter;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class CBPacketFriendRequestsBulk extends CBPacket {
    private String rawFriendRequests;
    @Getter private JsonArray friendRequests;

    public CBPacketFriendRequestsBulk() {}

    public CBPacketFriendRequestsBulk(String rawFriendRequests) { this.rawFriendRequests = rawFriendRequests; }

    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeString(this.rawFriendRequests);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {

    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {

    }
}
