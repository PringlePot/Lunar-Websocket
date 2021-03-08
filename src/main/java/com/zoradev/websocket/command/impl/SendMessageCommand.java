package com.zoradev.websocket.command.impl;

import com.zoradev.websocket.command.Command;
import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.PlayerManager;
import com.zoradev.websocket.server.player.impl.Player;
import com.zoradev.websocket.server.server.nethandler.impl.packetids.SendChatMessage;
import org.java_websocket.WebSocket;

public class SendMessageCommand extends Command {
    public SendMessageCommand() {
        super("sendmessage");
    }
    @Override
    public String execute(WebSocket var1, String var2, String[] var3) {
        if(var3.length < 2){
            return "sendmessage <player / all> <message>";
        }
        PlayerManager playerManager = WebServer.getInstance().getPlayerManager();
        try {
            if(!var3[0].equals("all")) {
                StringBuilder stringBuilder = new StringBuilder();
                for(int i = 2; i < var3.length; ++i) {
                    stringBuilder.append(var3[i]);
                }
                Player p = playerManager.getPlayerByName(var3[0]);
                if (p != null) {
                    WebServer.getInstance().getServerHandler().sendPacket(p.getConn(), new SendChatMessage(stringBuilder.toString().trim()));
                    return "Sent message to " + p.getUsername();
                }
            }
            else{
                for(WebSocket con : WebServer.getInstance().getConnections()){
                    StringBuilder stringBuilder = new StringBuilder();
                    for(int i = 2; i < var3.length; ++i) {
                        stringBuilder.append(var3[i]);
                    }
                    if (con != null) {
                        WebServer.getInstance().getServerHandler().sendPacket(con, new SendChatMessage(stringBuilder.toString().trim()));
                    }
                }
                return "Sent messages";
            }
        }
        catch (Exception e){
            return e.getMessage();
        }
        return "";
    }
}
