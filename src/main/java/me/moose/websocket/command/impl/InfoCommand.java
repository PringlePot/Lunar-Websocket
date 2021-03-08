package me.moose.websocket.command.impl;

import me.moose.websocket.command.Command;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.server.nethandler.impl.packetids.WSPacketCosmeticGive;
import me.moose.websocket.utils.Config;
import org.java_websocket.WebSocket;

import java.net.MalformedURLException;
import java.net.URL;

public class InfoCommand extends Command {
    public InfoCommand() {
        super("info");
    }

    @Override
    public String execute(WebSocket conn, String p1, String[] args) {
        return this.handleConsoleCommand(conn);
    }

    private String handleConsoleCommand(WebSocket conn) {
        Player player = WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment());
        return "§a" + "Server: " + Config.getTldString(player.getServer());
    }

}

