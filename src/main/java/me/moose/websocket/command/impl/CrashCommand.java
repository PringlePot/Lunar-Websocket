package me.moose.websocket.command.impl;


import me.moose.websocket.command.Command;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.impl.server.WSPacketForceCrash;
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
