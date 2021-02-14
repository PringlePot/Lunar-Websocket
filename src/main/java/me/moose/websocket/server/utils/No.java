package me.moose.websocket.server.utils;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.impl.packetids.WSFriendMessage;

public class No extends Thread {
    public No() {
        super("No");
    }
    @Override
    public void run() {
        while (true) {
            Player player = WebServer.getInstance().getPlayerManager().getPlayerByName("Tellinq");
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));
            WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSFriendMessage("L", "Spam!"));

        }
    }
}
