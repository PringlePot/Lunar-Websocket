package com.zoradev.websocket.command.impl;


import com.zoradev.websocket.command.Command;
import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.PlayerManager;
import com.zoradev.websocket.server.player.impl.Player;
import com.zoradev.websocket.server.server.nethandler.impl.server.WSPacketForceCrash;
import org.java_websocket.WebSocket;

public class CrashCommand extends Command {
    public CrashCommand() {
        super("crash");
    }
    @Override
    public String execute(WebSocket var1, String var2, String[] var3) {
        if(var3.length < 1){
            return "Usage: crash <player>";
        }
        PlayerManager playerManager = WebServer.getInstance().getPlayerManager();
        try {
            Player p = playerManager.getPlayerByName(var3[0]);
            if(p != null) {
                WebServer.getInstance().getServerHandler().sendPacket(p.getConn(), new WSPacketForceCrash());
                return "Crashed " + p.getUsername() + " hehe";
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
