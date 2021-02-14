package me.moose.websocket.server.server.nethandler.impl.packetids;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
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
        WebServer.getInstance().getLogger().info(WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment()).getUsername() + " is doing emote with id " + emoteId);
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {
        Player player = WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment());
        for(Player online : PlayerManager.getPlayerMap().values())
            handler.sendPacket(online.getConn(), new sendEmote(player.getPlayerId(), emoteId));
    }
}
