package com.zoradev.websocket.server.server.nethandler.impl.packetids;

import com.zoradev.websocket.server.server.nethandler.ByteBufWrapper;
import com.zoradev.websocket.server.server.nethandler.CBPacket;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.UUID;

public class sendEmote extends CBPacket {
    UUID uuid;
    int id;
    public sendEmote() {}
    public sendEmote(UUID uuid, int id) {
        this.uuid = uuid;
        this.id = id;
    }
    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeLong(uuid.getMostSignificantBits());
        out.writeLong(uuid.getLeastSignificantBits());
        out.writeInt(id);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {

    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {

    }
}
