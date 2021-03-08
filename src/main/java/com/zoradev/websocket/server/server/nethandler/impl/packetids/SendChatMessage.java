package com.zoradev.websocket.server.server.nethandler.impl.packetids;

import com.zoradev.websocket.server.server.nethandler.ByteBufWrapper;
import com.zoradev.websocket.server.server.nethandler.CBPacket;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class SendChatMessage extends CBPacket {
    String message;
    public SendChatMessage() {
        message = "Test Message!";
    }
    public SendChatMessage(String message) {
        this.message = message;
    }
    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeString(message);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
    //    WebServer.getInstance().getLogger().info("Packet ID 20: " + in.readInt());

    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {

    }
}