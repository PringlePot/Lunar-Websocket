package me.moose.websocket.server.server.nethandler.object;

import lombok.SneakyThrows;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.player.impl.rank.Rank;
import me.moose.websocket.server.server.nethandler.impl.packetids.WSPacketCosmeticGive;
import me.moose.websocket.server.server.nethandler.impl.packetids.WSPacketCosmeticSet;

import java.util.UUID;

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
