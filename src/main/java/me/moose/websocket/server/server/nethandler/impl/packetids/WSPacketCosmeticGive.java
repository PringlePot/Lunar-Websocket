package me.moose.websocket.server.server.nethandler.impl.packetids;

import me.moose.websocket.server.WebServer;
import me.moose.websocket.server.player.impl.Player;
import me.moose.websocket.server.server.nethandler.ByteBufWrapper;
import me.moose.websocket.server.server.nethandler.CBPacket;
import me.moose.websocket.server.server.nethandler.ServerHandler;
import me.moose.websocket.server.server.objects.GenFromIndexFile;
import me.moose.websocket.server.server.objects.LunarLogoColors;
import org.java_websocket.WebSocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class WSPacketCosmeticGive extends CBPacket {
    UUID target;
    int cosmeticId;
    int color = -1;
    boolean update;
    boolean updatety = false;
    public WSPacketCosmeticGive() {
        this.cosmeticId = -1;
        this.update = false;
       // System.out.println("Setting user cosmetics");

    }
    public WSPacketCosmeticGive(UUID uuid) {
        this.target = uuid;
       this.update = false;
    }
    public WSPacketCosmeticGive(UUID uuid, boolean update) {
        this.target = uuid;
        this.update = update;
    }
    public WSPacketCosmeticGive(UUID uuid, boolean updatety, boolean a) {
        this.target = uuid;
        this.updatety = updatety;
    }
    public WSPacketCosmeticGive(UUID uuid, int Color) {
        this.target = uuid;
        this.update = true;
        this.color = Color;
    }
    @Override
    public void write(WebSocket conn, ByteBufWrapper out) throws IOException {
        if (target == null) {
            target = conn.getAttachment();
        }
        out.writeLong(target.getMostSignificantBits());
        out.writeLong(target.getLeastSignificantBits());
        Player player = WebServer.getInstance().getPlayerManager().getPlayerById(target);
        if (!update) {
            out.writeVarInt(GenFromIndexFile.getCosmetics().values().size());
            ArrayList<Integer> dupes = new ArrayList<>();
            for (String[] values : GenFromIndexFile.getCosmetics().values()) {
                int id = Integer.parseInt(values[0]);
                out.writeVarInt(id);
                out.writeBoolean(false);
               // out.writeBoolean(player.getCosmetics().contains(id));
            }

            //System.out.println("Added cosmetics to user " + player.getUsername());
            out.writeInt(WebServer.getInstance().getPlayerManager().getPlayerById(target).getRank().getColor());
            out.writeBoolean(true);

        }
        /*  out.writeVarInt(60);
        out.writeVarInt(97);
        out.writeString("Teams Map 3 EOTW");
        out.writeString("cloak");
        out.writeFloat(1.0F);
        out.writeString("cosmetics/cloaks/teams_map_3_eotw.png");
        out.writeBoolean(false); */
        else {
            if(color == -1) {
                out.writeVarInt(player.getCosmetics().size());
                for (int cosmId : player.getCosmetics()) {
                    String[] info = GenFromIndexFile.getCosmetics().get(cosmId);
                    int id = Integer.parseInt(info[0]);
                    String name = info[3];
                    out.writeVarInt(id);
                    out.writeBoolean(true);
                }
                out.writeInt(WebServer.getInstance().getPlayerManager().getPlayerById(target).getRank().getColor());
                out.writeBoolean(true);
            } else if(!updatety){
                out.writeVarInt(player.getCosmetics().size());
                for (int cosmId : player.getCosmetics()) {
                    String[] info = GenFromIndexFile.getCosmetics().get(cosmId);
                    int id = Integer.parseInt(info[0]);
                    String name = info[3];
                    out.writeVarInt(id);
                    out.writeBoolean(true);
                }
                out.writeInt(color);
                out.writeBoolean(true);
            } else {
                out.writeVarInt(0);
                for (int cosmId : player.getCosmetics()) {
                    String[] info = GenFromIndexFile.getCosmetics().get(cosmId);
                    int id = Integer.parseInt(info[0]);
                    String name = info[3];
                    out.writeVarInt(id);
                    out.writeBoolean(true);
                }
                out.writeInt(color);
                out.writeBoolean(true);
            }
        }

    }

    @Override
    public void read(WebSocket conn, ByteBufWrapper in) throws IOException {

    }

    @Override
    public void process(WebSocket conn, ServerHandler handler) throws IOException {

    }
}
