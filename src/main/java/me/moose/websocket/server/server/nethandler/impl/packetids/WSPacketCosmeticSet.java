package me.moose.websocket.server.server.nethandler.impl.packetids;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.cosmetics.CosmeticType;
import me.moose.websocket.server.player.PlayerManager;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.server.objects.GenFromIndexFile;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.UUID;

public class WSPacketCosmeticSet extends CBPacket {
    UUID uuid;
    long cosmeticId;
    public WSPacketCosmeticSet() {}

    @Override

    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {

    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {
        cosmeticId = -1;
        int inAmount = in.readInt();
        Player player = WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment());
        player.getCosmetics().clear();
        for(int i = 0; i < inAmount; i++) {
            long cosmeticId = in.readLong();
            boolean state = in.readBoolean();
            if(state) {
                this.cosmeticId = cosmeticId;
                String type = GenFromIndexFile.getCosmetics().get((int)cosmeticId)[4];
                CosmeticType cType = CosmeticType.valueOf(type.toUpperCase());
                player.getCosmetics().add((int)cosmeticId);
            }
        }

        if(this.cosmeticId == -1) {
            this.cosmeticId = 1;
            System.out.println("STill -1");
        }
    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {
        Player player = WebServer.getInstance().getPlayerManager().getPlayerById(conn.getAttachment());
        for(Player online : PlayerManager.getPlayerMap().values()) {
            if (online != player) {
                handler.sendPacket(online.getConn(), new WSPacketCosmeticGive(player.getPlayerId(), true));
                WebServer.getInstance().getLogger().info("Sending cosmetics to player " + online.getUsername());
            }
        }
    }
}
