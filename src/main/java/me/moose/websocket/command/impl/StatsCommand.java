package me.moose.websocket.command.impl;

import me.moose.websocket.command.Command;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.server.nethandler.impl.server.PacketCommand;
import me.moose.websocket.utils.Config;
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
        serverHandler.sendPacket(conn, new PacketCommand("§aUptime: " + secondsToTimeString(uptimeseoncds)));
        return "§a";
    }
    public String format(long bytes) {
        double gb = (double)bytes / 1.0E9;
        return String.format("%.2f", gb) + " GB";
    }
    public String secondsToTimeString(long seconds) {
        int day = (int)TimeUnit.SECONDS.toDays(seconds);
        long hours = TimeUnit.SECONDS.toHours(seconds) - (day *24);
        long minute = TimeUnit.SECONDS.toMinutes(seconds) - (TimeUnit.SECONDS.toHours(seconds)* 60);
        long second = TimeUnit.SECONDS.toSeconds(seconds) - (TimeUnit.SECONDS.toMinutes(seconds) *60);

        return  "H: " + hours + " M: " + minute + " S: " + second;
    }
}