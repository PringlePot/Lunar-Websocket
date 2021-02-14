package me.moose.websocket.server.server.nethandler.impl.packetids;

import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class WSFriendMessage extends CBPacket {
    String name;
    String message;
    public WSFriendMessage() {}
    public WSFriendMessage(String name, String message) {
        this.name = name;
        this.message = message;
    }

    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeString(name);
        out.writeString(message);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {

    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {

    }
}
