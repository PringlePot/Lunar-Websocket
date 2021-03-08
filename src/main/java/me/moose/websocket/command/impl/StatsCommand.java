package me.moose.websocket.command.impl;

import me.moose.websocket.command.Command;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.server.nethandler.impl.server.PacketCommand;
import me.moose.websocket.utils.Config;
import org.java_websocket.WebSocket;

public class StatsCommand extends Command {
    public StatsCommand() {
        super("stats");
    }

    @Override
    public String execute(WebSocket conn, String p1, String[] args) {
        return this.handleConsoleCommand(conn);
    }

    private String handleConsoleCommand(WebSocket conn) {
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