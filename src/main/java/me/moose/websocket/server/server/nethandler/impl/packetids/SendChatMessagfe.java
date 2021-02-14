package me.moose.websocket.server.server.nethandler.impl.packetids;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.security.PublicKey;

public class SendChatMessagfe extends CBPacket {
    String message;
    public SendChatMessagfe() {
        message = "Test Message!";
    }
    public SendChatMessagfe(String message) {
        this.message = message;
    }
    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeString(message);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
        WebServer.getInstance().getLogger().info("Packet ID 20: " + in.readInt());

    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {

    }
}