package com.zoradev.websocket.server.server.nethandler.impl.packetids;

import com.zoradev.websocket.server.server.nethandler.ByteBufWrapper;
import com.zoradev.websocket.server.server.nethandler.CBPacket;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.PlayerManager;
import com.zoradev.websocket.server.player.impl.Player;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class doPlayerEmote extends CBPacket {
    int emoteId;
    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {

    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
        emoteId = in.readInt();
       // WebServer.getInstance().getLogger().info(WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment()).getUsername() + " is doing emote with id " + emoteId);
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {
        Player player = WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment());
        for(Player online : PlayerManager.getPlayerMap().values())
            handler.sendPacket(online.getConn(), new sendEmote(player.getPlayerId(), emoteId));
    }
}
