package com.zoradev.websocket.server.server.nethandler.impl.server;


import com.zoradev.websocket.server.server.nethandler.ByteBufWrapper;
import com.zoradev.websocket.server.server.nethandler.CBPacket;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class WSPacketForceCrash
        extends CBPacket {


    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {
    }

}