package com.zoradev.websocket.server.server.nethandler.object;

import com.zoradev.websocket.server.WebServer;
import com.zoradev.websocket.server.player.PlayerManager;
import com.zoradev.websocket.server.player.impl.Player;
import lombok.SneakyThrows;
import com.zoradev.websocket.server.server.nethandler.impl.packetids.WSPacketCosmeticGive;

public class UpdateTagThread extends Thread {

    @SneakyThrows
    @Override
    public void run() {
        while (true) {

            WebServer.getInstance().updateTags();
            for (Player player : PlayerManager.getPlayerMap().values()) {
                for (Player online : PlayerManager.getPlayerMap().values()) {

                    WebServer.getInstance().getServerHandler().sendPacket(online.getConn(), new WSPacketCosmeticGive(player.getPlayerId(), true));
                }
            }
            Thread.sleep(750);
        }
    }
}
