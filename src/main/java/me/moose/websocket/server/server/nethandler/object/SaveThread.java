package me.moose.websocket.server.server.nethandler.object;

import lombok.SneakyThrows;
import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.impl.Player;

public class SaveThread extends Thread {

    @SneakyThrows
    @Override
    public void run() {
        while(true) {
            Thread.sleep(1000 * 30);
            for(Player player : WebServer.getInstance().getToSave()) {
                WebServer.getInstance().getPlayerManager().removePlayer(player.getPlayerId(), true);
            }
            WebServer.getInstance().getToSave().clear();
            WebServer.getInstance().getLogger().info("Bulk Saved User Profiles");
            Runtime.getRuntime().gc();
        }
    }
}
