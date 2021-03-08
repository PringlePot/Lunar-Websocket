package com.zoradev.websocket.command.impl;

import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.impl.Player;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import com.zoradev.websocket.command.Command;
import com.zoradev.websocket.server.server.nethandler.impl.packetids.WSPacketCosmeticGive;
import org.java_websocket.WebSocket;

public class SendPacketsCommand extends Command {
    public SendPacketsCommand() {
        super("dev");
    }

    @Override
    public String execute(WebSocket conn, String p1, String[] args) {
        return this.handleConsoleCommand(conn);
    }

    private String handleConsoleCommand(WebSocket conn) {
        Player player = WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment());
        ServerHandler serverHandler = WebServer.getInstance().getServerHandler();
        serverHandler.sendPacket(conn, new WSPacketCosmeticGive());
     //  WebServer.getInstance().updateTags(conn, player.getUsername(), player.getPlayerId().toString(), player);
        return "§aDone";
    }
}

