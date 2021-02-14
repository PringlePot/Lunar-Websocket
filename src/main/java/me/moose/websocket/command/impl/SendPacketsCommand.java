package me.moose.websocket.command.impl;

import me.moose.websocket.command.Command;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.server.nethandler.impl.packetids.*;
import org.java_websocket.WebSocket;

import java.security.PublicKey;
import java.util.Arrays;

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
        serverHandler.sendPacket(conn, new BanMessagePacketI());
        serverHandler.sendPacket(conn, new PacketId57());
        serverHandler.sendPacket(conn, new SendChatMessagfe());
        serverHandler.sendPacket(conn, new HostFilePacket());
        serverHandler.sendPacket(conn, new EmoteGive());
        serverHandler.sendPacket(conn, new WSPacketCosmeticGive());
     //   WebServer.getInstance().updateTags(conn, player.getUsername(), player.getPlayerId().toString(), player);
        return "§aDone";
    }
}

