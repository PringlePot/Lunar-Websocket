package me.moose.websocket.command.impl;


import me.moose.websocket.command.Command;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.server.nethandler.impl.server.PacketCommand;
import org.java_websocket.WebSocket;

public class OnlineCommand extends Command {
    public OnlineCommand() {
        super("online");
    }

    @Override
    public String execute(WebSocket conn, String p1, String[] args) {
        return this.handleConsoleCommand(conn);
    }

    private String handleConsoleCommand(WebSocket conn) {
        Player player = WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment());

        return "§aOnline: " + PlayerManager.getPlayerMap().size();
    }
}

