package com.zoradev.websocket.command.impl;

import com.zoradev.websocket.command.Command;
import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.impl.Player;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import com.zoradev.websocket.server.server.nethandler.impl.server.PacketCommand;
import org.java_websocket.WebSocket;

import java.util.concurrent.TimeUnit;

public class StatsCommand extends Command {
    public StatsCommand() {
        super("stats");
    }

    @Override
    public String execute(WebSocket conn, String p1, String[] args) {
        return this.handleConsoleCommand(conn);
    }

    private String handleConsoleCommand(WebSocket conn) {
        long end = System.currentTimeMillis();
        long uptimeseoncds = WebServer.getInstance().getStart() - end;

        Player player = WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment());
        ServerHandler serverHandler = WebServer.getInstance().getServerHandler();
        Runtime runtime = Runtime.getRuntime();
        serverHandler.sendPacket(conn, new PacketCommand("§aOnline: " + WebServer.getInstance().getOnlineUsers()));
        serverHandler.sendPacket(conn, new PacketCommand("§aRam Usage: " + this.format(runtime.maxMemory() - runtime.freeMemory())));
        return "§a";
    }
    public String format(long bytes) {
        double gb = (double)bytes / 1.0E9;
        return String.format("%.2f", gb) + " GB";
    }

}