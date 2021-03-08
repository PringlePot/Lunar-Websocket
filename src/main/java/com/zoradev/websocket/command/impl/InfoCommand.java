package com.zoradev.websocket.command.impl;

import com.zoradev.websocket.command.Command;
import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.PlayerManager;
import com.zoradev.websocket.server.player.impl.Player;
import com.zoradev.websocket.server.server.nethandler.ServerHandler;
import com.zoradev.websocket.server.server.nethandler.impl.server.PacketCommand;
import com.zoradev.websocket.utils.Config;
import org.java_websocket.WebSocket;

public class InfoCommand extends Command {
    public InfoCommand() {
        super("info");
    }

    @Override
    public String execute(WebSocket conn, String p1, String[] args) {
        return this.handleConsoleCommand(conn, args);
    }

    private String handleConsoleCommand(WebSocket conn, String[] args) {
        if(args.length < 1){
            return "info <player>";
        }
        ServerHandler serverHandler = WebServer.getInstance().getServerHandler();
        PlayerManager playerManager = WebServer.getInstance().getPlayerManager();
        try {
            Player p = playerManager.getPlayerByName(args[0]);
            if(p != null) {
                serverHandler.sendPacket(conn, new PacketCommand("§aRank: " + p.getRank().getName()));
                serverHandler.sendPacket(conn, new PacketCommand("§aServer: " + p.getServer()));
                serverHandler.sendPacket(conn, new PacketCommand("§aFriend Count: " + p.getFriends().size()));
                serverHandler.sendPacket(conn, new PacketCommand("§aFriend State: " + p.getFriendStatus().getName()));
                serverHandler.sendPacket(conn, new PacketCommand("§aOnline Friend Count: " + p.getOnlineFriends().size()));
                serverHandler.sendPacket(conn, new PacketCommand("§aSent Friend Requests: " + p.getSentFriendRequests().size()));
                serverHandler.sendPacket(conn, new PacketCommand("§aRevived Friend Requests: " + p.getReceivedFriendRequests().size()));
                return "§aVersion: " + p.getVersion();
            }
            else{
                return "Invalid Player";
            }
        }
        catch (Exception e){
            return e.getMessage();
        }
    }

}

