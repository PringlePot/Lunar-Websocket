package com.zoradev.websocket.server.server.nethandler.impl.packetids;

import com.google.common.collect.Maps;
import com.zoradev.websocket.server.server.nethandler.ByteBufWrapper;
import com.zoradev.websocket.server.server.nethandler.CBPacket;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.Map;

public class PacketId64 extends CBPacket {
    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {

    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
        int x = in.readVarInt();
        Map<String, Boolean> map = Maps.newHashMap();
        for(int i = 0; i < x; i++) {
            map.put(in.readString(500), in.readBoolean());
        }
        String someString = in.readString(500);
    //    WebServer.getInstance().getLogger().info("Some String: " + someString + " Some Int " + x);
        for(Map.Entry<String, Boolean> test : map.entrySet()) {
       //    WebServer.getInstance().getLogger().info(test.getKey() + " " + test.getValue());
        }
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {

    }
}
