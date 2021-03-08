package com.zoradev.websocket.server.server.nethandler.object;

import lombok.SneakyThrows;
import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.impl.Player;
import com.zoradev.websocket.server.server.nethandler.impl.packetids.WSPacketCosmeticGive;

public class RunShitThread extends Thread {

    @SneakyThrows
    @Override
    public void run() {
        while(true) {
            Thread.sleep(1000 * 30);
            int toSave = WebServer.getInstance().getToSave().size();
            for(Player player : WebServer.getInstance().getToSave()) {
                WebServer.getInstance().getPlayerManager().removePlayer(player.getPlayerId(), true);
            }
            WebServer.getInstance().getToSave().clear();
            WebServer.getInstance().getLogger().info("Bulk Saved " + toSave + " Profiles");
            int toGive = WebServer.getInstance().getToGiveCosmetics().size();
            for(Player player : WebServer.getInstance().getToGiveCosmetics()) {
                WebServer.getInstance().getServerHandler().sendPacket(player.getConn(), new WSPacketCosmeticGive());
            }
             WebServer.getInstance().getToGiveCosmetics().clear();
            WebServer.getInstance().getLogger().info("Updated " + toGive + " Cosmetics");
            Runtime.getRuntime().gc();
        }
    }
}
