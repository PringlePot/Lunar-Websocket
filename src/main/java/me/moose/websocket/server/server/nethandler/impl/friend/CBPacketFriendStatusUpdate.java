package me.moose.websocket.server.server.nethandler.impl.friend;

import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import org.java_websocket.WebSocket;

import java.io.IOException;

public class CBPacketFriendStatusUpdate extends CBPacket {
    private boolean accepting;

    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException { }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
        this.accepting = in.readBoolean();
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {
        Player player = PlayerManager.getPlayerMap().get(conn.getAttachment());
        player.setAcceptingFriends(this.accepting);
    }
}
