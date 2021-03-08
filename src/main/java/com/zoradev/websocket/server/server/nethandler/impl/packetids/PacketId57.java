package com.zoradev.websocket.server.server.nethandler.impl.packetids;

import com.zoradev.websocket.server.server.nethandler.ByteBufWrapper;
import com.zoradev.websocket.server.server.nethandler.CBPacket;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class PacketId57 extends CBPacket {

    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
       for(int i = 0; i < 30; i++)
           out.writeVarInt(i);
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
  //      WebServer.getInstance().getLogger().info("Packet ID 56: " + in.readVarInt());
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {

    }
}