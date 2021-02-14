package me.moose.websocket.server.server.nethandler.impl.packetids;

import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class BanMessagePacketI extends CBPacket {

    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        out.writeInt(2);
        out.writeString("Tellinq");
        out.writeInt(3 );
        out.writeString("You were already detected, we just didn't ban you yet");
        out.writeString("L");
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {

    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {

    }
}
