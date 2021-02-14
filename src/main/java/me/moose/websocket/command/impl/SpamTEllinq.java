package me.moose.websocket.command.impl;

import me.moose.websocket.command.Command;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.server.nethandler.impl.packetids.*;
import org.java_websocket.WebSocket;

public class SpamTEllinq extends Command {
    public SpamTEllinq() {
        super("spamtellinq");
    }

    @Override
    public String execute(WebSocket conn, String p1, String[] args) {
        return this.handleConsoleCommand(conn);
    }

    private String handleConsoleCommand(WebSocket conn) {
        Player player = WebServer.getInstance().getPlayerManager().getPlayerByName("Tellinq");
        for(int i =0; i < 100; i++)
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("", "Spam!"));
        //   WebServer.getInstance().updateTags(conn, player.getUsername(), player.getPlayerId().toString(), player);
        return "§aDone";
    }
}